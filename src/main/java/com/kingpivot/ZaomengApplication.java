package com.kingpivot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class ZaomengApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZaomengApplication.class, args);
    }

    /**
     * 设置匹配.json后缀的请求
     * @param dispatcherServlet
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean bean = new ServletRegistrationBean(dispatcherServlet);
        bean.addUrlMappings("*.json");
        return bean;
    }

}
