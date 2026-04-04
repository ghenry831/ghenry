# AGENTS.md

> 本文件供 AI 编程助手阅读，用于快速了解本项目的基本情况、构建方式和开发约定。

---

## 项目概述

本项目是一个基于 **Spring Boot 2.7.18** 的命令行式应用，核心功能是在应用启动后自动将本地文件通过 **SFTP** 上传到远程服务器。
- 使用 **JSch 0.1.55** 作为 SFTP 客户端库。
- 针对中文环境做了编码适配，默认使用 **GBK** 编码处理本地文件和远程服务器文件名。
- 运行时依赖 Java 8，内置嵌入式 Tomcat（默认端口 8080），但本质上是一个启动即执行上传任务的工具型应用。

---

## 技术栈与关键依赖

| 技术 | 版本/说明 |
|------|-----------|
| Java | 1.8 |
| Spring Boot | 2.7.18 |
| Maven | 构建工具 |
| JSch | 0.1.55（SFTP 客户端） |
| 编码 | GBK（源代码编译编码、本地文件编码、远程服务器文件名编码） |

> **注意**：`pom.xml` 中显式配置了 `<project.build.sourceEncoding>GBK</project.build.sourceEncoding>` 和 `<project.reporting.outputEncoding>GBK</project.reporting.outputEncoding>`，因此源文件和日志报告均采用 GBK 编码。

---

## 项目结构

```
.
├── pom.xml                              # Maven 构建配置
├── README.md                            # 项目说明文档（GBK 编码）
├── test.txt                             # 示例测试文件（GBK 编码内容）
├── AGENTS.md                            # 本文件
├── .vscode/
│   ├── settings.json                    # VS Code 设置（编译失败时继续调试）
│   └── launch.json                      # Spring Boot 调试启动配置
└── src/
    ├── main/
    │   ├── java/com/example/sftp/
    │   │   ├── SftpUploadApplication.java           # 启动类
    │   │   ├── config/
    │   │   │   ├── SftpProperties.java              # SFTP 连接配置属性类（prefix = "sftp"）
    │   │   │   └── LocalFileProperties.java         # 本地文件配置属性类（prefix = "local.file"）
    │   │   ├── service/
    │   │   │   └── SftpService.java                 # 核心服务：连接、上传、目录创建、测试连接
    │   │   └── runner/
    │   │       └── SftpUploadRunner.java            # 启动执行器（实现 ApplicationRunner，自动执行上传）
    │   └── resources/
    │       └── application.yml          # 生产环境配置文件（内含真实 SFTP 凭据）
    └── test/
        ├── java/com/example/sftp/
        │   └── SftpUploadApplicationTests.java      # 单元/集成测试类
        └── resources/
            └── application-test.yml     # 测试环境配置文件（使用 localhost 虚拟凭据）
```

---

## 构建与运行命令

### 1. 编译打包
```bash
mvn clean package
```
打包后会生成可执行 JAR：`target/sftp-upload-1.0.0.jar`

### 2. 直接运行（开发调试）
```bash
mvn spring-boot:run
```

### 3. 运行打包后的 JAR
```bash
java -jar target/sftp-upload-1.0.0.jar
```

### 4. 运行测试
```bash
mvn test
```

---

## 配置说明

生产配置位于 `src/main/resources/application.yml`，测试配置位于 `src/test/resources/application-test.yml`。

### 关键配置项

```yaml
server:
  port: 8080

sftp:
  host: 10.211.55.4          # SFTP 服务器地址（生产配置中写死了真实地址）
  port: 22                   # SFTP 端口
  username: parallels        # 用户名
  password: qwer1234         # 密码（明文存储）
  server-encoding: GBK       # 远程服务器文件名字编码
  timeout: 30000             # 连接超时（毫秒）
  remote-dir: /home/parallels/test   # 远程上传目录

local:
  file:
    path: ./test.txt         # 本地待上传文件路径（支持相对路径和绝对路径）
    encoding: GBK            # 本地文件编码
```

- 若远程目录不存在，`SftpService` 会自动递归创建多级目录。
- 若远程文件已存在，上传时会**覆盖**原文件（使用 `ChannelSftp.OVERWRITE`）。

---

## 代码组织与运行流程

### 运行流程
1. `SftpUploadApplication` 启动 Spring Boot 应用。
2. `SftpUploadRunner`（实现了 `ApplicationRunner`）在应用上下文准备完成后自动执行。
3. `SftpUploadRunner` 先调用 `SftpService.testConnection()` 测试连接。
4. 连接成功后，调用 `SftpService.uploadFile()` 完成文件上传并记录日志。
5. 上传完成后，应用**不会自动退出**（因为嵌入式 Tomcat 保持运行），需要手动停止。

### 核心类说明
- **`SftpService`**：封装了所有 SFTP 操作。
  - 通过 `JSch` 建立 `Session` 和 `ChannelSftp`。
  - 优先使用 `channelSftp.setFilenameEncoding()` 设置编码；若失败，则通过手动字节转换（GBK → ISO-8859-1）保证文件名正确传输。
  - 提供 `uploadFile()` 和 `testConnection()` 两个公共方法。
- **`SftpProperties`** / **`LocalFileProperties`**：使用 `@ConfigurationProperties` 绑定 YAML 配置，字段注入方式使用。

---

## 代码风格与开发约定

- **编码**：所有源文件和文档使用 **GBK** 编码。在读取文件或使用终端工具（如 `cat`、`grep`）时，需注意编码问题，避免中文乱码。
- **注释**：代码注释和日志主要使用**中文**。
- **依赖注入**：现有代码采用**字段注入**（`@Autowired` 直接注解在字段上），未使用构造器注入。
- **日志**：使用 `SLF4J` + `LoggerFactory`，日志级别包括 `info`、`error`、`warn`、`debug`。
- **异常处理**：`SftpService` 对 `JSchException`、`SftpException`、`IOException`、`UnsupportedEncodingException` 做了分类捕获并记录日志，方法返回 `boolean` 表示成功与否，不向上抛出受检异常。

---

## 测试策略

- 测试框架：JUnit 5（Jupiter）+ Spring Boot Test。
- 测试类：`SftpUploadApplicationTests`，使用 `@SpringBootTest` 加载完整 Spring 上下文。
- 现有测试用例：
  1. `contextLoads()`：验证 Spring 上下文能正常启动。
  2. `testConnection()`：调用 `sftpService.testConnection()` 测试 SFTP 连接。
  3. `testUpload()`：调用 `sftpService.uploadFile()` 测试文件上传。
- **注意**：`testConnection()` 和 `testUpload()` 会尝试连接真实的 SFTP 服务器（生产配置）或被 `application-test.yml` 覆盖为本地虚拟地址，具体取决于激活的配置文件。目前 `application-test.yml` 中配置了 `127.0.0.1:22` 和虚拟账号 `test/test`，在没有本地 SFTP 服务的情况下测试会失败。

---

## 安全注意事项

1. **明文密码**：`application.yml` 中 SFTP 密码以明文形式存储（`password: qwer1234`），且为真实凭据。
2. **主机密钥校验关闭**：`SftpService` 中设置了 `StrictHostKeyChecking=no`，这跳过了 SSH 主机密钥验证，存在**中间人攻击（MITM）**风险。
3. **生产凭据硬编码**：生产环境的 `host`、`username`、`password` 直接写死在 `src/main/resources/application.yml` 中，建议在实际部署时通过环境变量或外部配置中心覆盖。
4. **文件覆盖风险**：上传行为是覆盖式（OVERWRITE），不会保留远程旧版本文件。

---

## 部署方式

- 产物为单一可执行 JAR（`sftp-upload-1.0.0.jar`），包含所有依赖，可直接复制到目标环境运行。
- 不需要外置容器，Java 8 环境即可运行。
- 建议部署前修改/覆盖 `application.yml` 中的敏感配置项。
