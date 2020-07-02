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
import org.apache.commons.codec.binary.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
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
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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



    @Resource
    private RedisTemplate<String, Entity> redisTemplate;
    @Autowired
    private NewWordService newWordService;
    @Autowired
    private KeyValService keyValService;


    @Autowired
    private DocService docService;


    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
            15,
            60, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(50));

    @Autowired
    private MyConfig myConfig;
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);




    @RequestMapping(value = "/uploadFolder", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @LoginRequired
    //@Async//这是一个异步方法。
    public String fileUpload(@RequestParam("file") MultipartFile[] files,
                                  @RequestParam(required = false, defaultValue = "测试类型") String type,
                                  @RequestParam(required = false, defaultValue = "admin") String userName,
                                  HttpServletRequest request)  {

        int fileCount = files.length;
        logger.info("上传的文件数量："+fileCount);
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
        logger.info("上传的时间戳："+s);


        for (MultipartFile file : files) {




            //uuid调用接口获得。
            String uuid = UUID.randomUUID().toString();
            logger.info("为文章分配UUID====>"+uuid);

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







            logger.info("读取文件中的内容："+filePath);
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

        logger.info("入参UUID为："+uuid);


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

            logger.info("下载成功："+uuid);


            return null;


        } catch (Exception e) {
            logger.info("下载失败："+uuid);
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
        logger.info("获取文章列表：起始页："+pageNum);
        logger.info("获取文章列表：每页显示"+pageSize+"条");
        logger.info("模糊查询(文章名)："+documentName);



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


                logger.info("过滤上传起始时间"+start);

                logger.info("过滤上传终止时间"+end);



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

        logger.info("入参UUID为："+uuid);

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
