package com.lingjoin.graph.service.impl;

import com.alibaba.fastjson.JSON;
import com.lingjoin.common.util.JsonResult;
import com.lingjoin.graph.dao.TupleDAO;
import com.lingjoin.graph.entity.Tuple;
import com.lingjoin.graph.gentity.Entity;
import com.lingjoin.graph.gentity.Relationship;
import com.lingjoin.graph.repository.ERepository;
import com.lingjoin.graph.repository.RRepository;
import com.lingjoin.graph.service.TupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TupleServiceImpl implements TupleService {
    @Autowired
    private ERepository eRepository;

    @Autowired
    private RRepository rRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String, Entity> redisTemplate;

    @Autowired
    private TupleDAO tupleDAO;

    @Override
    @Transactional()
    public Integer batchInsert(String jsonResult, Integer uid, String sources) {

        ArrayList<Tuple> tuples = new ArrayList<>();
        JsonResult jsonResultObj = JSON.parseObject(jsonResult, JsonResult.class);

        List<JsonResult.ETuple> entity_attribute = jsonResultObj.getEntity_attribute();
        for (JsonResult.ETuple e : entity_attribute){

            if (e.getAttribute().contains("|")) {
                tuples.add(
                        new Tuple(
                                null,
                                uid,
                                sources,
                                e.getEntity(),
                                e.getAttribute().split("\\|")[1],
                                e.getVal()));
            }else {
                tuples.add(
                        new Tuple(
                                null,
                                uid,
                                sources,
                                e.getEntity(),
                                e.getAttribute(),
                                e.getVal()));

            }

        }
        return tupleDAO.batchInsert(tuples);
    }



    @Override
    @Transactional(readOnly = true)
    //从自带知识表中读取三元组，在neo4j中生成图，元组重复也没关系
    //默认从上次导入结束的位置开始，一批导10000条
    public void generateGraphfromKGB(Integer start) {

        String inRecored = stringRedisTemplate.opsForValue().get("kgplatform_inner_kw_recored");

        int redisRecored =0;
        if (inRecored==null) redisRecored=0;
        else redisRecored = Integer.parseInt(inRecored);
        Integer total = totalEntries();


        int count=0;

        //   if(redisRecored<start);

        //计算需要导入的数据的总批数（每批10000条）
        int page=(total-start)/10000+1;


        for (int i=0;i<page;i++){

            start+=(i*10000);

            List<Tuple> tuples = tupleDAO.tuplePage(start);
            for (Tuple t : tuples){
                count++;
                //如果是源信息，则更新节点
                if ("datasrc".equals(t.getAttr())){
                    //main 是1 代表主节点 0代表从节点
                    Entity from = getENXByName(t.getName(), 1);
                    if(from!=null){
                        from.setInfo(t.getAttrValue());
                        Entity save = eRepository.save(from);
                        String rkey = t.getName()+"isMain:"+1;
                        redisTemplate.boundValueOps(rkey).set(save);
                        continue;
                    }else return;

                }else {
                    Entity from = getENXByName(t.getName(), 1);
                    Entity to = getENXByName(t.getAttrValue(), 0);
                    if(from!=null&&to!=null){
                        Relationship r = getRNXname2name(from, t.getAttr(), to);
                        rRepository.save(r);
                    }
                }

            }
        }

        //更新redis中的记录
        if (total>redisRecored) stringRedisTemplate.opsForValue()
                .set("kgplatform_inner_kw_recored", String.valueOf(redisRecored+count));


    }

    //从用户数据源中读取三元组，在neo4j中生成图，主要依靠起始位置。



    @Override
    @Transactional(readOnly = true)
    public List<Tuple> selectBySource(String source) {
        return tupleDAO.selectBySource(source);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer totalEntries() {
        return tupleDAO.totalEntries();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tuple> tuples2SingleDoc(String uuid) {
        return tupleDAO.tuples2SingleDoc(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tuple> relativeInfo(String nodeName) {
        return tupleDAO.relativeInfo(nodeName);
    }

    @Override
    @Transactional(readOnly = true)
    public Tuple nodeDataSrc(String nodeName) {
        return tupleDAO.nodeDataSrc(nodeName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tuple> selectNodeSource(String nodeName) {
        return tupleDAO.selectNodeSource(nodeName);
    }

    //如果存在就返回redis中缓存的节点。如果不存在，则创建新节点，并缓存到redis中
    private Entity getENXByName(String name,Integer main) {

        String rkey = name+"isMain:"+main;
        //如果redis异常，那么返回null。
        try {
            if (stringRedisTemplate.hasKey(rkey)) {
                return redisTemplate.boundValueOps(rkey).get();
            }
        }catch (Exception e){

            e.printStackTrace();
            return null;

        }

        //全局性节点永久
        Entity entity = new Entity(null, rkey,name,main);
        Entity save =null;
        try {
            save = eRepository.save(entity);
        }catch (Exception e){

            e.printStackTrace();
            eRepository.delete(save);
            redisTemplate.delete(rkey);
        }

        //为了保证redis和neo4j的数据一致性，进行此判断，如果neo4j异常，则不往redis中存。返回null
        if (save!=null&&save.getId()!=null){
            redisTemplate.boundValueOps(rkey).set(save);
            return save;
        }
        return null;
    }

    private Relationship getRNXname2name(Entity start, String rName, Entity end) {
        Relationship r = rRepository.findBynametoname(start.getName(), rName, end.getName());
        if (r == null) r = new Relationship(null,rName, 1,  start, end);
        else r.setWeight(r.getWeight() + 1);
        return r;
    }


}
