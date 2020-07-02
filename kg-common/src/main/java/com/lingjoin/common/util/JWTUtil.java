package com.lingjoin.common.util;

import com.lingjoin.auth.entity.User;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    public static Map<String, Object> headerMap;

    public static final long EXP = 1000 * 60 * 60;//过期时间为60分钟
    public static final String DEFAULT_ALG = "HS256";//默认加密算法


    static {

        //token的header部分
        headerMap = new HashMap<>();
        headerMap.put("alg", DEFAULT_ALG);
        headerMap.put("typ", "JWT");

    }


    /**
     *根据用户密钥生成 SecretKey
     *
     * @param secret
     * @return
     */
    public static SecretKey generalKey(String secret) {

        //先进行base64URLencode
        byte[] encodedKey = Base64.encodeBase64URLSafe(secret.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, DEFAULT_ALG);
    }

    public static Map<String,Object> generalMap(User user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("deptId", user.getDeptId());
        map.put("name", user.getName());
        map.put("userTypeInt", user.getUserTypeInt());
        return map;

    }




    /**
     * 将用户的 name，role，等字段添加到token的claim当中，前端直接获取解密即可得到用户名等非敏感信息。
     *
     *加入secret密钥，即可得到token
     *
     */
    public static String generalTokenWithDefaultAlg(User user,String secret){
        long now = System.currentTimeMillis();//当前时间(毫秒)

        return Jwts.builder()
                .setHeader(headerMap)
                .setClaims(generalMap(user))
                .setExpiration(new Date(now + EXP))
                .signWith(SignatureAlgorithm.HS256, generalKey(secret)).compact();

    }


    /**
     *
     * 根据密钥对token进行解密
     *
     * @param token
     * @param secret
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String token,String secret) throws Exception{

        Claims body =null;


        try {
            body= Jwts.parser().setSigningKey(generalKey(secret)).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            throw new RuntimeException("token过期");
        }catch (SignatureException e){
            throw new RuntimeException("签名无效");
        }


        return body;

    }





}
