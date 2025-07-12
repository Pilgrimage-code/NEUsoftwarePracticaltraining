package com.cemh.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.junit.jupiter.api.Assertions.*;

class AliyunOSSPropertiesTest {

    private AliyunOSSProperties properties;

    @BeforeEach
    void setUp() {
        properties = new AliyunOSSProperties();
    }

    @Test
    void testGetEndpoint() {
        String expectedEndpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        properties.setEndpoint(expectedEndpoint);
        assertEquals(expectedEndpoint, properties.getEndpoint());
    }

    @Test
    void testSetEndpoint() {
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        properties.setEndpoint(endpoint);
        assertEquals(endpoint, properties.getEndpoint());
    }

    @Test
    void testGetEndpointNull() {
        properties.setEndpoint(null);
        assertNull(properties.getEndpoint());
    }

    @Test
    void testGetEndpointEmpty() {
        properties.setEndpoint("");
        assertEquals("", properties.getEndpoint());
    }

    @Test
    void testGetBucketName() {
        String expectedBucketName = "my-test-bucket";
        properties.setBucketName(expectedBucketName);
        assertEquals(expectedBucketName, properties.getBucketName());
    }

    @Test
    void testSetBucketName() {
        String bucketName = "another-bucket";
        properties.setBucketName(bucketName);
        assertEquals(bucketName, properties.getBucketName());
    }

    @Test
    void testGetBucketNameNull() {
        properties.setBucketName(null);
        assertNull(properties.getBucketName());
    }

    @Test
    void testGetBucketNameEmpty() {
        properties.setBucketName("");
        assertEquals("", properties.getBucketName());
    }

    @Test
    void testGetRegion() {
        String expectedRegion = "cn-hangzhou";
        properties.setRegion(expectedRegion);
        assertEquals(expectedRegion, properties.getRegion());
    }

    @Test
    void testSetRegion() {
        String region = "cn-beijing";
        properties.setRegion(region);
        assertEquals(region, properties.getRegion());
    }

    @Test
    void testGetRegionNull() {
        properties.setRegion(null);
        assertNull(properties.getRegion());
    }

    @Test
    void testGetRegionEmpty() {
        properties.setRegion("");
        assertEquals("", properties.getRegion());
    }

    @Test
    void testGetAccessKeyId() {
        String expectedAccessKeyId = "LTAI5tRqFHMJqKqKqKqKqKq";
        properties.setAccessKeyId(expectedAccessKeyId);
        assertEquals(expectedAccessKeyId, properties.getAccessKeyId());
    }

    @Test
    void testSetAccessKeyId() {
        String accessKeyId = "LTAI5tRqFHMJqKqKqKqKqKqKq";
        properties.setAccessKeyId(accessKeyId);
        assertEquals(accessKeyId, properties.getAccessKeyId());
    }

    @Test
    void testGetAccessKeyIdNull() {
        properties.setAccessKeyId(null);
        assertNull(properties.getAccessKeyId());
    }

    @Test
    void testGetAccessKeyIdEmpty() {
        properties.setAccessKeyId("");
        assertEquals("", properties.getAccessKeyId());
    }

    @Test
    void testGetAccessKeySecret() {
        String expectedAccessKeySecret = "secret123456789";
        properties.setAccessKeySecret(expectedAccessKeySecret);
        assertEquals(expectedAccessKeySecret, properties.getAccessKeySecret());
    }

    @Test
    void testSetAccessKeySecret() {
        String accessKeySecret = "anotherSecret123";
        properties.setAccessKeySecret(accessKeySecret);
        assertEquals(accessKeySecret, properties.getAccessKeySecret());
    }

    @Test
    void testGetAccessKeySecretNull() {
        properties.setAccessKeySecret(null);
        assertNull(properties.getAccessKeySecret());
    }

    @Test
    void testGetAccessKeySecretEmpty() {
        properties.setAccessKeySecret("");
        assertEquals("", properties.getAccessKeySecret());
    }

    @Test
    void testAllPropertiesSet() {
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        String bucketName = "test-bucket";
        String region = "cn-hangzhou";
        String accessKeyId = "LTAI5tRqFHMJqKqKqKqKqKq";
        String accessKeySecret = "secret123456789";

        properties.setEndpoint(endpoint);
        properties.setBucketName(bucketName);
        properties.setRegion(region);
        properties.setAccessKeyId(accessKeyId);
        properties.setAccessKeySecret(accessKeySecret);

        assertEquals(endpoint, properties.getEndpoint());
        assertEquals(bucketName, properties.getBucketName());
        assertEquals(region, properties.getRegion());
        assertEquals(accessKeyId, properties.getAccessKeyId());
        assertEquals(accessKeySecret, properties.getAccessKeySecret());
    }

    @Test
    void testAllPropertiesNull() {
        properties.setEndpoint(null);
        properties.setBucketName(null);
        properties.setRegion(null);
        properties.setAccessKeyId(null);
        properties.setAccessKeySecret(null);

        assertNull(properties.getEndpoint());
        assertNull(properties.getBucketName());
        assertNull(properties.getRegion());
        assertNull(properties.getAccessKeyId());
        assertNull(properties.getAccessKeySecret());
    }

    @Test
    void testAllPropertiesEmpty() {
        properties.setEndpoint("");
        properties.setBucketName("");
        properties.setRegion("");
        properties.setAccessKeyId("");
        properties.setAccessKeySecret("");

        assertEquals("", properties.getEndpoint());
        assertEquals("", properties.getBucketName());
        assertEquals("", properties.getRegion());
        assertEquals("", properties.getAccessKeyId());
        assertEquals("", properties.getAccessKeySecret());
    }

    @Test
    void testClassAnnotations() {
        // 测试类注解
        assertTrue(properties.getClass().isAnnotationPresent(org.springframework.stereotype.Component.class));
        assertTrue(properties.getClass().isAnnotationPresent(ConfigurationProperties.class));
        
        ConfigurationProperties configProps = properties.getClass().getAnnotation(ConfigurationProperties.class);
        assertEquals("aliyun.oss", configProps.prefix());
    }

    @Test
    void testInstanceNotNull() {
        assertNotNull(properties);
    }

    @Test
    void testInstanceType() {
        assertTrue(properties instanceof AliyunOSSProperties);
    }

    @Test
    void testToString() {
        properties.setEndpoint("https://test.com");
        properties.setBucketName("test-bucket");
        properties.setRegion("test-region");
        properties.setAccessKeyId("test-key");
        properties.setAccessKeySecret("test-secret");

        String toString = properties.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("endpoint"));
        assertTrue(toString.contains("bucketName"));
        assertTrue(toString.contains("region"));
        assertTrue(toString.contains("accessKeyId"));
        assertTrue(toString.contains("accessKeySecret"));
    }

    @Test
    void testEquals() {
        AliyunOSSProperties properties1 = new AliyunOSSProperties();
        AliyunOSSProperties properties2 = new AliyunOSSProperties();

        // 设置相同的值
        properties1.setEndpoint("https://test.com");
        properties1.setBucketName("test-bucket");
        properties1.setRegion("test-region");
        properties1.setAccessKeyId("test-key");
        properties1.setAccessKeySecret("test-secret");

        properties2.setEndpoint("https://test.com");
        properties2.setBucketName("test-bucket");
        properties2.setRegion("test-region");
        properties2.setAccessKeyId("test-key");
        properties2.setAccessKeySecret("test-secret");

        // 由于没有重写equals方法，应该使用Object.equals的默认实现
        assertFalse(properties1.equals(properties2));
        assertTrue(properties1.equals(properties1));
        assertFalse(properties1.equals(null));
        assertFalse(properties1.equals(new Object()));
    }

    @Test
    void testHashCode() {
        AliyunOSSProperties properties1 = new AliyunOSSProperties();
        AliyunOSSProperties properties2 = new AliyunOSSProperties();

        // 由于没有重写hashCode方法，应该使用Object.hashCode的默认实现
        assertNotEquals(properties1.hashCode(), properties2.hashCode());
        assertEquals(properties1.hashCode(), properties1.hashCode());
    }

    @Test
    void testSpecialCharacters() {
        String specialEndpoint = "https://oss-cn-hangzhou.aliyuncs.com:8080";
        String specialBucketName = "test-bucket-123";
        String specialRegion = "cn-hangzhou-zone-a";
        String specialAccessKeyId = "LTAI5tRqFHMJqKqKqKqKqKq_123";
        String specialAccessKeySecret = "secret123!@#$%^&*()";

        properties.setEndpoint(specialEndpoint);
        properties.setBucketName(specialBucketName);
        properties.setRegion(specialRegion);
        properties.setAccessKeyId(specialAccessKeyId);
        properties.setAccessKeySecret(specialAccessKeySecret);

        assertEquals(specialEndpoint, properties.getEndpoint());
        assertEquals(specialBucketName, properties.getBucketName());
        assertEquals(specialRegion, properties.getRegion());
        assertEquals(specialAccessKeyId, properties.getAccessKeyId());
        assertEquals(specialAccessKeySecret, properties.getAccessKeySecret());
    }

    @Test
    void testLongValues() {
        String longEndpoint = "https://" + "a".repeat(100) + ".aliyuncs.com";
        String longBucketName = "b".repeat(200);
        String longRegion = "c".repeat(150);
        String longAccessKeyId = "d".repeat(100);
        String longAccessKeySecret = "e".repeat(200);

        properties.setEndpoint(longEndpoint);
        properties.setBucketName(longBucketName);
        properties.setRegion(longRegion);
        properties.setAccessKeyId(longAccessKeyId);
        properties.setAccessKeySecret(longAccessKeySecret);

        assertEquals(longEndpoint, properties.getEndpoint());
        assertEquals(longBucketName, properties.getBucketName());
        assertEquals(longRegion, properties.getRegion());
        assertEquals(longAccessKeyId, properties.getAccessKeyId());
        assertEquals(longAccessKeySecret, properties.getAccessKeySecret());
    }

    @Test
    void testUnicodeValues() {
        String unicodeEndpoint = "https://oss-cn-杭州.aliyuncs.com";
        String unicodeBucketName = "测试-存储桶";
        String unicodeRegion = "cn-杭州";
        String unicodeAccessKeyId = "测试密钥ID";
        String unicodeAccessKeySecret = "测试密钥Secret";

        properties.setEndpoint(unicodeEndpoint);
        properties.setBucketName(unicodeBucketName);
        properties.setRegion(unicodeRegion);
        properties.setAccessKeyId(unicodeAccessKeyId);
        properties.setAccessKeySecret(unicodeAccessKeySecret);

        assertEquals(unicodeEndpoint, properties.getEndpoint());
        assertEquals(unicodeBucketName, properties.getBucketName());
        assertEquals(unicodeRegion, properties.getRegion());
        assertEquals(unicodeAccessKeyId, properties.getAccessKeyId());
        assertEquals(unicodeAccessKeySecret, properties.getAccessKeySecret());
    }
} 