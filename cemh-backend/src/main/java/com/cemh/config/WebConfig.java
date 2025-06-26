package com.cemh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:D:/uploads}")
    private String uploadPath;

<<<<<<< HEAD
    @Value("${file.upload.url-prefix:/files}")
    private String urlPrefix;
=======
        // 设置允许的源
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:5173" )); // 确保这里是前端的端口
>>>>>>> 0423ffbbfd386bb8398442d65097936c421ca396

    /**
     * 配置CORS跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

    /**
     * 配置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取操作系统类型
        String os = System.getProperty("os.name").toLowerCase();
        
        // 添加上传文件的虚拟路径映射
        String uploadPath = System.getProperty("user.dir") + File.separator + "uploads";
        String accessPath = "/uploads/**";
        
        // Windows系统和其他系统的路径处理方式不同
        if (os.contains("win")) {
            registry.addResourceHandler(accessPath)
                    .addResourceLocations("file:" + uploadPath + File.separator);
        } else {
            registry.addResourceHandler(accessPath)
                    .addResourceLocations("file:" + uploadPath + File.separator);
        }
        
        // 专门为视频文件添加路径映射
        registry.addResourceHandler("/uploads/videos/**")
                .addResourceLocations("file:" + uploadPath + File.separator + "videos" + File.separator);
        
        // 添加其他静态资源路径
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        // 映射Swagger UI资源
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
        
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
}
