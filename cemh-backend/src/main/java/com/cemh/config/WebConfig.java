package com.cemh.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    /**
     * 配置日期时间格式
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 配置日期时间模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // 使用自定义的反序列化器支持多种日期格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        
        // 创建支持多种格式的LocalDateTime反序列化器
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws java.io.IOException {
                String dateString = p.getText();
                
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }

                // 如果包含时区信息（Z或+/-时区偏移），先转换为ZonedDateTime再转为LocalDateTime
                if (dateString.contains("Z") || dateString.matches(".*[+-]\\d{2}:?\\d{2}$")) {
                    try {
                        java.time.ZonedDateTime zonedDateTime = java.time.ZonedDateTime.parse(dateString);
                        return zonedDateTime.toLocalDateTime();
                    } catch (java.time.format.DateTimeParseException e) {
                        // 继续尝试其他格式
                    }
                }

                // 尝试各种格式
                java.time.format.DateTimeFormatter[] formatters = {
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
                    java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
                };
                
                for (java.time.format.DateTimeFormatter formatter : formatters) {
                    try {
                        return LocalDateTime.parse(dateString, formatter);
                    } catch (java.time.format.DateTimeParseException e) {
                        // 继续尝试下一个格式
                    }
                }

                // 如果所有格式都失败，抛出异常
                throw new java.io.IOException("无法解析日期时间字符串: " + dateString);
            }
        });
        
        objectMapper.registerModule(javaTimeModule);
        
        // 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许接受空字符串作为null值
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        
        converter.setObjectMapper(objectMapper);
        return converter;
    }

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
