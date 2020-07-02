package com.lingjoin.web.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lingjoin.auth.entity.User;
import com.lingjoin.auth.service.UserService;
import com.lingjoin.common.anonation.AdminAuthRequired;
import com.lingjoin.common.anonation.LoginRequired;
import com.lingjoin.common.myenum.ConType;
import com.lingjoin.common.util.Page;
import com.lingjoin.common.util.ReturnModel;
import com.lingjoin.file.entity.Doc;
import com.lingjoin.graph.service.TupleService;
import com.lingjoin.source.entity.Connection;
import com.lingjoin.source.entity.UsersConn;
import com.lingjoin.source.service.ConService;
import com.lingjoin.source.service.UsersConnService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
@RequestMapping(value = "/con", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = {"null", "http://172.16.1.184:8080"}, allowCredentials = "true")

public class SourceController {


    private static final Logger logger = LoggerFactory.getLogger(SourceController.class);

    @Autowired
    private ConService conService;
    @Autowired
    private UsersConnService usersConnService;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TupleService tupleService;


    //展示用户拥有的所有语料源连接
    @RequestMapping("/users_corpus_conns")
    @LoginRequired
    public String usersCorpusConns(HttpServletRequest request) {
        ReturnModel model = new ReturnModel(null, null, 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        Integer uid = 1;
        List<Connection> connections = conService.selectUsersAllCorpusConns(uid);
        model.setData(connections);
        return JSON.toJSONString(model);
    }

    //对接用户语料源列表(即数据库连接中所存在的内容)
    @RequestMapping("/corpusList")
    public String corpusList(HttpServletRequest request,
                             @RequestParam(value = "id") Integer id,
                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        ReturnModel model = new ReturnModel(null, null, 0);
        Page page = conService.listCorpusEntries(id, pageNum, pageSize);
        model.setData(page);
        System.out.println("===>page"+page);
        return JSON.toJSONString(model);
    }
    //对接用户所有知识源连接列表
    @RequestMapping("/users_knowledge_conns")
    @LoginRequired
    public String usersKnowledgeConns(HttpServletRequest request) {
        ReturnModel model = new ReturnModel(null, null, 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        Integer uid = 1;
        List<Connection> connections = conService.selectUsersAllKnowledgeConns(uid);
        model.setData(connections);
        return JSON.toJSONString(model);
    }

    //对接用户语料源列表(即数据库连接中所存在的内容)
    @RequestMapping("/knowledgeList")
    public String knowledgeList(HttpServletRequest request,
                             @RequestParam(value = "id") Integer id,
                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        ReturnModel model = new ReturnModel(null, null, 0);
        Page page = conService.listKnowledgeEntries(id, pageNum, pageSize);
        model.setData(page);
        return JSON.toJSONString(model);
    }



    //展示用户所拥有的连接            1级有所有连接       2级拥有1级下发给自己的。
    @RequestMapping("/users_conns_junshi")
    @LoginRequired
    public String usersConnListJunshi(HttpServletRequest request,
                                      @RequestParam(value = "type", required = false, defaultValue = "junshi") String type,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "createDate", required = false) String createDate) {
        ReturnModel model = new ReturnModel(null, null, 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        Integer uid = 1;


        logger.info("获取链接列表：起始页：" + pageNum);
        logger.info("获取连接列表：每页显示" + pageSize + "条");


        Date start = null;
        Date end = null;

        if (!StringUtils.isEmpty(createDate)) {
            String[] split = createDate.split("-");
            String startStr = split[0].trim();
            String endStr = split[1].trim();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                start = sdf.parse(startStr);
                //为结束时间加一天
                end = sdf.parse(endStr);
                Calendar c = Calendar.getInstance();
                c.setTime(end);
                c.add(Calendar.DAY_OF_MONTH, 1);
                end = c.getTime();

                logger.info("过滤上传起始时间" + start);
                logger.info("过滤上传终止时间" + end);

            } catch (ParseException e) {
                e.printStackTrace();
                model.setMessage("日期格式错误");
                return JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteNullNumberAsZero);
            }
        }
        Page page = new Page(conService.usersConnsTotalCount(start, end, uid, type), pageSize);
        //Page page = new Page(docService.totalCount(pageNum - 1, pageSize, start, end, uid, documentName), pageSize);
        List<Connection> cns = conService.selectUsersConns((pageNum - 1) * pageSize, pageSize, start, end, uid, type);
        page.setPageNo(pageNum);
        page.setPageList(cns);
        model.setData(page);
        model.setMessage("获取列表成功");
        model.setStatus(0);
        return JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);

    }

    //展示用户所拥有的连接            1级有所有连接       2级拥有1级下发给自己的。
    @RequestMapping("/users_conns_financial")
    @LoginRequired
    public String usersConnListFinancial(HttpServletRequest request,
                                         @RequestParam(value = "type", required = false, defaultValue = "financial") String type,
                                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "createDate", required = false) String createDate) {
        ReturnModel model = new ReturnModel(null, null, 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        Integer uid = 1;


        logger.info("获取链接列表：起始页：" + pageNum);
        logger.info("获取连接列表：每页显示" + pageSize + "条");


        Date start = null;
        Date end = null;

        if (!StringUtils.isEmpty(createDate)) {
            String[] split = createDate.split("-");
            String startStr = split[0].trim();
            String endStr = split[1].trim();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                start = sdf.parse(startStr);
                //为结束时间加一天
                end = sdf.parse(endStr);
                Calendar c = Calendar.getInstance();
                c.setTime(end);
                c.add(Calendar.DAY_OF_MONTH, 1);
                end = c.getTime();

                logger.info("过滤上传起始时间" + start);
                logger.info("过滤上传终止时间" + end);

            } catch (ParseException e) {
                e.printStackTrace();
                model.setMessage("日期格式错误");
                return JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteNullNumberAsZero);
            }
        }
        Page page = new Page(conService.usersConnsTotalCount(start, end, uid, type), pageSize);
        //Page page = new Page(docService.totalCount(pageNum - 1, pageSize, start, end, uid, documentName), pageSize);
        List<Connection> cns = conService.selectUsersConns((pageNum - 1) * pageSize, pageSize, start, end, uid, type);
        page.setPageNo(pageNum);
        page.setPageList(cns);
        model.setData(page);
        model.setMessage("获取列表成功");
        model.setStatus(0);
        return JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);

    }

    //展示用户所拥有的连接            1级有所有连接       2级拥有1级下发给自己的。
    @RequestMapping("/users_conns_economic")
    @LoginRequired
    public String usersConnListEconomic(HttpServletRequest request,
                                        @RequestParam(value = "type", required = false, defaultValue = "economic") String type,

                                        @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                        @RequestParam(value = "createDate", required = false) String createDate) {
        ReturnModel model = new ReturnModel(null, null, 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        Integer uid = 1;


        logger.info("获取链接列表：起始页：" + pageNum);
        logger.info("获取连接列表：每页显示" + pageSize + "条");


        Date start = null;
        Date end = null;

        if (!StringUtils.isEmpty(createDate)) {
            String[] split = createDate.split("-");
            String startStr = split[0].trim();
            String endStr = split[1].trim();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                start = sdf.parse(startStr);
                //为结束时间加一天
                end = sdf.parse(endStr);
                Calendar c = Calendar.getInstance();
                c.setTime(end);
                c.add(Calendar.DAY_OF_MONTH, 1);
                end = c.getTime();

                logger.info("过滤上传起始时间" + start);
                logger.info("过滤上传终止时间" + end);

            } catch (ParseException e) {
                e.printStackTrace();
                model.setMessage("日期格式错误");
                return JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteNullNumberAsZero);
            }
        }
        Page page = new Page(conService.usersConnsTotalCount(start, end, uid, type), pageSize);
        //Page page = new Page(docService.totalCount(pageNum - 1, pageSize, start, end, uid, documentName), pageSize);
        List<Connection> cns = conService.selectUsersConns((pageNum - 1) * pageSize, pageSize, start, end, uid, type);
        page.setPageNo(pageNum);
        page.setPageList(cns);
        model.setData(page);
        model.setMessage("获取列表成功");
        model.setStatus(0);
        return JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);

    }

    //展示用户直接管理的下级
    @RequestMapping("/sub_users")
    @LoginRequired
    public String userList(HttpServletRequest request) {

        ReturnModel model = new ReturnModel(null, null, 0);
        String token = request.getHeader("Authorization");
        String[] split = token.split("\\.");
        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
        Integer userTypeInt = user.getUserTypeInt();
        List<User> users = userService.listManagedUsers(user.getId());
        model.setData(users);
        return JSON.toJSONString(model);
    }

    //测试链接是否可用接口
    @RequestMapping("/test_Con")
    @LoginRequired
    @AdminAuthRequired
    public String testCon(String connType,
                          String sourceType,
                          String databaseType,
                          String ip,
                          String port,
                          @RequestParam(defaultValue = "mysql") String database,
                          String username,
                          String password,
                          String table,
                          @RequestParam(required = false, defaultValue = "") String field,
                          String markType,
                          String markField) {

        logger.info("databaseType:" + databaseType);
        logger.info("connType:" + connType);
        logger.info("sourceType:" + sourceType);
        logger.info("ip:" + ip);
        logger.info("port:" + port);
        logger.info("database:" + database);
        logger.info("username:" + username);
        logger.info("password:" + password);
        logger.info("table:" + table);
        logger.info("field:" + field);
        logger.info("markType:" + markType);
        logger.info("markField:" + markField);


        ReturnModel model = new ReturnModel(null, "连接数据库成功", 0);
        boolean able = false;
        HashMap<String, Object> map = new HashMap<>();
        map.put("databaseType", databaseType);
        map.put("sourceType", sourceType);
        map.put("ip", ip);
        map.put("port", port);
        map.put("database", database);
        map.put("username", username);
        map.put("password", password);
        map.put("table", table);
        map.put("field", field);
        map.put("markType", markType);
        map.put("markField", markField);

        ConType conType = ConType.toConType(databaseType);
        switch (conType) {
            case MYSQL:
                able = conService.testMysqlCon(ip, port, database, username, password);
                break;
            case SOLR:
                //todo  solrCon
                break;
            case HBASE:
                //todo hbase
                break;
            default:
                able = false;

        }

        map.put("able", able);
        model.setData(map);
        if (!able) model.setMessage("链接数据库失败");
        return JSON.toJSONString(model);
    }


    //保存连接
    @RequestMapping("/save_Con")
    @LoginRequired
    @AdminAuthRequired
    public String saveCon(String connType,
                          String sourceType,
                          String databaseType,
                          String ip,
                          Integer port,
                          String database,
                          String username,
                          String password,
                          String table,
                          @RequestParam(required = false, defaultValue = "") String field,
                          String markType,
                          String markField,
                          @RequestParam(required = false, defaultValue = "") String head,
                          @RequestParam(required = false, defaultValue = "") String relation,
                          @RequestParam(required = false, defaultValue = "") String tail,
                          HttpServletRequest request) {


        String token = request.getHeader("Authorization");
        String[] split = token.split("\\.");
        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
        Integer uid = user.getId();

        logger.info("databaseType:" + databaseType);
        logger.info("connType:" + connType);
        logger.info("sourceType:" + sourceType);
        logger.info("ip:" + ip);
        logger.info("port:" + port);
        logger.info("database:" + database);
        logger.info("username:" + username);
        logger.info("password:" + password);
        logger.info("table:" + table);
        logger.info("field:" + field);
        logger.info("markType:" + markType);
        logger.info("markField:" + markField);
        logger.info("head:" + head);
        logger.info("relation:" + relation);
        logger.info("tail:" + tail);


        ReturnModel model = new ReturnModel(null, "", 0);

        String conn = null;
        Connection connection = new Connection();
        ConType conType = ConType.toConType(databaseType);
        switch (conType) {
            case MYSQL:
                conn = toMysql8url(ip, port, database);
                connection.setDriver("com.mysql.cj.jdbc.Driver");
                break;
            case SOLR:
                //todo  solrCon
                break;
            case HBASE:
                //todo hbase
                break;
            default:

                model.setStatus(1);
                model.setMessage("无此链接类型");
                return JSON.toJSONString(model);
        }

        if (conService.exist(database, table, field, conn)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("databaseType", databaseType);
            map.put("connType", connType);
            map.put("sourceType", sourceType);
            map.put("ip", ip);
            map.put("port", port);
            map.put("database", database);
            map.put("username", username);
            map.put("password", password);
            map.put("table", table);
            map.put("field", field);
            map.put("markType", markType);
            map.put("markField", markField);
            map.put("head", head);
            map.put("relation", relation);
            map.put("tail", tail);
            model.setMessage("连接已经存在,请勿重复建立");
            model.setData(map);
            model.setStatus(1);
            return JSON.toJSONString(model);

        }


        connection.setConn(conn);
        connection.setCreateDate(new Date());
        connection.setHost(ip);
        connection.setPort(port);
        connection.setDatabase(database);
        connection.setField(field);
        connection.setMarkType(markType);
        connection.setMarkField(markField);
        connection.setUser(username);
        connection.setPassword(password);
        connection.setTable(table);
        connection.setDatabaseType(databaseType);
        connection.setConnType(connType);
        connection.setSourceType(sourceType);
        connection.setHeadField(head);
        connection.setRelField(relation);
        connection.setTailField(tail);


        conService.saveConn(connection);
        Integer conID = connection.getId();


        //同时将本链接添加到管理员自己的链接库中。
        usersConnService.save(new UsersConn(null, conID, uid));

        //同时设置标记字段的初始值。整形默认为1，日期默认为1970年 todo


        model.setMessage("保存链接成功");
        return JSON.toJSONString(model);
    }


    //数据源分发接口
    @RequestMapping("/distrib_Conn")
    @LoginRequired
    @AdminAuthRequired
    public String distributeConn(Integer conId, String uids) {

        logger.info("conID:" + conId);
        logger.info("uids:" + uids);


        ReturnModel model = new ReturnModel(null, "", 0);

        ArrayList<UsersConn> usersConns = new ArrayList<>();
        String[] split = uids.split(",");
        for (String s : split) usersConns.add(new UsersConn(null, conId, Integer.parseInt(s)));

        usersConnService.batchSave(usersConns);

        model.setMessage("连接分发成功");

        return JSON.toJSONString(model);
    }


    //外部数据库表导入知识
    @RequestMapping("/outer_kw_import")
    public String outerKnowledgeImport(@RequestParam(required = false) Integer conId,
                                       @RequestParam(required = false) Integer startIndex) {


        return "";


    }


    //自带知识库知识导入
    @RequestMapping("/inner_kw_import")
    public String innerKnowledgeImport(@RequestParam(required = false, defaultValue = "-1") Integer startIndex) {
        ReturnModel model = new ReturnModel(null, "", 0);
        String inRecored = stringRedisTemplate.opsForValue().get("kgplatform_inner_kw_recored");

        if (startIndex == -1 && inRecored == null) startIndex = 0;
        else if (startIndex == -1) startIndex = Integer.parseInt(inRecored);


        //逻辑判断 如果用户输入了大于redis中记录的起始位置  则直接返回  否则记录数不一致导致数据不一致！
        if (startIndex > Integer.parseInt(inRecored)) {
            model.setMessage("起始位置不合法，请输入小于默认值的整数");
            model.setStatus(1);
            return JSON.toJSONString(model);
        }

        tupleService.generateGraphfromKGB(startIndex);
        model.setMessage("从第" + startIndex + "条记录导入");
        return JSON.toJSONString(model);
    }

    //自带知识库现存记录数
    @RequestMapping("/inner_kw_total_count")
    public String innerKWcount() {
        return tupleService.totalEntries().toString();
    }


    //自带知识库已经生成图谱的记录数
    @RequestMapping("/inner_kw_recored")
    public String innerRecored() {
        String s = stringRedisTemplate.opsForValue().get("kgplatform_inner_kw_recored");
        if (s == null) return "0";
        return String.valueOf(Integer.parseInt(s) + 1);
    }


    private String toMysql8url(String ip, Integer port, String database) {

        return "jdbc:mysql://" + ip + ":" + port + "/" + database + "?serverTimezone=GMT%2B8&characterEncoding=utf8&useSSL=false";

    }


}
