# VERSION 1.0.0
# Author: robin
# 打包jar采用maven镜像
FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD
MAINTAINER robin <axuplus@gmail.com>
# 工作目录在镜像的 /build 目录下
WORKDIR /build/
# 把本地的 pom.xml 和 src目录 复制到镜像的/build目录下
COPY pom.xml /build/
COPY src /build/src/
# 执行maven打包
RUN mvn package
# 运行jar采用jdk基础镜像
FROM openjdk:8-jdk-alpine
# 设置工作目录在镜像的 /app 目录下
WORKDIR /app
# 将jar包添加到容器中并更名为app.jar
COPY --from=MAVEN_BUILD /build/target/campus-organization-1.0.0-SNAPSHOT.jar /app/
# 运行jar包
ENTRYPOINT  ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
