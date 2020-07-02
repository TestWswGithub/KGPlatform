package com.lingjoin.web.inteceptor;

import com.alibaba.fastjson.JSON;
import com.lingjoin.common.anonation.LoginRequired;
import com.lingjoin.auth.entity.User;
import com.lingjoin.common.util.JWTUtil;
import com.lingjoin.common.util.ReturnModel;
import io.jsonwebtoken.Claims;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断接口是否需要登录
        LoginRequired methodAnnotation  = method.getAnnotation(LoginRequired.class);
        boolean needLogin=methodAnnotation!=null;
        // 有 @LoginRequired 注解，需要认证

        if (!needLogin) return true;

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        ReturnModel model = new ReturnModel(null, "", 0);
        String token = request.getHeader("Authorization");
        //如果没有携带token 拒绝
        if (StringUtils.isEmpty(token)) {
            model.setMessage("请求头没有携带token");
            model.setStatus(403);

            out = response.getWriter();
            out.append(JSON.toJSONString(model));
            return false;
        }

        String[] split = token.split("\\.");
        //对token的第二部分进行解密，可以得到用户的用户名 权限相关等非敏感信息。
        byte[] bytes = Base64.decodeBase64(split[1]);
        String s = new String(bytes);

        //解析成user对象。
        User user = JSON.parseObject(s, User.class);
        String secret = stringRedisTemplate.opsForValue().get("usertokenuid" + user.getId());
        //如果用户密钥不存在或者过期 拒绝
        if (secret == null) {
            model.setMessage("用户密钥不存在或者过期");
            model.setStatus(403);
            out = response.getWriter();
            out.append(JSON.toJSONString(model));
            return false;
        }


        Claims claims = null;

        try {
            claims = JWTUtil.parseJWT(token, secret);
        } catch (Exception e) {
            model.setMessage(e.getMessage());
            model.setStatus(403);
            out = response.getWriter();
            out.append(JSON.toJSONString(model));
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
