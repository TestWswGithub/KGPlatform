package com.lingjoin.web.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.lingjoin.auth.entity.User;
import com.lingjoin.common.anonation.LoginRequired;
import com.lingjoin.common.config.MyConfig;
import com.lingjoin.common.util.Page;
import com.lingjoin.common.util.ReturnModel;
import com.lingjoin.file.entity.Doc;
import com.lingjoin.file.service.DocService;
import com.lingjoin.file.service.KeyValService;
import com.lingjoin.file.service.NewWordService;
import com.lingjoin.graph.service.TupleService;
import com.lingjoin.source.service.ConService;
import org.apache.commons.codec.binary.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;
import javax.xml.crypto.Data;
import java.io.*;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/file")
@CrossOrigin(origins = {"null", "http://172.16.1.129:8888", "http://172.16.1.129:9999", "http://172.16.1.129:8079", "http://172.16.1.184:8080", "http://172.16.1.184:8081"}, allowCredentials = "true")
public class FileController {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TupleService tupleService;

    @Autowired
    private ConService conService;

    @Resource
    private RedisTemplate<String, Entity> redisTemplate;
    @Autowired
    private NewWordService newWordService;
    @Autowired
    private KeyValService keyValService;

    private String content = "【文/观察者网 王世纯】\n" +
            "\n" +
            "美日澳三国在澳大利亚举行声势浩大的海上军演，但夺去军演“风头”的，则是我军一艘815A型电子侦察船。\n" +
            "\n" +
            "据澳大利亚广播公司（ABC）7日报道，美澳日“护身军刀-2019”联合军演当天在昆士兰州外海拉开序幕，此次军演声势浩大，美国海军出动印太司令部主力“里根”号航母赴昆士兰参演，而日本海上自卫队则排出“日向”级直升机航母“伊势”号和“大隅”级船坞运输舰“国东”号也赴澳参演。\n" +
            "\n" +
            "\n" +
            "\n" +
            "美国第七舰队“里根”号航母打击大队已于7月5日抵达昆士兰 图源：美国海军\n" +
            "\n" +
            "除了航母与两栖攻击舰以外，美澳还出动战机，装甲部队和两栖登陆部队进行了联演，参演人员达到2.5万人，是近年来比较大的联合军演。值得一提的是，日本自卫队去年才成立的“水陆机动团”也是首次参加军演。\n" +
            "\n" +
            "\n" +
            "\n" +
            "澳大利亚陆军和美国陆军试射高机动火箭系统(HIMARS) 图源：美国陆军\n" +
            "\n" +
            "ABC声称，今年的“护身军刀-2019”（Talisman Sabre 2019）演习将着眼于中强度的“高端”（High-end）作战规划与演练，演练将提高美日澳军的“互操作性”。\n" +
            "\n" +
            "“高端”演练（High-end）是美国在国防政策转为大国竞争时代以后的特有词语，意味针对中俄等大国高科技战争演练。“护身军刀-2019”演习内容包括海上作战、空中作战、特种作战、两栖登陆和后勤补给等。\n" +
            "\n" +
            "\n" +
            "\n" +
            "“高端演练” 图源：新西兰新闻\n" +
            "\n" +
            "美国着眼与“高端战争”，而日本则更加直言不讳。一位不愿意透露姓名的日本自卫队告诉“商业内幕”网记者，此次“水陆机动团”是首次参加军演，目的是针对中国在钓鱼岛可能发生的军事行动。\n" +
            "\n" +
            "既然是针对“大国竞争”的演习，也少不了来自大国的监视，据ABC 7月8日报道，一艘解放军815G型“间谍船”（原文为Type815G，应为我军815A型电子侦察舰），6日晚间出现在巴布亚新几内亚北部海域，现正南下航行，接近演习海域。\n" +
            "\n" +
            "\n" +
            "\n" +
            "资料图：2018年导弹巡洋舰安提坦和“天枢星”船并排行驶 图源：美国海军官网\n" +
            "\n" +
            "ABC报道称，这艘电子监视船配备有先进的通信和电子侦察系统，可以用于监视舰艇及其他军队。多名澳大利亚国防部官员告诉ABC，他们已经开始着手应对这艘电子侦察船。\n" +
            "\n" +
            "815型电子监视船是中国新一代大型海上电子侦察舰，从第二艘改进型开始被称为815A型。该型侦察船装有三个大型天线罩，可以进行电子信号情报的监听、收集、分析，具有良好的电磁兼容性和自动化程度，甚至可以监视卫星和弹道导弹。\n" +
            "\n" +
            "截止到2018年末，中国人民海军总共拥有9艘815/815A型电子侦察船。由于815/815A型电子侦察船都采用星宿名称来命名，因此有不少网友将该型侦察船称为是“星宿”级。\n" +
            "\n" +
            "\n" +
            "\n" +
            "资料图，853舰“天王星”号\n" +
            "\n" +
            "至于815A来的目的，澳大利亚人认为是日本人把电子侦察船引到昆士兰的。不愿透露姓名的澳大利亚军方官员对ABC新闻表示，澳军方推测，日本今年派遣水陆机动团以及两栖攻击舰参演一事，引起了中国的兴趣。\n" +
            "\n" +
            "\n" +
            "\n" +
            "参演的日本“日向”级直升机航母“伊势”号 图源：澳大利亚海军\n" +
            "\n" +
            "不过侦察船在公海航行，合理合法，美日澳联军对这艘电子侦察船也没什么办法。澳大利亚联合作战司令部指挥官比尔顿中将专门在开幕式说道：\n" +
            "\n" +
            "“目前有一艘电子侦察船在我们北边。”\n" +
            "\n" +
            "他表示：“这是一艘收集情报的船只，没有什么太大威胁，我们正在对其（815A电子侦察船）进行跟踪。我们尚不清楚该舰的目的地，但我们推定她会驶向昆士兰州东海岸，我们将对此采取适当措施。”\n" +
            "\n" +
            "\n" +
            "\n" +
            "澳大利亚联合作战司令部指挥官比尔顿中将在开幕上向记者们提及中国815A电子侦察船来澳一事 图源：ABC\n" +
            "\n" +
            "比尔顿没有透露采取措施的细节。不过这位中将承认，这艘中国军舰在有权在国际水域航行，如果她愿意，她也可以进行监视行动。\n" +
            "\n" +
            "悉尼美国研究中心外交政策和国防主任阿什利·汤森告诉ABC，根据《联合国海洋法公约》，所有国家都有权在一个国家12海里领海以外的国际水域进行军事监视行动。\n";

    private static String kgResult = "{\n" +
            "   \"entity_attribute\" : [\n" +
            "      {\n" +
            "         \"attribute\" : \"entity_name\",\n" +
            "         \"entity\" : \"澳大利亚\",\n" +
            "         \"val\" : \"country\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"datasrc\",\n" +
            "         \"entity\" : \"澳大利亚\",\n" +
            "         \"val\" : \"不过侦察船在公海航行，合理合法，美日澳联军对这艘电子侦察船也没什么办法。澳大利亚联合作战司令部指挥官比尔顿中将专门在开幕式说道：\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"country|国家\",\n" +
            "         \"entity\" : \"澳大利亚\",\n" +
            "         \"val\" : \"澳大利亚\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"zhihuiguan|指挥官\",\n" +
            "         \"entity\" : \"澳大利亚\",\n" +
            "         \"val\" : \"比尔顿中将\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"show_name\",\n" +
            "         \"entity\" : \"澳大利亚\",\n" +
            "         \"val\" : \"澳大利亚\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"entity_name\",\n" +
            "         \"entity\" : \"澳大利亚\",\n" +
            "         \"val\" : \"country\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"datasrc\",\n" +
            "         \"entity\" : \"澳大利亚\",\n" +
            "         \"val\" : \"澳大利亚联合作战司令部指挥官比尔顿中将在开幕上向记者们提及中国815A电子侦察船来澳一事 图源：ABC\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"country|国家\",\n" +
            "         \"entity\" : \"澳大利亚\",\n" +
            "         \"val\" : \"澳大利亚\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"zhihuiguan|指挥官\",\n" +
            "         \"entity\" : \"澳大利亚\",\n" +
            "         \"val\" : \"比尔顿中将\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"show_name\",\n" +
            "         \"entity\" : \"澳大利亚\",\n" +
            "         \"val\" : \"澳大利亚\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"entity_name\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"s_Cname\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"datasrc\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"据澳大利亚广播公司（ABC）7日报道，美澳日“护身军刀-2019”联合军演当天在昆士兰州外海拉开序幕，此次军演声势浩大，美国海军出动印太司令部主力“里根”号航母赴昆士兰参演，而日本海上自卫队则排出“日向”级直升机航母“伊势”号和“大隅”级船坞运输舰“国东”号也赴澳参演。\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"s_Cname|代号\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"“护身军刀- 2019”\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"country|国家\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"澳大利亚\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"show_name\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"“护身军刀- 2019”\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"entity_name\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"s_Cname\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"datasrc\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"据澳大利亚广播公司（ABC）7日报道，美澳日“护身军刀-2019”联合军演当天在昆士兰州外海拉开序幕，此次军演声势浩大，美国海军出动印太司令部主力“里根”号航母赴昆士兰参演，而日本海上自卫队则排出“日向”级直升机航母“伊势”号和“大隅”级船坞运输舰“国东”号也赴澳参演。\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"s_Cname|代号\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"“护身军刀- 2019”\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"country|国家\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"美国\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"show_name\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"“护身军刀- 2019”\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"entity_name\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"s_Cname\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"datasrc\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"据澳大利亚广播公司（ABC）7日报道，美澳日“护身军刀-2019”联合军演当天在昆士兰州外海拉开序幕，此次军演声势浩大，美国海军出动印太司令部主力“里根”号航母赴昆士兰参演，而日本海上自卫队则排出“日向”级直升机航母“伊势”号和“大隅”级船坞运输舰“国东”号也赴澳参演。\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"s_Cname|代号\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"“护身军刀- 2019”\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"country|国家\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"日本\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"show_name\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"“护身军刀- 2019”\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"entity_name\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"s_Cname\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"datasrc\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"据澳大利亚广播公司（ABC）7日报道，美澳日“护身军刀-2019”联合军演当天在昆士兰州外海拉开序幕，此次军演声势浩大，美国海军出动印太司令部主力“里根”号航母赴昆士兰参演，而日本海上自卫队则排出“日向”级直升机航母“伊势”号和“大隅”级船坞运输舰“国东”号也赴澳参演。\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"s_Cname|代号\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"“护身军刀- 2019”\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"country|国家\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"美国\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"attribute\" : \"show_name\",\n" +
            "         \"entity\" : \"“护身军刀- 2019”\",\n" +
            "         \"val\" : \"“护身军刀- 2019”\"\n" +
            "      }\n" +
            "   ],\n" +
            "   \"relation\" : null\n" +
            "}\n";

    @Autowired
    private DocService docService;


    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
            15,
            60, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(50));

    @Autowired
    private MyConfig myConfig;
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);


    @RequestMapping(value = "/kgbfile", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @LoginRequired
    public String kgbFile(HttpServletRequest request, MultipartFile file) {

        ReturnModel model = new ReturnModel(null, "", 0);
        Date date = new Date();
        String suffixName = file.getOriginalFilename();
        logger.info("suffixName:" + suffixName);

        String name = suffixName.substring(0, suffixName.lastIndexOf("."));
        logger.info("name:" + name);

//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();

        Integer uid = 1;

        //uuid调用接口获得。
        String uuid = UUID.randomUUID().toString();
        logger.info("为文章分配UUID====>" + uuid);

        //此过程调用耀飞docker容器，目前仅仅是测试模拟数据
        //kgb抽取结果，并入库


        //获取抽取结果之后,在mysql对应的三元组表中添加相应的三元组信息

        tupleService.batchInsert(kgResult, uid, uuid);

        //es索引,这一步是耀飞容器做的


        //将文件元信息存储在数据库中

        Doc doc = new Doc();
        doc.setDelete(false);
        doc.setUuid(uuid);
        doc.setUid(uid);
        doc.setName(name);
        doc.setSuffixName(suffixName);
        doc.setUploadTime(date);
        doc.setUserName("邹伟健");
        docService.save(doc);


        return JSON.toJSONString(model);

    }


    @RequestMapping(value = "/text", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getText(HttpServletRequest request,
                          @RequestParam(value = "conId", required = false) Integer conId,
                          @RequestParam(value = "rowId", required = false) Integer rowId,
                          @RequestParam(value = "uuid", required = false) String uuid) {


        ReturnModel model = new ReturnModel(null, "", 0);

        System.out.println("uuid=========>"+uuid);

        //如果uuid不为null  则是查看文件内容  从es中根据uuid取出内容
        if (uuid != null&&!"".equals(uuid)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("title", "文章标题");
            map.put("content", content);

            model.setData(map);
            return JSON.toJSONString(model);
        }

        //如果UUID为null则从对应的数据库中拿到对应的那一行。

        String cropus = conService.getCropus(conId, rowId);
        if (cropus == null) {
            model.setStatus(1);
            model.setMessage("没有权限查询");
            return JSON.toJSONString(model);
        }


        HashMap<String, String> map = new HashMap<>();
        map.put("title", "");
        map.put("content", cropus);
        model.setData(map);
        return JSON.toJSONString(model);

    }


//    @RequestMapping(value = "/file_text", produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public String getFileText(HttpServletRequest request,
//                          @RequestParam(value = "uuid", required = false) String uuid) {
//
//
//        ReturnModel model = new ReturnModel(null, "", 0);
//
//        System.out.println("uuid=========>"+uuid);
//
//        //如果uuid不为null  则是查看文件内容  从es中根据uuid取出内容
//
//            HashMap<String, String> map = new HashMap<>();
//            map.put("title", "文章标题");
//            map.put("content", content);
//
//            model.setData(map);
//            return JSON.toJSONString(model);
//
//
//    }


    @RequestMapping(value = "/uploadFolder", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @LoginRequired
    //@Async//这是一个异步方法。
    public String fileUpload(@RequestParam("file") MultipartFile[] files,
                             @RequestParam(required = false, defaultValue = "测试类型") String type,
                             @RequestParam(required = false, defaultValue = "admin") String userName,
                             HttpServletRequest request) {

        int fileCount = files.length;
        logger.info("上传的文件数量：" + fileCount);
        ReturnModel model = new ReturnModel(null, null, 0);
        long t1 = System.currentTimeMillis();
        if (files == null || files.length == 0) {
            model.setStatus(1);
            model.setMessage("上传的文件为空");
            return JSON.toJSONString(model);
        }

        String token = request.getHeader("Authorization");
        String[] split = token.split("\\.");
        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
        Integer uid = user.getId();


        ArrayList<Doc> documents = new ArrayList<>();


        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String s = format.format(date);
        String basePath = "";
        logger.info("上传的时间戳：" + s);


        for (MultipartFile file : files) {


            //uuid调用接口获得。
            String uuid = UUID.randomUUID().toString();
            logger.info("为文章分配UUID====>" + uuid);

            //basePath = myConfig.getUploadpath() + "/" + uid + "/" + s + "/" + uuid;
            //FileUtil.mkDir(new File(basePath));

            String suffixName = file.getOriginalFilename();
            logger.info("suffixName:" + suffixName);

            String name = suffixName.substring(0, suffixName.lastIndexOf("."));
            logger.info("name:" + name);


            String suffix = suffixName.substring(suffixName.lastIndexOf("."));
            String uuidFileName = uuid + suffix;
            String filePath = basePath + "/" + uuidFileName;

            logger.info("文件原名为：" + suffixName);
            logger.info("文件上传路径为：" + filePath);

            File dest = new File(filePath);


            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
                model.setStatus(1);
                model.setMessage("上传文件出现异常,请检查服务器");
                return JSON.toJSONString(model);
            }


            logger.info("读取文件中的内容：" + filePath);
            //String text = FileUtil.readFile(new File(filePath));


            Doc document = new Doc();
            document.setType(type);
            document.setUserName(userName);
            //document.setText(text);
            document.setDelete(false);
            document.setName(name);
            document.setSuffixName(suffixName);
            document.setPath(filePath);
            document.setUploadTime(date);
            document.setUuid(uuid);
            document.setUid(uid);
            document.setTimeStamp(s);
            //threadPoolExecutor.execute(new NewWordThread(newWordService, myConfig, uid, uuid, suffixName, s, text));
            //document.setText(text);
            documents.add(document);


        }


        try {
            if (documents.size() != 0) docService.batchSave(documents);
        } catch (Exception e) {
            e.printStackTrace();
            model.setStatus(1);
            model.setMessage("插入mysql时发生异常");
            return JSON.toJSONString(model);
        }


        model.setStatus(0);
        model.setMessage("解析成功");
        model.setData(s);
        long t2 = System.currentTimeMillis();

        System.out.println("responseTime:" + (t2 - t1));
        return JSON.toJSONString(model);
    }

    @RequestMapping("/download")
    @LoginRequired
    public String download(HttpServletRequest request,
                           HttpServletResponse response,
                           String uuid) {

        logger.info("入参UUID为：" + uuid);


        Doc doc = docService.queryByUUID(124, uuid);


        File ofile = new File(doc.getPath());
        if (!ofile.exists()) return "要下载的文件不存在";

        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        try {

            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(doc.getSuffixName(), "UTF-8"));
            // 实现文件下载
            fis = new FileInputStream(ofile);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }

            logger.info("下载成功：" + uuid);


            return null;


        } catch (Exception e) {
            logger.info("下载失败：" + uuid);
            return null;

        } finally {

            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @RequestMapping(value = "/doc_list", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @LoginRequired
    public String getDocList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "createDate", required = false) String createDate,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "documentName", required = false) String documentName,
                             HttpServletRequest request) {

//        User user =(User) request.getSession().getAttribute("user");
//        Integer uid=user.getId();
        Integer uid = 1;//todo
        ReturnModel model = new ReturnModel(null, "", 0);
        logger.info("获取文章列表：起始页：" + pageNum);
        logger.info("获取文章列表：每页显示" + pageSize + "条");
        logger.info("模糊查询(文章名)：" + documentName);


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


        Page page = new Page(docService.totalCount(pageNum - 1, pageSize, start, end, status, uid, documentName), pageSize);
        List<Doc> docs = docService.listDocPage((pageNum - 1) * pageSize, pageSize, start, end, status, uid, documentName);
        page.setPageNo(pageNum);
        page.setPageList(docs);
        model.setData(page);
        model.setMessage("获取列表成功");
        model.setStatus(0);
        return JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);


    }


    @RequestMapping(value = "/sinDetil", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @LoginRequired
    public String sinDetil(String uuid) {

        logger.info("入参UUID为：" + uuid);

        ReturnModel model = new ReturnModel(null, "", 0);

        //        User user =(User) request.getSession().getAttribute("user");
//        Integer uid=user.getId();
        Integer uid = 1;//todo
        Doc doc = docService.queryByUUID(uid, uuid);

        model.setData(doc);
        model.setMessage("查询成功");
        model.setStatus(0);
        return JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
    }


}
