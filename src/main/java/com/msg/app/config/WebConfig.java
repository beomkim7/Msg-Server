package com.msg.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // '/profile' 경로에 대해 www.react.com 도메인에서 오는 요청을 허용
        registry.addMapping("/**")
                .allowedOrigins("https://www.vue.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true); // 쿠키를 포함시킬 경우 true로 설정
    }


}
