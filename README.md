# retail-management-system

# 开发环境搭建详细指南

## 1. 开发工具选择

1. **集成开发环境（IDE）**
    - 使用 [IntelliJ IDEA](https://www.jetbrains.com/idea/)。
    - 安装后，配置 Java 开发插件以提高开发效率。

---

## 2. 安装 Java 开发工具包（JDK）

1. 下载 [Java SE Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)。
    - 确保选择版本 11 或更高。

2. 安装 JDK 并配置环境变量：
    - 在系统中设置 `JAVA_HOME` 为 JDK 的安装路径。
    - 将 `JAVA_HOME/bin` 添加到 `PATH` 环境变量中。

3. 验证安装：
   ```bash
   java -version
   ```
   输出应显示所安装的 JDK 版本。

---

## 3. 配置构建工具

1. **选择构建工具：**
    - 使用 Maven。

2. **安装 Maven：**
    - 下载 [Maven](https://maven.apache.org/download.cgi)。
    - 解压并配置环境变量 `MAVEN_HOME` 和 `PATH`。
    - 验证安装：
      ```bash
      mvn -version
      ```

---

## 4. 版本控制系统

1. 安装 [Git](https://git-scm.com/):
    - 配置用户信息：
      ```bash
      $ git -v //查看版本
      
      $ git config user.name yunzhi1121    //给当前件配置
      $ git config user.email yunzhi1121@qq.com    /给当前文件配置
      $ git config --global user.name yunzhi1121    /全局配置
      $ git config --global user.email yunzhi1121@qqcom    //全局配置
 
      ```

2. 配置远程仓库：
    - 注册 [GitHub](https://github.com/) 。
    - 生成 SSH 密钥并添加到远程仓库：
      ```bash
      ssh-keygen -t rsa -b 4096 -C "youremail@example.com"
      ```
      将生成的公钥（`~/.ssh/id_rsa.pub`）添加到 GitHub/GitLab 中。

      ```bash
      $ git clone https://github.com/yunzhi1121/retail-management-system
      ```

3. 验证连接：
   ```bash
   ssh -T git@github.com
   ```

---

## 5. 数据库配置

1. **安装数据库系统**
    - 使用 MySQL。

2. **MySQL 安装指南：**
    - 下载 [MySQL](https://dev.mysql.com/downloads/mysql/) 并安装。
    - 设置 root 用户密码。
    - 启动服务并验证连接：
      ```bash
      mysql -u root -p
      ```

3. **创建开发数据库：**
   ```sql
   CREATE DATABASE my_project_db;
   ```

---

## 6. 后端框架设置

1. 初始化 Spring Boot 项目：
    - 使用 [Spring Initializr](https://start.spring.io/)：
        - 选择 Maven/Gradle。
        - 添加依赖：Spring Web、Spring Data JPA、Spring Security、MySQL Driver。

2. 下载并解压项目。

3. 配置 `application.properties` 文件：
   ```properties
   # 数据库配置
   spring.datasource.url=jdbc:mysql://localhost:3306/my_project_db
   spring.datasource.username=root
   spring.datasource.password=yourpassword

   # JPA 配置
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

4. 启动项目：
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 7. 容器化技术（可选）

1. 安装 [Docker](https://www.docker.com/)。
    - 配置镜像加速器（针对国内用户）。

2. 创建 Dockerfile：
   ```dockerfile
   FROM openjdk:11-jdk
   COPY target/my-project.jar app.jar
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```

3. 构建镜像：
   ```bash
   docker build -t my-project:latest .
   ```

4. 启动容器：
   ```bash
   docker run -p 8080:8080 my-project:latest
   ```

---

## 8. 持续集成/持续部署（CI/CD）

1. 使用 GitHub Actions 配置 CI/CD 流程：
    - 创建 `.github/workflows/main.yml`：
      ```yaml
      name: CI/CD Pipeline
 
      on:
        push:
          branches:
            - main
 
      jobs:
        build:
          runs-on: ubuntu-latest
          steps:
            - name: Checkout code
              uses: actions/checkout@v3
 
            - name: Set up JDK 11
              uses: actions/setup-java@v3
              with:
                java-version: '11'
 
            - name: Build with Maven
              run: mvn clean install
 
            - name: Deploy to Docker Hub
              if: github.ref == 'refs/heads/main'
              run: |
                docker build -t my-repo/my-project:latest .
                echo "${{ secrets.DOCKER_PASSWORD }}" | docker login -u "${{ secrets.DOCKER_USERNAME }}" --password-stdin
                docker push my-repo/my-project:latest
      ```

2. 配置 GitHub Secrets：
    - 添加 `DOCKER_USERNAME` 和 `DOCKER_PASSWORD`。

---

## 9. 编写搭建指南

1. **记录所有步骤：**
    - 保存为 Markdown 文档，便于共享。

2. **定期更新：**
    - 根据项目调整，更新配置和依赖信息。

3. **存储位置：**
    - 提交到项目根目录，命名为 `SETUP.md` 或 `README.md`。
