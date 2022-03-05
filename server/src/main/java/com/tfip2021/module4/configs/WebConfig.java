package com.tfip2021.module4.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedMethods("GET", "POST")
            .allowedOrigins("http://localhost:4200");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        final String FORWARD_URI = "forward:/index.html";

        // Map "/"
        registry.addViewController("/")
                .setViewName(FORWARD_URI);

        // Map "/word", "/word/word", and "/word/word/word" - except for anything starting with "/api/..." or ending with
        // a file extension like ".js" - to index.html. By doing this, the client receives and routes the url. It also
        // allows client-side URLs to be bookmarked.

        // Single directory level - no need to exclude "api"
        registry.addViewController("/{x:[\\w\\-]+}")
                .setViewName(FORWARD_URI);
        // Multi-level directory path, need to exclude "api" on the first part of the path
        registry.addViewController("/{x:^(?!api$).*$}/{y:[\\w\\-]+}")
                .setViewName(FORWARD_URI);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
}
