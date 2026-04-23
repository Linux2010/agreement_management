# Agreement Management - 协议管理后台系统

企业级协议管理系统，基于 Spring Boot 实现 Word 文档模板生成 PDF。

## 项目简介

协议管理系统核心功能：
- Word 文档模板管理
- 根据模板生成 Word 文档
- Word 转 PDF
- 协议审批流程
- 电子签名支持

## 技术栈

- **后端**: Spring Boot + MyBatis
- **前端**: Vue.js
- **文档处理**: Apache POI + PDF转换
- **数据库**: MySQL

## 功能特性

- ✅ 协议模板管理
- ✅ 动态填充文档内容
- ✅ Word 生成与导出
- ✅ PDF 转换
- ✅ 协议状态管理
- ✅ 审批流程配置

## 项目结构

```
agreement_management/
├── src/main/java/
│   ├── controller/    # 控制器
│   ├── service/       # 服务层
│   ├── entity/        # 实体类
│   └── utils/         # 工具类
├── resources/
│   ├── templates/     # Word模板
│   └── application.yml
└── README.md
```

## 快速开始

### 环境要求

- Java 8+
- MySQL 5.7+
- Maven 3.6+

### 配置

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/agreement
    username: root
    password: your_password
```

### 运行

```bash
mvn clean install
mvn spring-boot:run
```

## 核心功能说明

### Word 模板填充

使用 Apache POI 读取 Word 模板，动态替换占位符生成新文档。

### PDF 转换

支持多种 PDF 转换方式：
- LibreOffice 命令行转换
- Aspose PDF 库

## License

MIT License
