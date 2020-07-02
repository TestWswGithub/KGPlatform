package com.lingjoin.web.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.lingjoin.common.anonation.LoginRequired;
import com.lingjoin.common.util.Page;
import com.lingjoin.common.util.ReturnModel;
import com.lingjoin.file.entity.NewWord;
import com.lingjoin.file.service.NewWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/newWord")
@CrossOrigin(origins = {"null", "http://172.16.1.129:8888","http://172.16.1.129:9999","http://172.16.1.129:8079", "http://172.16.1.184:8080", "http://172.16.1.184:8081"}, allowCredentials = "true")
public class NewWordController {

    @Autowired
    private NewWordService newWordService;


    @RequestMapping(value = "/nwList",produces = "application/json;charset=UTF-8")
    @LoginRequired
    public String nwList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "createDate", required = false) String createDate,
                            @RequestParam(value = "likeWord", required = false) String likeWord,
                            String time,
                            HttpServletRequest request){

        //        User user =(User) request.getSession().getAttribute("user");
//        Integer uid=user.getId();
        Integer uid = 124;//todo
        ReturnModel model = new ReturnModel( null, "", 0);

        System.out.println("pagenum:" + pageNum);


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
            } catch (ParseException e) {
//                e.printStackTrace();
                e.printStackTrace();
                model.setMessage("日期格式错误");
                return JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat,SerializerFeature.WriteNullNumberAsZero);
            }
        }



        Page page=new Page(newWordService.totalCount(start,end,uid,time,likeWord),pageSize);
        List<NewWord> nws = newWordService.listNewWordPage((pageNum - 1) * pageSize, pageSize, start, end, uid,time, likeWord);
        page.setPageNo(pageNum);
        page.setPageList(nws);
        model.setData(page);
        model.setMessage("获取列表成功");
        model.setStatus(0);
        return JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);

    }



    //测试httpClient的接口
    //在后台不能由一个类全部接受的时候，就用application/x-www-form-urlencoded。


    public static class ParamContainer{

        private String username;
        private String method;
        private String code;

        @Override
        public String toString() {
            return "ParamContainer{" +
                    "username='" + username + '\'' +
                    ", method='" + method + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }

        public ParamContainer() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    @RequestMapping(value = "/array",produces = "application/json;charset=UTF-8")
    public String testArray(
                            String username,
                            String method,
                            String code,
                            @RequestBody ParamContainer param,
                            HttpServletRequest request) throws IOException {


        System.out.println(request.getContentType());
        System.out.println(username);
        System.out.println(method);
        System.out.println(code);

        System.out.println(param);



        return "ok";

    }


}
