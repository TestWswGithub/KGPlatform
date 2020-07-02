package com.lingjoin.web.listener;


import com.lingjoin.common.config.MyConfig;
import com.lingjoin.common.jna.NWF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ConfigListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(ConfigListener.class);


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("上下文初始化中...");
        MyConfig myConfig = WebApplicationContextUtils
                .getWebApplicationContext(servletContextEvent.getServletContext()).getBean(MyConfig.class);

    }
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
