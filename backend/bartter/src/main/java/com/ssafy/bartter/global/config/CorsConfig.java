package com.ssafy.bartter.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 와 관련된 설정을 하는 클래스
 *
 * @author 김훈민
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // TODO : 배포시 변경 필요
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("http://localhost:5173")
                .allowCredentials(true);;
    }
}