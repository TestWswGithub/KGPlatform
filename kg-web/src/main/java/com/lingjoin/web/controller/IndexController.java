package com.lingjoin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class IndexController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;




}
