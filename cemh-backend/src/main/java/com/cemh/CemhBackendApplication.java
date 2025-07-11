package com.cemh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 测盟汇管理系统启动类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2025-6-28
 */
@SpringBootApplication
@MapperScan("com.cemh.mapper")
@EnableTransactionManagement
public class CemhBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CemhBackendApplication.class, args);
        System.out.println("===========================================");
        System.out.println("    测盟汇管理系统后端服务启动成功！");
        System.out.println("    API文档地址: http://localhost:8080/api/doc.html");
        System.out.println("    Druid监控: http://localhost:8080/api/druid/");
        System.out.println("===========================================");
    }
}

