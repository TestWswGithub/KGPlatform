package com.lingjoin.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.lingjoin.common.anonation.AdminAuthRequired;
import com.lingjoin.common.anonation.LoginRequired;
import com.lingjoin.common.config.MyConfig;
import com.lingjoin.common.util.Page;
import com.lingjoin.common.util.ReturnModel;
import com.lingjoin.file.entity.Doc;
import com.lingjoin.file.service.DocService;
import com.lingjoin.graph.entity.Tuple;
import com.lingjoin.graph.gentity.Entity;
import com.lingjoin.graph.gentity.Relationship;
import com.lingjoin.graph.repository.ERepository;
import com.lingjoin.graph.repository.RRepository;
import com.lingjoin.graph.service.TupleService;
import com.lingjoin.web.util.LoadUtil;
import com.lingjoin.web.util.NewRDTO;
import com.lingjoin.web.util.NodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/node", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = {"null", "http://172.16.1.129:8888","http://172.16.1.129:9999","http://172.16.1.129:8079", "http://172.16.1.184:8080", "http://172.16.1.184:8081"}, allowCredentials = "true")
public class NodeController {

    @Autowired
    private MyConfig myConfig;
    @Autowired
    private ERepository eRepository;
    @Autowired
    private RRepository rRepository;

    @Autowired
    private DocService docService;

    @Autowired
    private TupleService tupleService;

    public static class SourceDTO{

        private String mark;

        private String showName;

        public SourceDTO() {
        }

        public SourceDTO(String mark, String showName) {
            this.mark = mark;
            this.showName = showName;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }
    }



    private static class RelationDTO{
        private Long id;
        private String name;
        private String from;
        private String to;
        private String relation;
        private String src;
        private Date date;

        public RelationDTO() {
        }

        public Long getId() {

            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public RelationDTO(Long id, String name, String from, String to, String relation, String src, Date date) {
            this.id = id;
            this.name = name;
            this.from = from;
            this.to = to;
            this.relation = relation;
            this.src = src;
            this.date = date;
        }
    }

    /**
     * 查看某个节点的一级关系节点
     *
     * @param
     * @param model
     * @return
     */
    private String getNode(String name,
                           Model model,
                           Integer show,
                           Integer uid) {
        name = ".*" + name + ".*";

        List<Relationship> byname = rRepository.findByname(name,uid);
        //ArrayList<Entity> entities = new ArrayList<>();

        HashSet<Entity> entities = new HashSet<>();

        byname.forEach( r->{
            entities.add(r.getTo());
            entities.add(r.getFrom());
        });

        Entity[] array = entities.toArray(new Entity[0]);

        ArrayList<NodeDTO> nodeDTOS = LoadUtil.nodeTrans(Arrays.asList(array));

        //List<com.lingjoin.air.util.RelationDTO> transfer = LoadUtil.transfer(rs,show);
        ArrayList<NewRDTO> newRDTOS = new ArrayList<>();
        LoadUtil.load(byname,newRDTOS);
        model.addAttribute("lists", newRDTOS);
        model.addAttribute("data", nodeDTOS);
        model.addAttribute("status",1);
        String s = JSON.toJSONString(model, SerializerFeature.WriteNullStringAsEmpty);
        return s;
    }

    private String shortestPath(String name1, String name2, Model model,
                                Integer show,
                                Integer uid) {
        name1 = ".*" + name1 + ".*";
        name2 = ".*" + name2 + ".*";
        List<Relationship> rs = rRepository.shortestPath(name1,name2,uid);

        //ArrayList<Entity> entities = new ArrayList<>();

        HashSet<Entity> entities = new HashSet<>();

        rs.forEach( r->{
            entities.add(r.getTo());
            entities.add(r.getFrom());
        });

        Entity[] array = entities.toArray(new Entity[0]);

        ArrayList<NodeDTO> nodeDTOS = LoadUtil.nodeTrans(Arrays.asList(array));

        //List<com.lingjoin.air.util.RelationDTO> transfer = LoadUtil.transfer(rs,show);
        ArrayList<NewRDTO> newRDTOS = new ArrayList<>();
        LoadUtil.load(rs,newRDTOS);
        model.addAttribute("lists", newRDTOS);
        model.addAttribute("data", nodeDTOS);
        model.addAttribute("status",1);
        String s = JSON.toJSONString(model, SerializerFeature.WriteNullStringAsEmpty);
        return s;
    }

    @RequestMapping("/search")
    @LoginRequired
    public String search(@RequestParam("text") String text, Model model,
                         @RequestParam(required = false,defaultValue = "0") Integer show) {
//        User user =(User) request.getSession().getAttribute("user");
//        Integer uid=user.getId();
        Integer uid = 124;//todo

        System.out.println("search  show:"+show);
        if (StringUtils.isEmpty(text)) return "text参数为空";
        if (text.contains(",")) {
            String[] split = text.split(",", 2);
            return shortestPath(split[0], split[1], model,show,uid);
        }
        return getNode(text, model,show,uid);

    }
//    @RequestMapping("/sinrels")
//    @LoginRequired
//    public String batchrelations(
//            @RequestParam(value = "likeName", required = false) String likeName,
//            HttpServletRequest request,String uuid,@RequestParam(required = false,defaultValue = "")String time){
//
//        ReturnModel model = new ReturnModel( null, "获取列表成功", 0);
//        Integer uid = 124;//todo
//
//        Page page =null;
//        ArrayList<RelationDTO> relations = new ArrayList<>();
//        if(StringUtils.isEmpty(likeName)){
//            List<Relationship> relationships = rRepository.allRelwithUUID(uuid);
//            relationships.forEach(r->{
//                try {
//
//                    relations.add(
//                            new RelationDTO(
//                                    r.getId(),
//                                    r.getName(),
//                                    r.getFrom().getName(),
//                                    r.getTo().getName(),
//                                    r.getName(),
//                                    r.getUid()+"/"+r.getDate(),
//                                    new SimpleDateFormat("yyyyMMddHHmmss").parse(r.getDate())));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            });
//
//
//            page = new Page();
//            page.setPageList(relations);
//            model.setData(page);
//
//        }else {
//
//            List<Relationship> relationships = rRepository.allRelwithUUID(uuid);
//            relationships.forEach(r->{
//                try {
//                    if (r.getName().equals("send")){
//                        relations.add(
//                                new RelationDTO(
//                                        r.getId(),
//                                        "发送邮件"+r.getWeight()+"封",
//                                        r.getFrom().getName(),
//                                        r.getTo().getName(),
//                                        r.getName(),
//                                        r.getUid()+"/"+r.getDate(),
//                                        new SimpleDateFormat("yyyyMMddHHmmss").parse(r.getDate())));
//                        return;
//                    }
//                    relations.add(
//                            new RelationDTO(
//                                    r.getId(),
//                                    r.getName(),
//                                    r.getFrom().getName(),
//                                    r.getTo().getName(),
//                                    r.getName(),
//                                    r.getUid()+"/"+r.getDate(),
//                                    new SimpleDateFormat("yyyyMMddHHmmss").parse(r.getDate())));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            });
//            page = new Page();
//
//            page.setPageList(relations);
//            model.setData(page);
//
//        }
//
//        return JSON.toJSONStringWithDateFormat(model,"yyyy-MM-dd HH:mm:ss",SerializerFeature.WriteDateUseDateFormat);
//
//    }


    @RequestMapping("/sinrels")
    @LoginRequired
    public String relDetil(HttpServletRequest request,
                           String uuid,
                           @RequestParam(required = false)String time){
        ReturnModel model = new ReturnModel(null, "", 0);
        HashMap<String, List<Tuple>> map = new HashMap<>();
        List<Tuple> tuples = tupleService.tuples2SingleDoc(uuid);
        map.put("pageList",tuples);
        model.setData(map);
        return JSON.toJSONString(model);
    }
//    @RequestMapping("/allsingleRel")
//    @LoginRequired
//    public String singleRel(@RequestParam(required = false,defaultValue = "1") Integer show,
//                            HttpServletRequest request, Model model, String uuid,
//                            @RequestParam(required = false,defaultValue = "")String time) {
////        User user =(User) request.getSession().getAttribute("user");
////        Integer uid=user.getId();
//        Integer uid = 124;//todo
//
//        System.out.println("show:"+show);
//
//        List<Relationship> rs = rRepository.allRelwithUUID(uuid);
//        List<Entity> entities = eRepository.allNodeWithUUID(uuid);
//
//        ArrayList<NewRDTO> newRDTOS = new ArrayList<>();
//
//        ArrayList<NodeDTO> nodeDTOS = new ArrayList<>();
//        LoadUtil.load(rs,newRDTOS);
//        nodeDTOS = LoadUtil.nodeTrans(entities);
//        model.addAttribute("data", nodeDTOS);
//        //model.addAttribute("personList",nodeDTOS);
//
//        model.addAttribute("lists",newRDTOS);
//        model.addAttribute("status",1);
//        String s = JSON.toJSONString(model, SerializerFeature.WriteNullStringAsEmpty);
//        return s;
//    }
//

    @RequestMapping("/mainsinrels")
    @LoginRequired
    public String mainsinrels(HttpServletRequest request,String uuid,Model model,@RequestParam(required = false,defaultValue = "")String time){

        Integer uid = 1;//todo
        List<Relationship> mainuuidr = rRepository.sinMainRels(uid, uuid);
        HashSet<Entity> entities = new HashSet<>();

        System.out.println(mainuuidr);
        mainuuidr.forEach( r->{
            entities.add(r.getTo());
            entities.add(r.getFrom());
        });

        Entity[] array = entities.toArray(new Entity[0]);

        ArrayList<NodeDTO> nodeDTOS = LoadUtil.nodeTrans(Arrays.asList(array));

        ArrayList<NewRDTO> newRDTOS = new ArrayList<>();
        LoadUtil.load(mainuuidr,newRDTOS);

        System.out.println(nodeDTOS);
        System.out.println(newRDTOS);

        model.addAttribute("lists", newRDTOS);
        model.addAttribute("data", nodeDTOS);
        model.addAttribute("status",1);
        String s = JSON.toJSONString(model, SerializerFeature.WriteNullStringAsEmpty);
        return s;
    }



    //主关系
    @RequestMapping("/mainrels")
    @LoginRequired
    public String mainrels(@RequestParam(required = false,defaultValue = "") String uuid,
                       HttpServletRequest request, Model model,
                       @RequestParam(required = false,defaultValue = "")String time) {

        //测试用数据：

        Entity e1 = new Entity(null, "", "XX行动", 1);
        Entity e2 = new Entity(null, "", "中国", 0);
        Entity e3 = new Entity(null, "", "俄罗斯", 0);
        Entity e4 = new Entity(null, "", "日本", 0);
        Entity e5 = new Entity(null, "", "815/815A型电子侦察船", 1);

        ArrayList<Entity> testEntity = new ArrayList<>();
        testEntity.add(e1);
        testEntity.add(e2);
        testEntity.add(e3);
        testEntity.add(e4);
        testEntity.add(e5);

        Relationship r1 = new Relationship(null, "国家", 1, e1, e2);
        Relationship r2 = new Relationship(null, "国家", 1, e1, e3);
        Relationship r3 = new Relationship(null, "国家", 1, e1, e4);
        Relationship r4 = new Relationship(null, "国家", 1, e5, e2);
        ArrayList<Relationship> testRles = new ArrayList<>();
        testRles.add(r1);
        testRles.add(r2);
        testRles.add(r3);
        testRles.add(r4);
        Integer uid = 1;//todo


//        List<Relationship> rels = rRepository.mainRels(uid);
        List<Relationship> rels = testRles;
        System.out.println(rels);
//        List<Entity> entities=eRepository.mainEntity(uid);
        List<Entity> entities=testEntity;


        ArrayList<NewRDTO> newRDTOS = new ArrayList<>();
        LoadUtil.load(rels,newRDTOS);
        ArrayList<NodeDTO> nodeDTOS = LoadUtil.nodeTrans(entities);

        //System.out.println(nodeDTOS);
        //System.out.println(newRDTOS);


        model.addAttribute("data", nodeDTOS);
        //model.addAttribute("personList",nodeDTOS);

        model.addAttribute("lists",newRDTOS);
        model.addAttribute("status",1);
        String s = JSON.toJSONString(model, SerializerFeature.WriteNullStringAsEmpty);
        return s;
    }

    @RequestMapping("/allrels")
    @LoginRequired
    public String allrels(@RequestParam(required = false,defaultValue = "") String uuid,
                              HttpServletRequest request, Model model,
                              @RequestParam(required = false,defaultValue = "")String time) {

        Entity e1 = new Entity(null, "", "XX行动", 1);
        Entity e2 = new Entity(null, "", "中国", 0);
        Entity e3 = new Entity(null, "", "俄罗斯", 0);
        Entity e4 = new Entity(null, "", "日本", 0);
        Entity e5 = new Entity(null, "", "815/815A型电子侦察船", 1);
        Entity e6 = new Entity(null, "", "250人", 0);
        Entity e7 = new Entity(null, "", "17.4米", 0);
        Entity e8 = new Entity(null, "", "132.6米", 0);
        Entity e9 = new Entity(null, "", "沪东中华造船", 0);
        Entity e10 = new Entity(null, "", "20节", 0);


        ArrayList<Entity> testEntity = new ArrayList<>();
        testEntity.add(e1);
        testEntity.add(e2);
        testEntity.add(e3);
        testEntity.add(e4);
        testEntity.add(e5);
        testEntity.add(e6);
        testEntity.add(e7);
        testEntity.add(e8);
        testEntity.add(e9);
        testEntity.add(e10);

        Relationship r1 = new Relationship(null, "国家", 1, e1, e2);
        Relationship r2 = new Relationship(null, "国家", 1, e1, e3);
        Relationship r3 = new Relationship(null, "国家", 1, e1, e4);
        Relationship r4 = new Relationship(null, "国家", 1, e5, e2);
        Relationship r5 = new Relationship(null, "乘员", 1, e5, e6);
        Relationship r6 = new Relationship(null, "全宽", 1, e5, e7);
        Relationship r7 = new Relationship(null, "全长", 1, e5, e8);
        Relationship r8 = new Relationship(null, "制造厂", 1, e5, e9);
        Relationship r9 = new Relationship(null, "最高速度", 1, e5, e10);
        ArrayList<Relationship> testRles = new ArrayList<>();
        testRles.add(r1);
        testRles.add(r2);
        testRles.add(r3);
        testRles.add(r4);
        testRles.add(r5);
        testRles.add(r6);
        testRles.add(r7);
        testRles.add(r8);
        testRles.add(r9);



        Integer uid = 1;//todo


//        List<Relationship> rels = rRepository.mainRels(uid);
        List<Relationship> rels = testRles;
        System.out.println(rels);
//        List<Entity> entities=eRepository.mainEntity(uid);
        List<Entity> entities=testEntity;
        ArrayList<NewRDTO> newRDTOS = new ArrayList<>();
        LoadUtil.load(rels,newRDTOS);
        ArrayList<NodeDTO> nodeDTOS = LoadUtil.nodeTrans(entities);
        model.addAttribute("data", nodeDTOS);

        //model.addAttribute("personList",nodeDTOS);
        model.addAttribute("lists",newRDTOS);
        model.addAttribute("status",1);
        String s = JSON.toJSONString(model, SerializerFeature.WriteNullStringAsEmpty);
        return s;
    }

    //查询某个节点的相关信息：来源等（文章来源，数据库等）
    @RequestMapping("/nodeInfo")
    @LoginRequired
    @AdminAuthRequired
    public String nodeInfo(String nodeName,
                           @RequestParam(required = false) String page,
                           @RequestParam(required = false) String limit){
        ReturnModel model = new ReturnModel(null, "", 0);

        HashMap<String, Object> contentResult = new HashMap<>();



        ArrayList<SourceDTO> sourceDTOS = new ArrayList<>();
        List<Tuple> tuples = tupleService.selectNodeSource(nodeName);
        tuples.forEach(t->{
            if (t.getSource().contains(",")){
                String[] split = t.getSource().split(",");
                sourceDTOS.add(new SourceDTO(t.getSource(),"连接"+split[0]+"第"+split[1]+"条记录"));
            }else {
                Doc doc = docService.queryByUUID(t.getUid(), t.getSource());
                sourceDTOS.add(new SourceDTO(t.getSource(),doc.getSuffixName()));
            }
        });

        List<Tuple> relativeInfo = tupleService.relativeInfo(nodeName);

        Tuple tupleSrc = tupleService.nodeDataSrc(nodeName);

        contentResult.put("src",tupleSrc);
        contentResult.put("relativeInfo",relativeInfo);

        contentResult.put("contentList",sourceDTOS);




        model.setData(contentResult);
        return JSON.toJSONString(model, SerializerFeature.WriteNullStringAsEmpty);
    }

}
