package com.cemh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

/**
 * Web配置类 - 修复CORS配置冲突
 *
 * 使用 CorsFilter Bean 替代 WebMvcConfigurer 的 addCorsMappings 方法
 * 解决 allowCredentials(true) 和 allowedOrigins 冲突问题
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:D:/uploads}")
    private String uploadPath;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 设置允许的源
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:5173" )); // 确保这里是前端的端口

        // 设置允许的请求方法
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 设置允许的请求头
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));

        // 设置暴露的响应头
        corsConfiguration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));

        // 允许携带凭证
        corsConfiguration.setAllowCredentials(true);

        // 设置预检请求的缓存时间
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保路径以file:开头，并且以/结尾
        String path = uploadPath;
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        
        // 使用file:协议访问本地文件系统
        String filePath = "file:" + path;
        
        System.out.println("静态资源映射: /uploads/** -> " + filePath);
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(filePath);
    }
}
