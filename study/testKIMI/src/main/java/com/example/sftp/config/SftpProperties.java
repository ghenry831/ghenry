package com.example.sftp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "sftp")
public class SftpProperties {

  
    private String host;

    
    private int port = 22;

    
    private String username;

    
    private String password;

    
    private String serverEncoding = "GBK";

    
    private int timeout = 30000;

   
    private String remoteDir = "/upload";

    // Getters and Setters
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerEncoding() {
        return serverEncoding;
    }

    public void setServerEncoding(String serverEncoding) {
        this.serverEncoding = serverEncoding;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getRemoteDir() {
        return remoteDir;
    }

    public void setRemoteDir(String remoteDir) {
        this.remoteDir = remoteDir;
    }

    @Override
    public String toString() {
        return "SftpProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", serverEncoding='" + serverEncoding + '\'' +
                ", timeout=" + timeout +
                ", remoteDir='" + remoteDir + '\'' +
                '}';
    }
}
