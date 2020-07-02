package com.lingjoin.web.controller;

import com.alibaba.fastjson.JSON;

import com.lingjoin.auth.entity.User;
import com.lingjoin.common.anonation.LoginRequired;
import com.lingjoin.common.util.Page;
import com.lingjoin.common.util.ReturnModel;
import com.lingjoin.file.entity.KeyVal;
import com.lingjoin.file.service.KeyValService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/keyval")
@CrossOrigin(origins = {"null", "http://172.16.1.129:8888","http://172.16.1.129:9999","http://172.16.1.129:8079", "http://172.16.1.184:8080", "http://172.16.1.184:8081"}, allowCredentials = "true")

public class KeyValController {


    private static class KeyValDTO {


        private String type;

        public List<KeyVal> getKvs() {
            return kvs;
        }

        public void setKvs(List<KeyVal> kvs) {
            this.kvs = kvs;
        }

        private List<KeyVal> kvs;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


        public KeyValDTO() {

        }
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private KeyValService keyValService;


    @RequestMapping(value = "/kvList", produces = "application/json;charset=UTF-8")
    @LoginRequired
    public String kvList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                         @RequestParam(value = "likeName", required = false) String likeName,
                         String uuid,
                         HttpServletRequest request) {


        Integer uid = 124;//todo
        ReturnModel model = new ReturnModel(null, "", 0);

        System.out.println("pagenum:" + pageNum);



        List<KeyVal> kvs = keyValService.keyValPage(pageNum - 1, pageSize, uid, uuid, null);
        kvs.forEach(System.out::println);


        ArrayList<KeyValDTO> collect = kvs.stream()
                .collect(Collectors.collectingAndThen(Collectors.groupingBy(KeyVal::getName),
                        slMap -> {
                    ArrayList<KeyValDTO> valDTOS = new ArrayList<>();
                    slMap.forEach((key, value) -> {
                        KeyValDTO dto = new KeyValDTO();
                        dto.setType(key + "(" + value.size() + ")");
                        dto.setKvs(value);
                        valDTOS.add(dto);
                    });
                    return valDTOS;
        }));
        model.setData(collect);
        model.setMessage("实体列表返回成功");
        return JSON.toJSONString(model);

    }


    @RequestMapping(value = "/cdkv",produces = "application/json;charset=UTF-8")
    @LoginRequired
    public String cdkv(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                         @RequestParam(value = "likeName", required = false) String likeName,
                         String uuid,
                         HttpServletRequest request) throws Exception {

//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
        Integer uid = 1;


        ReturnModel model = new ReturnModel(null, "", 1);
        System.out.println("pagenum:" + pageNum);
        List<KeyVal> nws = keyValService.keyValPage((pageNum - 1) * pageSize, pageSize, uid, uuid,likeName);
        Page page = new Page();
        page.setPageList(nws);
        model.setData(page);
        model.setMessage("获取列表成功");
        model.setStatus(0);
        return JSON.toJSONString(model);

    }





}
