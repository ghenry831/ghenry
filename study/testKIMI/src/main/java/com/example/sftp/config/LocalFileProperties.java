package com.example.sftp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ï¿½ï¿½ï¿½ï¿½ï¿½Ä¼ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
 */
@ConfigurationProperties(prefix = "local.file")
public class LocalFileProperties {

    /**
     * Òªï¿½Ï´ï¿½ï¿½Ä±ï¿½ï¿½ï¿½ï¿½Ä¼ï¿½Â·ï¿½ï¿½
     */
    private String path = "./²âÊÔÎÄ¼þ.txt";

    /**
     * ï¿½ï¿½ï¿½ï¿½ï¿½Ä¼ï¿½ï¿½ï¿½ï¿½ï¿½
     */
    private String encoding = "GBK";

    // Getters and Setters
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String toString() {
        return "LocalFileProperties{" +
                "path='" + path + '\'' +
                ", encoding='" + encoding + '\'' +
                '}';
    }
}
