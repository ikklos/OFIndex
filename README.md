# OFIndex - 多端PDF阅读软件

## 项目概述
OFIndex是一个跨平台的PDF阅读软件，包含网页前端、安卓原生前端和Java Spring后端。支持PDF文件上传、在线阅读、笔记标注、书签管理等功能。

## 技术栈
### 后端
- Java Spring Boot
- MySQL数据库
- JWT认证
- 文件存储系统

### 网页前端
- Vue 3
- Vite构建工具
- Axios网络请求
- Vue Router路由管理

### 安卓前端
- Kotlin语言
- Jetpack Compose UI框架
- Retrofit网络请求
- Coil图片加载
- Gson JSON序列化


## 部署说明
### 后端
1. 安装MySQL数据库
2. 创建ofindex数据库
3. 修改application.properties中的数据库连接信息
4. 运行Spring Boot应用

### 网页前端
1. 安装Node.js
2. 运行`npm install`安装依赖
3. 运行`npm run dev`启动开发服务器

### 安卓端
1. 安装Android Studio
2. 导入项目
3. 连接设备或启动模拟器
4. 运行项目

## 开发环境要求
- JDK 17+
- Node.js 18+
- Android Studio 2023+
- MySQL 8.0+
