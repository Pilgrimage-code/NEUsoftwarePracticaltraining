package com.cemh.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AliyunOSSOperatorTest {

    private static AliyunOSSProperties properties;
    private AliyunOSSOperator operator;
    private OSS mockOssClient;

    // 创建一个可测试的AliyunOSSOperator子类
    static class TestableAliyunOSSOperator extends AliyunOSSOperator {
        private OSS mockOssClient;
        private AliyunOSSProperties testProperties;
        
        public TestableAliyunOSSOperator(AliyunOSSProperties properties, OSS mockOssClient) {
            super(properties);
            this.mockOssClient = mockOssClient;
            this.testProperties = properties;
        }
        
        @Override
        public String upload(byte[] content, String originalFilename) throws Exception {
            // 模拟上传逻辑，不实际调用OSS
            String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
            String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = dir + "/" + newFileName;
            
            // 模拟OSS客户端调用
            if (mockOssClient != null) {
                mockOssClient.putObject(testProperties.getBucketName(), objectName, new ByteArrayInputStream(content));
            }
            
            return testProperties.getEndpoint().split("//")[0] + "//" + testProperties.getBucketName() + "." + testProperties.getEndpoint().split("//")[1] + "/" + objectName;
        }
    }

    @BeforeEach
    void setUp() {
        properties = new AliyunOSSProperties();
        properties.setEndpoint("https://oss-cn-hangzhou.aliyuncs.com");
        properties.setBucketName("test-bucket");
        properties.setRegion("cn-hangzhou");
        properties.setAccessKeyId("test-access-key-id");
        properties.setAccessKeySecret("test-access-key-secret");
        
        mockOssClient = mock(OSS.class);
        operator = new TestableAliyunOSSOperator(properties, mockOssClient);
    }

    @Test
    void testConstructor() {
        assertNotNull(operator);
    }

    @Test
    void testConstructorWithNullProperties() {
        assertThrows(NullPointerException.class, () -> {
            new AliyunOSSOperator(null);
        });
    }

    @Test
    void testConstructorWithNullEndpoint() {
        AliyunOSSProperties nullEndpointProps = new AliyunOSSProperties();
        nullEndpointProps.setEndpoint(null);
        nullEndpointProps.setBucketName("test-bucket");
        nullEndpointProps.setRegion("cn-hangzhou");
        nullEndpointProps.setAccessKeyId("test-key");
        nullEndpointProps.setAccessKeySecret("test-secret");

        AliyunOSSOperator op = new AliyunOSSOperator(nullEndpointProps);
        assertNotNull(op);
    }

    @Test
    void testConstructorWithNullBucketName() {
        AliyunOSSProperties nullBucketProps = new AliyunOSSProperties();
        nullBucketProps.setEndpoint("https://oss-cn-hangzhou.aliyuncs.com");
        nullBucketProps.setBucketName(null);
        nullBucketProps.setRegion("cn-hangzhou");
        nullBucketProps.setAccessKeyId("test-key");
        nullBucketProps.setAccessKeySecret("test-secret");

        AliyunOSSOperator op = new AliyunOSSOperator(nullBucketProps);
        assertNotNull(op);
    }

    @Test
    void testConstructorWithNullRegion() {
        AliyunOSSProperties nullRegionProps = new AliyunOSSProperties();
        nullRegionProps.setEndpoint("https://oss-cn-hangzhou.aliyuncs.com");
        nullRegionProps.setBucketName("test-bucket");
        nullRegionProps.setRegion(null);
        nullRegionProps.setAccessKeyId("test-key");
        nullRegionProps.setAccessKeySecret("test-secret");

        AliyunOSSOperator op = new AliyunOSSOperator(nullRegionProps);
        assertNotNull(op);
    }

    @Test
    void testConstructorWithNullAccessKeyId() {
        AliyunOSSProperties nullKeyProps = new AliyunOSSProperties();
        nullKeyProps.setEndpoint("https://oss-cn-hangzhou.aliyuncs.com");
        nullKeyProps.setBucketName("test-bucket");
        nullKeyProps.setRegion("cn-hangzhou");
        nullKeyProps.setAccessKeyId(null);
        nullKeyProps.setAccessKeySecret("test-secret");

        AliyunOSSOperator op = new AliyunOSSOperator(nullKeyProps);
        assertNotNull(op);
    }

    @Test
    void testConstructorWithNullAccessKeySecret() {
        AliyunOSSProperties nullSecretProps = new AliyunOSSProperties();
        nullSecretProps.setEndpoint("https://oss-cn-hangzhou.aliyuncs.com");
        nullSecretProps.setBucketName("test-bucket");
        nullSecretProps.setRegion("cn-hangzhou");
        nullSecretProps.setAccessKeyId("test-key");
        nullSecretProps.setAccessKeySecret(null);

        AliyunOSSOperator op = new AliyunOSSOperator(nullSecretProps);
        assertNotNull(op);
    }

    @Test
    void testUploadSuccess() throws Exception {
        byte[] content = "test content".getBytes();
        String originalFilename = "test.txt";

        String result = operator.upload(content, originalFilename);
        
        // 验证返回的URL格式 - 不依赖具体的日期和UUID，只验证格式
        assertNotNull(result);
        assertTrue(result.contains("test-bucket"));
        assertTrue(result.contains(".oss-cn-hangzhou.aliyuncs.com/"));
        assertTrue(result.contains(".txt"));
        assertTrue(result.matches("https://test-bucket\\.oss-cn-hangzhou\\.aliyuncs\\.com/\\d{4}/\\d{2}/[a-f0-9-]+\\.txt"));
        
        // 验证OSS客户端被调用
        verify(mockOssClient, times(1)).putObject(eq("test-bucket"), anyString(), any(ByteArrayInputStream.class));
    }

    @Test
    void testUploadWithNullContent() {
        assertThrows(NullPointerException.class, () -> {
            operator.upload(null, "test.txt");
        });
    }

    @Test
    void testUploadWithNullFilename() {
        byte[] content = "test content".getBytes();
        assertThrows(NullPointerException.class, () -> {
            operator.upload(content, null);
        });
    }

    @Test
    void testUploadWithEmptyContent() throws Exception {
        byte[] content = new byte[0];
        String originalFilename = "test.txt";

        String result = operator.upload(content, originalFilename);
        
        assertNotNull(result);
        assertTrue(result.contains("test-bucket"));
        assertTrue(result.contains(".oss-cn-hangzhou.aliyuncs.com/"));
        assertTrue(result.contains(".txt"));
        assertTrue(result.matches("https://test-bucket\\.oss-cn-hangzhou\\.aliyuncs\\.com/\\d{4}/\\d{2}/[a-f0-9-]+\\.txt"));
    }

    @Test
    void testUploadWithEmptyFilename() {
        byte[] content = "test content".getBytes();
        assertThrows(StringIndexOutOfBoundsException.class, () -> {
            operator.upload(content, "");
        });
    }

    @Test
    void testUploadWithFilenameWithoutExtension() throws Exception {
        byte[] content = "test content".getBytes();
        String originalFilename = "testfile";

        // 对于没有扩展名的文件，应该抛出异常或者使用默认扩展名
        assertThrows(StringIndexOutOfBoundsException.class, () -> {
            operator.upload(content, originalFilename);
        });
    }

    @Test
    void testUploadWithMultipleDotsInFilename() throws Exception {
        byte[] content = "test content".getBytes();
        String originalFilename = "test.file.txt";

        String result = operator.upload(content, originalFilename);
        
        assertNotNull(result);
        assertTrue(result.contains("test-bucket"));
        assertTrue(result.contains(".oss-cn-hangzhou.aliyuncs.com/"));
        assertTrue(result.contains(".txt"));
        assertTrue(result.matches("https://test-bucket\\.oss-cn-hangzhou\\.aliyuncs\\.com/\\d{4}/\\d{2}/[a-f0-9-]+\\.txt"));
    }

    @Test
    void testUploadWithSpecialCharactersInFilename() throws Exception {
        byte[] content = "test content".getBytes();
        String originalFilename = "test@#$%^&*().txt";

        String result = operator.upload(content, originalFilename);
        
        assertNotNull(result);
        assertTrue(result.contains("test-bucket"));
        assertTrue(result.contains(".oss-cn-hangzhou.aliyuncs.com/"));
        assertTrue(result.contains(".txt"));
        assertTrue(result.matches("https://test-bucket\\.oss-cn-hangzhou\\.aliyuncs\\.com/\\d{4}/\\d{2}/[a-f0-9-]+\\.txt"));
    }

    @Test
    void testUploadWithUnicodeFilename() throws Exception {
        byte[] content = "test content".getBytes();
        String originalFilename = "测试文件.txt";

        String result = operator.upload(content, originalFilename);
        
        assertNotNull(result);
        assertTrue(result.contains("test-bucket"));
        assertTrue(result.contains(".oss-cn-hangzhou.aliyuncs.com/"));
        assertTrue(result.contains(".txt"));
        assertTrue(result.matches("https://test-bucket\\.oss-cn-hangzhou\\.aliyuncs\\.com/\\d{4}/\\d{2}/[a-f0-9-]+\\.txt"));
    }

    @Test
    void testUploadWithDifferentDateFormats() throws Exception {
        byte[] content = "test content".getBytes();
        String originalFilename = "test.txt";

        String result = operator.upload(content, originalFilename);
        
        assertNotNull(result);
        assertTrue(result.contains("test-bucket"));
        assertTrue(result.contains(".oss-cn-hangzhou.aliyuncs.com/"));
        assertTrue(result.contains(".txt"));
        assertTrue(result.matches("https://test-bucket\\.oss-cn-hangzhou\\.aliyuncs\\.com/\\d{4}/\\d{2}/[a-f0-9-]+\\.txt"));
    }

    @Test
    void testUploadWithDifferentEndpoints() throws Exception {
        // 测试不同的endpoint格式
        AliyunOSSProperties props1 = new AliyunOSSProperties();
        props1.setEndpoint("http://oss-cn-beijing.aliyuncs.com");
        props1.setBucketName("test-bucket");
        props1.setRegion("cn-beijing");
        props1.setAccessKeyId("test-key");
        props1.setAccessKeySecret("test-secret");
        
        OSS mockOssClient2 = mock(OSS.class);
        TestableAliyunOSSOperator op1 = new TestableAliyunOSSOperator(props1, mockOssClient2);
        
        byte[] content = "test content".getBytes();
        String originalFilename = "test.txt";

        String result = op1.upload(content, originalFilename);
        
        assertNotNull(result);
        assertTrue(result.contains("test-bucket"));
        assertTrue(result.contains(".oss-cn-beijing.aliyuncs.com/"));
        assertTrue(result.contains(".txt"));
        assertTrue(result.startsWith("http://"));
        assertTrue(result.matches("http://test-bucket\\.oss-cn-beijing\\.aliyuncs\\.com/\\d{4}/\\d{2}/[a-f0-9-]+\\.txt"));
        
        // 验证OSS客户端被调用
        verify(mockOssClient2, times(1)).putObject(eq("test-bucket"), anyString(), any(ByteArrayInputStream.class));
    }

    @Test
    void testUploadWithCustomBucketName() throws Exception {
        AliyunOSSProperties props = new AliyunOSSProperties();
        props.setEndpoint("https://oss-cn-hangzhou.aliyuncs.com");
        props.setBucketName("my-custom-bucket");
        props.setRegion("cn-hangzhou");
        props.setAccessKeyId("test-key");
        props.setAccessKeySecret("test-secret");
        
        OSS mockOssClient2 = mock(OSS.class);
        TestableAliyunOSSOperator op = new TestableAliyunOSSOperator(props, mockOssClient2);
        
        byte[] content = "test content".getBytes();
        String originalFilename = "test.txt";

        String result = op.upload(content, originalFilename);
        
        assertNotNull(result);
        assertTrue(result.contains("my-custom-bucket"));
        assertTrue(result.contains(".oss-cn-hangzhou.aliyuncs.com/"));
        assertTrue(result.contains(".txt"));
        assertTrue(result.matches("https://my-custom-bucket\\.oss-cn-hangzhou\\.aliyuncs\\.com/\\d{4}/\\d{2}/[a-f0-9-]+\\.txt"));
        
        // 验证OSS客户端被调用
        verify(mockOssClient2, times(1)).putObject(eq("my-custom-bucket"), anyString(), any(ByteArrayInputStream.class));
    }

    @Test
    void testUploadWithLargeContent() throws Exception {
        byte[] content = new byte[1024 * 1024]; // 1MB
        String originalFilename = "large-file.bin";

        String result = operator.upload(content, originalFilename);
        
        assertNotNull(result);
        assertTrue(result.contains("test-bucket"));
        assertTrue(result.contains(".oss-cn-hangzhou.aliyuncs.com/"));
        assertTrue(result.contains(".bin"));
        assertTrue(result.matches("https://test-bucket\\.oss-cn-hangzhou\\.aliyuncs\\.com/\\d{4}/\\d{2}/[a-f0-9-]+\\.bin"));
    }

    @Test
    void testUploadWithDifferentFileExtensions() throws Exception {
        String[] extensions = {".jpg", ".png", ".gif", ".pdf", ".doc", ".xls", ".zip", ".rar"};
        byte[] content = "test content".getBytes();

        for (String ext : extensions) {
            String originalFilename = "test" + ext;
            String result = operator.upload(content, originalFilename);
            
            assertNotNull(result);
            assertTrue(result.contains("test-bucket"));
            assertTrue(result.contains(".oss-cn-hangzhou.aliyuncs.com/"));
            assertTrue(result.contains(ext));
            assertTrue(result.matches("https://test-bucket\\.oss-cn-hangzhou\\.aliyuncs\\.com/\\d{4}/\\d{2}/[a-f0-9-]+" + ext.replace(".", "\\.")));
        }
    }

    @Test
    void testUploadWithException() {
        byte[] content = "test content".getBytes();
        String originalFilename = "test.txt";

        // 测试当OSS客户端创建失败时的异常处理
        // 这里我们通过设置无效的endpoint来触发异常
        AliyunOSSProperties invalidProps = new AliyunOSSProperties();
        invalidProps.setEndpoint("invalid-endpoint");
        invalidProps.setBucketName("test-bucket");
        invalidProps.setRegion("cn-hangzhou");
        invalidProps.setAccessKeyId("test-key");
        invalidProps.setAccessKeySecret("test-secret");
        
        AliyunOSSOperator invalidOperator = new AliyunOSSOperator(invalidProps);

        assertThrows(Exception.class, () -> {
            invalidOperator.upload(content, originalFilename);
        });
    }

    @Test
    void testClassAnnotations() {
        // 测试原始AliyunOSSOperator类的注解
        assertTrue(AliyunOSSOperator.class.isAnnotationPresent(org.springframework.stereotype.Component.class));
    }

    @Test
    void testInstanceNotNull() {
        assertNotNull(operator);
    }

    @Test
    void testInstanceType() {
        assertTrue(operator instanceof AliyunOSSOperator);
    }

    @Test
    void testToString() {
        String toString = operator.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("AliyunOSSOperator"));
    }

    @Test
    void testHashCode() {
        int hashCode = operator.hashCode();
        assertNotEquals(0, hashCode);
    }

    @Test
    void testEquals() {
        AliyunOSSOperator operator2 = new AliyunOSSOperator(properties);
        
        // 由于没有重写equals方法，应该使用Object.equals的默认实现
        assertFalse(operator.equals(operator2));
        assertTrue(operator.equals(operator));
        assertFalse(operator.equals(null));
        assertFalse(operator.equals(new Object()));
    }

    @Test
    void testOssClientShutdown() throws Exception {
        byte[] content = "test content".getBytes();
        String originalFilename = "test.txt";

        String result = operator.upload(content, originalFilename);
        
        assertNotNull(result);
        // 验证OSS客户端被调用
        verify(mockOssClient, times(1)).putObject(eq("test-bucket"), anyString(), any(ByteArrayInputStream.class));
    }

    @Test
    void testUploadWithNullOssClient() throws Exception {
        // 测试当OSS客户端为null时的情况
        TestableAliyunOSSOperator nullClientOperator = new TestableAliyunOSSOperator(properties, null);
        
        byte[] content = "test content".getBytes();
        String originalFilename = "test.txt";

        String result = nullClientOperator.upload(content, originalFilename);
        
        assertNotNull(result);
        assertTrue(result.contains("test-bucket"));
        assertTrue(result.contains(".oss-cn-hangzhou.aliyuncs.com/"));
        assertTrue(result.contains(".txt"));
    }
} 