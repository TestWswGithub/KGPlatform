package com.lingjoin.web.inteceptor;

import com.alibaba.fastjson.JSON;
import com.lingjoin.common.anonation.AdminAuthRequired;
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
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AdminAuthInterceptor.class);

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断接口是否需要管理员权限
        AdminAuthRequired adminAuthRequired  = method.getAnnotation(AdminAuthRequired.class);
        boolean needAdminAuth=adminAuthRequired!=null;
        // 有 @AdminAuthRequired 注解，需要管理员权限

        if(!needAdminAuth) return true;

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        ReturnModel model = new ReturnModel(null, "", 0);
        String token = request.getHeader("Authorization");

        String[] split = token.split("\\.");
        byte[] bytes = Base64.decodeBase64(split[1]);
        String s = new String(bytes);

        User user = JSON.parseObject(s, User.class);
        String secret = stringRedisTemplate.opsForValue().get("usertokenuid" + user.getId());
        //如果用户密钥不存在或者过期 拒绝
        if (secret == null) {
            model.setMessage("用户密钥不存在或者过期");
            model.setStatus(1);
            out = response.getWriter();
            out.append(JSON.toJSONString(model));
            return false;
        }


        Claims claims = null;

        try {
            claims = JWTUtil.parseJWT(token, secret);
        } catch (Exception e) {
            model.setMessage(e.getMessage());
            model.setStatus(1);
            out = response.getWriter();
            out.append(JSON.toJSONString(model));
            return false;
        }

        Integer userType = user.getUserTypeInt();
        //判断用户类型是否是1
        if (userType==1) return true;
        else {
            model.setStatus(403);
            model.setMessage("没有权限访问");
            out = response.getWriter();
            out.append(JSON.toJSONString(model));
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
