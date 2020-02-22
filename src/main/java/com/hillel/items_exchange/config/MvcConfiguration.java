package com.hillel.items_exchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;


@Configuration
@EnableWebMvc
@ComponentScan("com")
public class MvcConfiguration implements WebMvcConfigurer{

    @Bean
    public UrlBasedViewResolver urlBasedViewResolver() {
        System.out.println("started method urlBasedViewResolver");

        UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
        urlBasedViewResolver.setPrefix("/templates/");
        urlBasedViewResolver.setSuffix(".html");
        urlBasedViewResolver.setViewClass(JstlView.class);
        return urlBasedViewResolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        System.out.println("started method addViewControllers");
        
        registry.addViewController("/").setViewName("index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        WebMvcConfigurer.super.addViewControllers(registry);
    }

}
