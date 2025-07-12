package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.utils.AliyunOSSOperator;
import com.cemh.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * UploadController测试类
 */
@ExtendWith(MockitoExtension.class)
public class UploadControllerTest {

    @Mock
    private AliyunOSSOperator aliyunOSSOperator;

    @InjectMocks
    private UploadController uploadController;

    @BeforeEach
    void setUp() {
        // 设置控制器的配置参数
        ReflectionTestUtils.setField(uploadController, "uploadPath", "uploads");
        ReflectionTestUtils.setField(uploadController, "urlPrefix", "http://localhost:8080/uploads");
        ReflectionTestUtils.setField(uploadController, "serverPort", "8080");
        ReflectionTestUtils.setField(uploadController, "domain", "http://localhost");
    }

    @Test
    void testUploadImage_Success() throws Exception {
        // 准备测试数据
        byte[] content = "test image content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", content);

        // 模拟OSS上传
        when(aliyunOSSOperator.upload(any(byte[].class), anyString())).thenReturn("http://example.com/test.jpg");

        // 执行测试
        Result<Map<String, Object>> result = uploadController.uploadImage(file);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertNotNull(result.getData().get("url"));
        assertEquals("test.jpg", result.getData().get("originalName"));
    }

    @Test
    void testUploadImage_EmptyFile() {
        // 准备空文件
        MockMultipartFile file = new MockMultipartFile(
                "file", "", "image/jpeg", new byte[0]);

        // 执行测试
        Result<Map<String, Object>> result = uploadController.uploadImage(file);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("请选择要上传的文件", result.getMessage());
    }

    @Test
    void testUploadImage_InvalidType() {
        // 准备非图片文件
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "text content".getBytes());

        // 执行测试
        Result<Map<String, Object>> result = uploadController.uploadImage(file);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("只能上传图片文件", result.getMessage());
    }

    @Test
    void testUploadImage_TooLarge() {
        // 准备超过2MB的文件
        byte[] largeContent = new byte[3 * 1024 * 1024]; // 3MB
        MockMultipartFile file = new MockMultipartFile(
                "file", "large.jpg", "image/jpeg", largeContent);

        // 执行测试
        Result<Map<String, Object>> result = uploadController.uploadImage(file);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("文件大小不能超过2MB", result.getMessage());
    }

    @Test
    void testUploadVideo_Success() throws Exception {
        // 准备测试数据
        byte[] content = "test video content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.mp4", "video/mp4", content);

        // 执行测试
        Result<Map<String, Object>> result = uploadController.uploadVideo(file);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertNotNull(result.getData().get("url"));
        assertEquals("test.mp4", result.getData().get("originalName"));
    }

    @Test
    void testUploadVideo_EmptyFile() {
        // 准备空文件
        MockMultipartFile file = new MockMultipartFile(
                "file", "", "video/mp4", new byte[0]);

        // 执行测试
        Result<Map<String, Object>> result = uploadController.uploadVideo(file);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("请选择要上传的文件", result.getMessage());
    }

    @Test
    void testUploadVideo_InvalidType() {
        // 准备非视频文件
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "text content".getBytes());

        // 执行测试
        Result<Map<String, Object>> result = uploadController.uploadVideo(file);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("只能上传视频文件", result.getMessage());
    }

    @Test
    void testUploadVideo_TooLarge() {
        // 准备超过50MB的文件
        byte[] largeContent = new byte[51 * 1024 * 1024]; // 51MB
        MockMultipartFile file = new MockMultipartFile(
                "file", "large.mp4", "video/mp4", largeContent);

        // 执行测试
        Result<Map<String, Object>> result = uploadController.uploadVideo(file);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("文件大小不能超过50MB", result.getMessage());
    }

    @Test
    void testUploadDocument_Success() throws Exception {
        // 准备测试数据
        byte[] content = "test document content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", content);

        // 模拟OSS上传
        when(aliyunOSSOperator.upload(any(byte[].class), anyString())).thenReturn("http://example.com/test.pdf");

        // 执行测试
        Result<Map<String, String>> result = uploadController.uploadDocument(file);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertNotNull(result.getData().get("url"));
        assertEquals("test.pdf", result.getData().get("name"));
    }

    @Test
    void testUploadDocument_EmptyFile() {
        // 准备空文件
        MockMultipartFile file = new MockMultipartFile(
                "file", "", "application/pdf", new byte[0]);

        // 执行测试
        Result<Map<String, String>> result = uploadController.uploadDocument(file);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("请选择要上传的文件", result.getMessage());
    }

    @Test
    void testUploadDocument_TooLarge() {
        // 准备超过5MB的文件
        byte[] largeContent = new byte[6 * 1024 * 1024]; // 6MB
        MockMultipartFile file = new MockMultipartFile(
                "file", "large.pdf", "application/pdf", largeContent);

        // 执行测试
        Result<Map<String, String>> result = uploadController.uploadDocument(file);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("文件大小不能超过5MB", result.getMessage());
    }

    @Test
    void testUploadFile_Success() throws Exception {
        // 准备测试数据
        byte[] content = "test file content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", content);

        // 模拟OSS上传
        when(aliyunOSSOperator.upload(any(byte[].class), anyString())).thenReturn("http://example.com/test.txt");

        // 执行测试
        Result<Map<String, String>> result = uploadController.uploadFile(file);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertNotNull(result.getData().get("url"));
        assertEquals("test.txt", result.getData().get("name"));
    }

    @Test
    void testUploadImage_IOException() throws Exception {
        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(1024L);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getBytes()).thenThrow(new java.io.IOException("IO error"));
        Result<Map<String, Object>> result = uploadController.uploadImage(file);
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("文件上传失败"));
    }

    @Test
    void testUploadImage_OtherException() throws Exception {
        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(1024L);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getBytes()).thenThrow(new RuntimeException("other error"));
        assertThrows(RuntimeException.class, () -> uploadController.uploadImage(file));
    }

    @Test
    void testUploadVideo_IOException() throws Exception {
        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("video/mp4");
        when(file.getSize()).thenReturn(1024L);
        when(file.getOriginalFilename()).thenReturn("test.mp4");
        doThrow(new java.io.IOException("IO error")).when(file).transferTo(any(File.class));
        Result<Map<String, Object>> result = uploadController.uploadVideo(file);
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("视频上传失败"));
    }

    @Test
    void testUploadDocument_IOException() throws Exception {
        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getSize()).thenReturn(1024L);
        when(file.getOriginalFilename()).thenReturn("test.pdf");
        when(file.getBytes()).thenThrow(new java.io.IOException("IO error"));
        Result<Map<String, String>> result = uploadController.uploadDocument(file);
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("上传文档失败"));
    }

    @Test
    void testUploadFile_IOException() throws Exception {
        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.txt");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenThrow(new java.io.IOException("IO error"));
        Result<Map<String, String>> result = uploadController.uploadFile(file);
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("上传文件失败"));
    }

    @Test
    void testDeleteFile_Success() throws Exception {
        // 准备测试数据 - 创建临时文件
        File tempDir = new File("uploads");
        tempDir.mkdirs();
        File tempFile = new File(tempDir, "test-delete.txt");
        
        // 使用Java 8兼容的方式写入文件
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write("test content".getBytes());
        }
        
        // 准备请求数据
        UploadController.DeleteFileRequest request = new UploadController.DeleteFileRequest();
        request.setUrl(tempFile.getPath());
        
        // 执行测试
        Result<Void> result = uploadController.deleteFile(request);
        
        // 验证结果 - 无法mock静态方法，但可以验证控制器的行为
        assertEquals(200, result.getCode());
        
        // 清理
        tempFile.delete();
        tempDir.delete();
    }

    @Test
    void testDeleteFile_EmptyUrl() {
        // 准备空URL
        UploadController.DeleteFileRequest request = new UploadController.DeleteFileRequest();
        request.setUrl("");

        // 执行测试
        Result<Void> result = uploadController.deleteFile(request);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("文件路径不能为空", result.getMessage());
    }

    @Test
    void testDeleteFile_Fail() {
        UploadController.DeleteFileRequest request = new UploadController.DeleteFileRequest();
        request.setUrl("not_exist_file.txt");
        Result<Void> result = uploadController.deleteFile(request);
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("删除文件失败", result.getMessage());
    }

    @Test
    void testBuildFullUrl() {
        // 通过反射调用私有方法测试不同的URL格式
        String url1 = (String) ReflectionTestUtils.invokeMethod(uploadController, 
                "buildFullUrl", "/uploads/20250627_UUID.mp4");
        assertNotNull(url1);
        assertTrue(url1.contains("/uploads/20250627_UUID.mp4"));

        String url2 = (String) ReflectionTestUtils.invokeMethod(uploadController, 
                "buildFullUrl", "/images/test.png");
        assertNotNull(url2);
        assertTrue(url2.contains("/images/test.png"));
        
        // 测试urlPrefix已经包含完整URL的情况
        ReflectionTestUtils.setField(uploadController, "urlPrefix", "https://example.com/uploads");
        String url3 = (String) ReflectionTestUtils.invokeMethod(uploadController, 
                "buildFullUrl", "/test.jpg");
        assertNotNull(url3);
        assertEquals("https://example.com/uploads/test.jpg", url3);
        
        // 测试url和prefix都带/的情况
        ReflectionTestUtils.setField(uploadController, "urlPrefix", "https://example.com/uploads/");
        String url4 = (String) ReflectionTestUtils.invokeMethod(uploadController, 
                "buildFullUrl", "/test2.jpg");
        assertNotNull(url4);
        assertEquals("https://example.com/uploads/test2.jpg", url4);
        
        // 测试url和prefix都不带/的情况
        ReflectionTestUtils.setField(uploadController, "urlPrefix", "https://example.com/uploads");
        String url5 = (String) ReflectionTestUtils.invokeMethod(uploadController, 
                "buildFullUrl", "test3.jpg");
        assertNotNull(url5);
        assertEquals("https://example.com/uploads/test3.jpg", url5);
    }

    @Test
    void testBuildFullUrl_OtherPrefix() {
        org.springframework.test.util.ReflectionTestUtils.setField(uploadController, "urlPrefix", "uploads");
        org.springframework.test.util.ReflectionTestUtils.setField(uploadController, "domain", "http://localhost");
        org.springframework.test.util.ReflectionTestUtils.setField(uploadController, "serverPort", "8080");
        String url = (String) org.springframework.test.util.ReflectionTestUtils.invokeMethod(uploadController, "buildFullUrl", "test4.jpg");
        assertNotNull(url);
        assertTrue(url.contains("http://localhost:8080/uploads/test4.jpg"));
    }
} 