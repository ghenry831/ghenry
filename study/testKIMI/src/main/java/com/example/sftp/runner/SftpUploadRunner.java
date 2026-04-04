package com.example.sftp.runner;

import com.example.sftp.service.SftpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动后执行SFTP文件上传
 * 实现ApplicationRunner接口，在Spring Boot应用启动完成后自动执行
 */
@Component
public class SftpUploadRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(SftpUploadRunner.class);

    @Autowired
    private SftpService sftpService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("========================================");
        logger.info("Spring Boot应用启动完成，开始执行SFTP文件上传任务");
        logger.info("========================================");

        // 首先测试连接
        boolean connectionSuccess = sftpService.testConnection();
        if (!connectionSuccess) {
            logger.error("SFTP服务器连接失败，请检查配置");
            // 连接失败不终止应用，只记录错误
            return;
        }

        // 执行文件上传
        boolean uploadSuccess = sftpService.uploadFile();

        if (uploadSuccess) {
            logger.info("========================================");
            logger.info("SFTP文件上传任务执行成功");
            logger.info("========================================");
        } else {
            logger.error("========================================");
            logger.error("SFTP文件上传任务执行失败");
            logger.error("========================================");
        }
    }
}
