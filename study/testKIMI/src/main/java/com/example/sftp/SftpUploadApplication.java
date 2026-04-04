package com.example.sftp;

import com.example.sftp.config.LocalFileProperties;
import com.example.sftp.config.SftpProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Spring Boot SFTP�ļ��ϴ�Ӧ��������
 */
@SpringBootApplication
@EnableConfigurationProperties({SftpProperties.class, LocalFileProperties.class})
public class SftpUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SftpUploadApplication.class, args);
    }
}
