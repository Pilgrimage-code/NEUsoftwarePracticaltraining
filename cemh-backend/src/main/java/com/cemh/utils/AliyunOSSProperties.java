package com.cemh.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOSSProperties {
    private String endpoint;
    private String bucketName;
    private String region;
    // 添加 accessKeyId 和 accessKeySecret 属性
    private String accessKeyId;
    private String accessKeySecret;
}
