package com.jason.netty;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ImportResource({"classpath*:/spring-mvc.xml","classpath*:/spring-mybatis.xml"})
public class AppConfig {
}
