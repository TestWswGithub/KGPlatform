package com.lingjoin.web.controller;


import com.alibaba.fastjson.JSON;
import com.lingjoin.common.config.MyConfig;
import com.lingjoin.common.util.ReturnModel;

import com.lingjoin.web.util.KeyWordVO;
import com.lingjoin.web.util.WordFreqVO;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/nlpir",produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = {"null","http://172.16.1.184:8080"},allowCredentials = "true")
public class StrengthAnalyzController {

    @Autowired
    private MyConfig myConfig;


    public static class NlpirResult{


        public Map<String,Map<String, List<String>>> getResults() {
            return results;
        }

        public void setResults(Map<String,Map<String, List<String>>> results) {
            this.results = results;
        }

        private Map<String,Map<String, List<String>>> results;
        @Override
        public String toString() {
            return "NlpirResult{" +
                    "results=" + results +
                    '}';
        }

    }
    public static class ResultToken{

        private String token;

        @Override
        public String toString() {
            return "ResultToken{" +
                    "token='" + token + '\'' +
                    '}';
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public ResultToken() {
        }
    }
    private final static char[] HEXARRAY = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEXARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEXARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    @RequestMapping("/nlpir")
    public String nlpir(String text, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {


        System.out.println(text);
        ReturnModel model = new ReturnModel(null, null, 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        //获取用户名
        //String username=user.getName();
        String username="tests";
        Date date = new Date();
        System.out.println(date);

        //todo python中需要的time与java中不同，java是毫秒数，python是秒数 而且由于容器时间快 需要+6S
        String s = String.valueOf(date.getTime() / 1000);
        String method = "ictclas";

        MessageDigest usermd5 = null;
        try {
            usermd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //生成用户token
        byte[] bytes = usermd5.digest((username + myConfig.getDockerClient()).getBytes(StandardCharsets.UTF_8));
        String userToken = bytesToHex(bytes);
        System.out.println(userToken);
        MessageDigest codemd5 = null;
        try {
            codemd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //用户token和时间戳生成code
        byte[] digest = codemd5.digest((userToken + s).getBytes(StandardCharsets.UTF_8));
        String code = bytesToHex(digest);
        System.out.println(code);

        System.out.println(s);
        ArrayList<String> texts = new ArrayList<>();
        texts.add(text);
        HashMap<String, Object> data = new HashMap<>();
        data.put("text",texts);
        data.put("POS",true);

        HashMap<String, Object> param = new HashMap<>();
        param.put("instant",false);
        param.put("username",username);
        param.put("timestamp", s);
        param.put("method",method);
        param.put("code", code);
        param.put("data", data);


        String jsonparam = JSON.toJSONString(param);

        System.out.println(jsonparam);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(myConfig.getDockerServiveAddr()+"/nlpir/text/");
        System.out.println("==========");
        System.out.println(myConfig.getDockerServiveAddr());
        System.out.println("==========");

        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        httpPost.setEntity(new StringEntity(jsonparam,StandardCharsets.UTF_8));

        CloseableHttpResponse response = null;
        ResultToken resultToken=null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String toString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

            System.out.println(toString);
            resultToken = JSON.parseObject(toString, ResultToken.class);

            System.out.println(resultToken);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //由于容器服务未能及时得到结果，所以只能等待1S 再获取结果。//todo 容器优化之后修改此处
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        NlpirResult nlpirResult = getResultRequest(username, s, method, resultToken.getToken());
        //层层获取对应值
        List<String> resultString = nlpirResult.getResults().get(method).get(resultToken.getToken());

        String[] strings = resultString.get(0).split(" ");
        System.out.println(Arrays.toString(strings));

        model.setData(strings);
        return JSON.toJSONString(model);



    }

    @RequestMapping("/classify")
    public String classify(String text, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println(text);
        ReturnModel model = new ReturnModel(null, null, 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        //获取用户名
        //String username=user.getName();
        String username="tests";
        Date date = new Date();
        System.out.println(date);

        //todo python中需要的time与java中不同，java是毫秒数，python是秒数 而且由于容器时间快 需要+6S
        String s = String.valueOf(date.getTime() / 1000);
        String method = "classify";

        MessageDigest usermd5 = null;
        try {
            usermd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //生成用户token
        byte[] bytes = usermd5.digest((username + myConfig.getDockerClient()).getBytes(StandardCharsets.UTF_8));
        String userToken = bytesToHex(bytes);
        System.out.println(userToken);
        MessageDigest codemd5 = null;
        try {
            codemd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //用户token和时间戳生成code
        byte[] digest = codemd5.digest((userToken + s).getBytes(StandardCharsets.UTF_8));
        String code = bytesToHex(digest);
        System.out.println(code);

        System.out.println(s);
        ArrayList<String> texts = new ArrayList<>();
        texts.add(text);
        HashMap<String, Object> data = new HashMap<>();
        data.put("text",texts);
        //data.put("POS",true);

        HashMap<String, Object> param = new HashMap<>();
        param.put("instant",false);
        param.put("username",username);
        param.put("timestamp", s);
        param.put("method",method);
        param.put("code", code);
        param.put("data", data);


        String jsonparam = JSON.toJSONString(param);

        System.out.println(jsonparam);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(myConfig.getDockerServiveAddr()+"/nlpir/text/");

        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        httpPost.setEntity(new StringEntity(jsonparam,StandardCharsets.UTF_8));

        CloseableHttpResponse response = null;
        ResultToken resultToken=null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String toString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

            System.out.println(toString);
            resultToken = JSON.parseObject(toString, ResultToken.class);

            System.out.println(resultToken);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //由于容器服务未能及时得到结果，所以只能等待1S 再获取结果。//todo 容器优化之后修改此处
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NlpirResult nlpirResult = getResultRequest(username, s, method, resultToken.getToken());
        //层层获取对应值
        List<String> resultString = nlpirResult.getResults().get(method).get(resultToken.getToken());

        String classifyResult = resultString.get(0);
        System.out.println(classifyResult);

        model.setData(classifyResult);
        return JSON.toJSONString(model);
    }

    @RequestMapping("/key_words")
    public String keyWords(String text, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println(text);
        ReturnModel model = new ReturnModel(null, null, 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        //获取用户名
        //String username=user.getName();
        String username="tests";
        Date date = new Date();
        System.out.println(date);

        //todo python中需要的time与java中不同，java是毫秒数，python是秒数 而且由于容器时间快 需要+6S
        String s = String.valueOf(date.getTime() / 1000);
        String method = "key_extract";

        MessageDigest usermd5 = null;
        try {
            usermd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //生成用户token
        byte[] bytes = usermd5.digest((username + myConfig.getDockerClient()).getBytes(StandardCharsets.UTF_8));
        String userToken = bytesToHex(bytes);
        System.out.println(userToken);
        MessageDigest codemd5 = null;
        try {
            codemd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //用户token和时间戳生成code
        byte[] digest = codemd5.digest((userToken + s).getBytes(StandardCharsets.UTF_8));
        String code = bytesToHex(digest);
        System.out.println(code);

        System.out.println(s);
        ArrayList<String> texts = new ArrayList<>();
        texts.add(text);
        HashMap<String, Object> data = new HashMap<>();
        data.put("text",texts);
        data.put("keys",50);

        HashMap<String, Object> param = new HashMap<>();
        param.put("instant",false);
        param.put("username",username);
        param.put("timestamp", s);
        param.put("method",method);
        param.put("code", code);
        param.put("data", data);


        String jsonparam = JSON.toJSONString(param);

        System.out.println(jsonparam);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(myConfig.getDockerServiveAddr()+"/nlpir/text/");

        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        httpPost.setEntity(new StringEntity(jsonparam,StandardCharsets.UTF_8));

        CloseableHttpResponse response = null;
        ResultToken resultToken=null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String toString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

            System.out.println(toString);
            resultToken = JSON.parseObject(toString, ResultToken.class);

            System.out.println(resultToken);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //由于容器服务未能及时得到结果，所以只能等待1S 再获取结果。//todo 容器优化之后修改此处
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        NlpirResult nlpirResult = getResultRequest(username, s, method, resultToken.getToken());
        //层层获取对应值
        List<String> resultString = nlpirResult.getResults().get(method).get(resultToken.getToken());

        String keyWordsResult = resultString.get(0);
        System.out.println(keyWordsResult);
        String[] split = keyWordsResult.split("#");
        ArrayList<KeyWordVO> keywords = new ArrayList<>();
        Random r = new Random();
        for (int i=0;i<split.length;i++) keywords.add(new KeyWordVO(split[i],r.nextInt(99)));
        model.setData(keywords);
        return JSON.toJSONString(model);
    }



    //todo 词频接口
    //todo 目前是根据分词结果自己进行统计 但以后容器会增加相应的接口，需要修改此处代码
    @RequestMapping("/word_freq")
    public String wordFreq(String text, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        System.out.println(text);
        ReturnModel model = new ReturnModel(null, null, 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        //获取用户名
        //String username=user.getName();
        String username="tests";
        Date date = new Date();
        System.out.println(date);

        //todo python中需要的time与java中不同，java是毫秒数，python是秒数 而且由于容器时间快 需要+6S
        String s = String.valueOf(date.getTime() / 1000);
        String method = "ictclas";

        MessageDigest usermd5 = null;
        try {
            usermd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //生成用户token
        byte[] bytes = usermd5.digest((username + myConfig.getDockerClient()).getBytes(StandardCharsets.UTF_8));
        String userToken = bytesToHex(bytes);
        System.out.println(userToken);
        MessageDigest codemd5 = null;
        try {
            codemd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //用户token和时间戳生成code
        byte[] digest = codemd5.digest((userToken + s).getBytes(StandardCharsets.UTF_8));
        String code = bytesToHex(digest);
        System.out.println(code);

        System.out.println(s);
        ArrayList<String> texts = new ArrayList<>();
        texts.add(text);
        HashMap<String, Object> data = new HashMap<>();
        data.put("text",texts);
        data.put("POS",true);

        HashMap<String, Object> param = new HashMap<>();
        param.put("instant",false);
        param.put("username",username);
        param.put("timestamp", s);
        param.put("method",method);
        param.put("code", code);
        param.put("data", data);


        String jsonparam = JSON.toJSONString(param);

        System.out.println(jsonparam);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(myConfig.getDockerServiveAddr()+"/nlpir/text/");
        System.out.println("==========");
        System.out.println(myConfig.getDockerServiveAddr());
        System.out.println("==========");

        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        httpPost.setEntity(new StringEntity(jsonparam,StandardCharsets.UTF_8));

        CloseableHttpResponse response = null;
        ResultToken resultToken=null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String toString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

            System.out.println(toString);
            resultToken = JSON.parseObject(toString, ResultToken.class);

            System.out.println(resultToken);

        } catch (IOException e) {
            e.printStackTrace();
        }


        //由于容器服务未能及时得到结果，所以只能等待1S 再获取结果。//todo 容器优化之后修改此处
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NlpirResult nlpirResult = getResultRequest(username, s, method, resultToken.getToken());
        //层层获取对应值
        List<String> resultString = nlpirResult.getResults().get(method).get(resultToken.getToken());

        String[] strings = resultString.get(0).split(" ");

        List<String> list = Arrays.asList(strings);



        Map<String, List<WordFreqVO>> collect = list.stream().collect(Collectors.collectingAndThen(Collectors.groupingBy(new Function<String, String>() {
            @Override
            public String apply(String t) {
                //n,v,a
                if (!t.contains("/")) return "";
                String[] split = t.split("/");
                if (split.length<2) return "";
                if (split[1].length()==0) return "";
                return String.valueOf(split[1].charAt(0));
            }
        }), new Function<Map<String, List<String>>, Map<String,  List<WordFreqVO>>>() {
            @Override
            public Map<String, List<WordFreqVO>> apply(Map<String, List<String>> stringListMap) {

                HashMap<String, List<WordFreqVO>> map = new HashMap<>();

                //所有名词
                List<String> ns = stringListMap.get("n");
                List<WordFreqVO> nss = ns.stream().collect(Collectors.collectingAndThen(Collectors.groupingBy(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        return s.split("/")[0];
                    }
                }), new Function<Map<String, List<String>>, List<WordFreqVO>>() {
                    @Override
                    public List<WordFreqVO> apply(Map<String, List<String>> stringListMap) {
                        ArrayList<WordFreqVO> nWordFreqs = new ArrayList<>();

                        stringListMap.forEach((k, v) -> nWordFreqs.add(new WordFreqVO(k, v.size())));

                        return nWordFreqs;
                    }
                }));
                List<WordFreqVO> nsLimit = nss.stream().sorted((o1,o2)->o2.getValue()-o1.getValue()).limit(10).collect(Collectors.toList());
                map.put("ns",nsLimit);


                //所有动词
                List<String> vs = stringListMap.get("v");

                List<WordFreqVO> vss = vs.stream().collect(Collectors.collectingAndThen(Collectors.groupingBy(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        return s.split("/")[0];
                    }
                }), new Function<Map<String, List<String>>, List<WordFreqVO>>() {
                    @Override
                    public List<WordFreqVO> apply(Map<String, List<String>> stringListMap) {
                        ArrayList<WordFreqVO> nWordFreqs = new ArrayList<>();

                        stringListMap.forEach((k, v) -> nWordFreqs.add(new WordFreqVO(k, v.size())));

                        return nWordFreqs;
                    }
                }));
                List<WordFreqVO> vsLimit = vss.stream().sorted((o1,o2)->o2.getValue()-o1.getValue()).limit(10).collect(Collectors.toList());

                map.put("vs",vsLimit);
                //所有形容词
                List<String> as = stringListMap.get("a");
                List<WordFreqVO> ass = as.stream().collect(Collectors.collectingAndThen(Collectors.groupingBy(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        return s.split("/")[0];
                    }
                }), new Function<Map<String, List<String>>, List<WordFreqVO>>() {
                    @Override
                    public List<WordFreqVO> apply(Map<String, List<String>> stringListMap) {
                        ArrayList<WordFreqVO> nWordFreqs = new ArrayList<>();

                        stringListMap.forEach((k, v) -> nWordFreqs.add(new WordFreqVO(k, v.size())));

                        return nWordFreqs;
                    }
                }));
                List<WordFreqVO> asLimit = ass.stream().sorted((o1,o2)->o2.getValue()-o1.getValue()).limit(10).collect(Collectors.toList());

                map.put("as",asLimit);

                return map;
            }
        }));

        model.setData(collect);
        return JSON.toJSONString(model);
    }


    public NlpirResult getResultRequest(String username,String time,String method,String token) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Date date = new Date();
        //todo 由于容器时间快6S所以在此加6s
        String s = String.valueOf(date.getTime() / 1000);
        MessageDigest usermd5 = MessageDigest.getInstance("MD5");

        byte[] bytes = usermd5.digest((username + myConfig.getDockerClient()).getBytes(StandardCharsets.UTF_8));
        String userToken = bytesToHex(bytes);
        System.out.println(userToken);
        MessageDigest codemd5 = MessageDigest.getInstance("MD5");


        byte[] digest = codemd5.digest((userToken + s).getBytes(StandardCharsets.UTF_8));
        String code = bytesToHex(digest);
        System.out.println(code);
        ArrayList<String> texts = new ArrayList<>();
        texts.add(token);

        HashMap<String, Object> data = new HashMap<>();
        data.put(method,texts);
        HashMap<String, Object> param = new HashMap<>();
        param.put("username",username);

        param.put("timestamp", s);
        param.put("code", code);
        param.put("tokens", data);
        String jsonparam = JSON.toJSONString(param);

        System.out.println(jsonparam);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(myConfig.getDockerServiveAddr()+"/nlpir/retrieve/");
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setEntity(new StringEntity(jsonparam));

        CloseableHttpResponse response = null;
        NlpirResult nlpirResult =null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            nlpirResult = JSON.parseObject(result, NlpirResult.class);
            System.out.println(nlpirResult);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nlpirResult;
    }

}
