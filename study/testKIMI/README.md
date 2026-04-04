# Spring Boot SFTP 文件上传项目

基于 Spring Boot 2.7.18 + JSch 实现的 SFTP 文件上传功能。

## 技术栈

- Spring Boot 2.7.18
- JSch 0.1.55 (SFTP客户端)
- 编码：GBK（本地和远程服务器）

## 项目结构

```
sftp-upload/
├── pom.xml                              # Maven配置文件
├── README.md                            # 项目说明
├── test.txt                             # 测试文件（示例）
└── src/
    ├── main/
    │   ├── java/com/example/sftp/
    │   │   ├── SftpUploadApplication.java           # 启动类
    │   │   ├── config/
    │   │   │   ├── SftpProperties.java              # SFTP配置属性
    │   │   │   └── LocalFileProperties.java         # 本地文件配置属性
    │   │   ├── service/
    │   │   │   └── SftpService.java                 # SFTP服务类
    │   │   └── runner/
    │   │       └── SftpUploadRunner.java            # 启动执行器
    │   └── resources/
    │       └── application.yml          # 应用配置文件
    └── test/
        ├── java/com/example/sftp/
        │   └── SftpUploadApplicationTests.java      # 测试类
        └── resources/
            └── application-test.yml     # 测试配置文件
```

## 配置说明

### application.yml

```yaml
sftp:
  host: 127.0.0.1          # SFTP服务器地址
  port: 22                 # SFTP端口
  username: root           # 用户名
  password: password       # 密码
  server-encoding: GBK     # 远程服务器编码
  timeout: 30000           # 连接超时时间（毫秒）
  remote-dir: /upload      # 远程上传目录

local:
  file:
    path: ./test.txt       # 本地文件路径
    encoding: GBK          # 本地文件编码
```

## 使用说明

### 1. 修改配置

编辑 `src/main/resources/application.yml`，配置SFTP服务器信息和本地文件路径：

```yaml
sftp:
  host: your-sftp-host
  port: 22
  username: your-username
  password: your-password
  remote-dir: /your/remote/path

local:
  file:
    path: /path/to/your/local/file.txt
```

### 2. 打包运行

```bash
# 打包
mvn clean package

# 运行
java -jar target/sftp-upload-1.0.0.jar
```

### 3. 直接运行

```bash
# 使用Maven插件运行
mvn spring-boot:run
```

## 功能特点

1. **自动上传**：应用启动后自动执行SFTP文件上传
2. **GBK编码支持**：文件名和内容均支持GBK编码
3. **目录自动创建**：远程目录不存在时自动创建（支持多级目录）
4. **连接超时控制**：可配置连接超时时间
5. **详细日志**：记录上传过程的详细信息

## 注意事项

1. 确保SFTP服务器已启动并可访问
2. 确保配置的账号有相应的读写权限
3. 远程目录如果不存在会自动创建
4. 如果文件已存在，会覆盖原文件

## 测试

```bash
# 运行单元测试
mvn test
```
