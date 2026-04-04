package com.example.sftp;

import com.example.sftp.service.SftpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SftpUploadApplicationTests {

    @Autowired
    private SftpService sftpService;

    @Test
    void contextLoads() {
        // 测试Spring上下文是否能正常加载
    }

    @Test
    void testConnection() {
        // 测试SFTP连接
        boolean result = sftpService.testConnection();
        System.out.println("SFTP连接测试结果: " + (result ? "成功" : "失败"));
    }

    @Test
    void testUpload() {
        // 测试文件上传
        boolean result = sftpService.uploadFile();
        System.out.println("SFTP上传测试结果: " + (result ? "成功" : "失败"));
    }
}
