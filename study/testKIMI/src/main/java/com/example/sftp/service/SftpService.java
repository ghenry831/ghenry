package com.example.sftp.service;

import com.example.sftp.config.LocalFileProperties;
import com.example.sftp.config.SftpProperties;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Properties;

/**
 * SFTP服务类
 * 提供SFTP连接、文件上传等功能
 */
@Service
public class SftpService {

    private static final Logger logger = LoggerFactory.getLogger(SftpService.class);

    @Autowired
    private SftpProperties sftpProperties;

    @Autowired
    private LocalFileProperties localFileProperties;

    /**
     * 上传本地文件到SFTP服务器
     *
     * @return 上传是否成功
     */
    public boolean uploadFile() {
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            // 1. 获取本地文件
            File localFile = new File(localFileProperties.getPath());
            if (!localFile.exists()) {
                logger.error("本地文件不存在: {}", localFile.getAbsolutePath());
                return false;
            }

            String fileName = localFile.getName();
            logger.info("准备上传文件: {}, 文件编码: {}", localFile.getAbsolutePath(), localFileProperties.getEncoding());

            // 2. 创建JSch对象
            JSch jsch = new JSch();

            // 3. 创建会话
            session = jsch.getSession(
                    sftpProperties.getUsername(),
                    sftpProperties.getHost(),
                    sftpProperties.getPort()
            );
            session.setPassword(sftpProperties.getPassword());

            // 4. 配置会话参数
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password");
            session.setConfig(config);
            session.setTimeout(sftpProperties.getTimeout());

            // 5. 连接会话
            logger.info("正在连接SFTP服务器: {}:{}, 编码: {}",
                    sftpProperties.getHost(), sftpProperties.getPort(), sftpProperties.getServerEncoding());
            session.connect();
            logger.info("SFTP会话连接成功");

            // 6. 打开SFTP通道
            Channel channel = session.openChannel("sftp");
            channelSftp = (ChannelSftp) channel;
            
            // 7. 设置远程文件编码（需要在connect之前设置）
            boolean encodingSet = false;
            try {
                channelSftp.setFilenameEncoding(sftpProperties.getServerEncoding());
                encodingSet = true;
                logger.info("SFTP文件编码设置成功: {}", sftpProperties.getServerEncoding());
            } catch (Exception e) {
                logger.warn("无法设置SFTP文件编码: {}，将使用手动字节转换", e.getMessage());
            }
            
            // 8. 连接通道
            channel.connect();
            logger.info("SFTP通道连接成功");

            // 9. 确保远程目录存在
            String remoteDir = sftpProperties.getRemoteDir();
            createRemoteDirectoryIfNotExists(channelSftp, remoteDir, encodingSet);

            // 10. 切换到远程目录
            String encodedRemoteDir = encodingSet ? remoteDir : encodeFilename(remoteDir);
            channelSftp.cd(encodedRemoteDir);
            logger.info("切换到远程目录: {}", remoteDir);

            // 11. 上传文件
            String remoteFileName = encodingSet ? fileName : encodeFilename(fileName);

            logger.info("开始上传文件 '{}' 到远程目录 '{}'", fileName, remoteDir);

            try (InputStream inputStream = new FileInputStream(localFile)) {
                channelSftp.put(inputStream, remoteFileName, ChannelSftp.OVERWRITE);
            }

            logger.info("文件上传成功: {} -> {}/{}", localFile.getAbsolutePath(), remoteDir, fileName);
            return true;

        } catch (JSchException e) {
            logger.error("SFTP连接失败: {}", e.getMessage(), e);
            return false;
        } catch (SftpException e) {
            logger.error("SFTP操作失败: {}, 错误码: {}", e.getMessage(), e.id, e);
            return false;
        } catch (UnsupportedEncodingException e) {
            logger.error("编码不支持: {}", e.getMessage(), e);
            return false;
        } catch (IOException e) {
            logger.error("文件读取失败: {}", e.getMessage(), e);
            return false;
        } finally {
            // 12. 关闭连接
            closeConnection(channelSftp, session);
        }
    }

    /**
     * 编码文件名/路径
     * 将Java字符串（Unicode）按服务器编码（GBK）转换为字节，
     * 再用ISO-8859-1包装（保持字节不变），确保SFTP传输时字节序列正确
     *
     * @param filename 原始文件名
     * @return 编码后的文件名
     * @throws UnsupportedEncodingException
     */
    private String encodeFilename(String filename) throws UnsupportedEncodingException {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }
        // 将字符串按GBK编码转为字节数组，再用ISO-8859-1包装为字符串
        // ISO-8859-1是单字节编码，不会改变字节值
        return new String(filename.getBytes(sftpProperties.getServerEncoding()), "ISO-8859-1");
    }

    /**
     * 若远程目录不存在，则创建目录（支持多级目录）
     *
     * @param channelSftp SFTP通道
     * @param remoteDir   远程目录路径
     * @param encodingSet 是否已通过setFilenameEncoding设置编码
     * @throws SftpException
     */
    private void createRemoteDirectoryIfNotExists(ChannelSftp channelSftp, String remoteDir, boolean encodingSet) throws SftpException {
        if (remoteDir == null || remoteDir.isEmpty() || "/".equals(remoteDir)) {
            return;
        }

        try {
            // 尝试切换目录，若目录存在不会抛出异常
            String encodedDir = encodingSet ? remoteDir : encodeFilename(remoteDir);
            channelSftp.cd(encodedDir);
            logger.debug("远程目录已存在: {}", remoteDir);
        } catch (SftpException e) {
            // 目录不存在，需要创建
            logger.info("远程目录不存在，准备创建: {}", remoteDir);

            // 递归创建父目录
            String parentDir = remoteDir.substring(0, remoteDir.lastIndexOf('/'));
            if (parentDir.length() > 0 && !"/".equals(parentDir)) {
                createRemoteDirectoryIfNotExists(channelSftp, parentDir, encodingSet);
            }

            // 创建当前目录
            try {
                String encodedDir = encodingSet ? remoteDir : encodeFilename(remoteDir);
                channelSftp.mkdir(encodedDir);
                logger.info("远程目录创建成功: {}", remoteDir);
            } catch (SftpException mkdirException) {
                // 可能是并发或线程已创建，可以忽略
                logger.debug("创建目录时发生异常（可能已存在）: {}", mkdirException.getMessage());
            } catch (UnsupportedEncodingException ue) {
                logger.error("目录编码失败: {}", ue.getMessage());
            }
        }
    }

    /**
     * 关闭SFTP连接
     *
     * @param channelSftp SFTP通道
     * @param session     会话
     */
    private void closeConnection(ChannelSftp channelSftp, Session session) {
        if (channelSftp != null && channelSftp.isConnected()) {
            channelSftp.disconnect();
            logger.debug("SFTP通道已关闭");
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
            logger.debug("SFTP会话已关闭");
        }
    }

    /**
     * 测试SFTP连接
     *
     * @return 连接是否成功
     */
    public boolean testConnection() {
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(
                    sftpProperties.getUsername(),
                    sftpProperties.getHost(),
                    sftpProperties.getPort()
            );
            session.setPassword(sftpProperties.getPassword());

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(sftpProperties.getTimeout());

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            logger.info("SFTP连接测试成功: {}:{}", sftpProperties.getHost(), sftpProperties.getPort());
            return true;

        } catch (Exception e) {
            logger.error("SFTP连接测试失败: {}", e.getMessage(), e);
            return false;
        } finally {
            closeConnection(channelSftp, session);
        }
    }
}
