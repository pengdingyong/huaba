# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
mvn clean install -DskipTests                    # Build all
mvn clean install -pl <module> -am               # Build specific module with dependencies
cd web && mvn spring-boot:run                    # Run web module (port 8980)
```

## Architecture

JeeSite v5.16 基于 Spring Boot 3.5 的快速开发平台。

**项目结构**:
- `parent/` - 父POM，定义依赖版本
- `common/` - 公共静态资源
- `modules/` - 业务模块
  - `core/` - 核心模块（系统、用户、权限等）
  - `cms/` - CMS内容管理
  - `ai/` - AI相关模块（ai-cms, ai-tools）
  - `app/` - 应用模块
  - `test/` - 测试模块
- `web/` - Web主入口 (port 8980, context-path: /js)
- `web-ai/` - AI Web模块（web-ai-cms, web-ai-mcp）
- `web-api/` - API接口服务
- `web-fast/` - 快速开发Web
- `web-mini/` - 小程序Web
- `jeesite-vue/` - Vue前端

**URL Convention**: 后台路径 `/a`，前台路径 `/f`，context-path 为 `/js`

## Database

- 支持多数据库: MySQL, Oracle, PostgreSQL, 达梦, 人大金仓, H2
- 表名/字段名: 小写+下划线 (`sys_user`)
- 非自增主键: 使用 `IdUtils.getId()` 生成
- 必需字段: `create_user`, `create_time`, `update_user`, `update_time`, `remark`
- 多租户支持: `tenant_id` (通过 `user.useCorpModel` 开启)

## Code Quality

每次生成或修改代码后自动检查并优化:
- 空指针风险、资源泄漏、异常处理
- SQL注入风险、逻辑错误
- 未关闭的流/连接
- 冗余代码、重复逻辑、可简化的条件判断

## Tech Stack

- Java 17
- Spring Boot 3.5.11
- MyBatis 3.5.19
- Shiro 2.0.6
- Druid 1.2.27
- POI 5.4.1
- FastJSON2 2.0.57
- Quartz 2.5.2

## Common Commands

```bash
# 编译整个项目
mvn clean install -DskipTests

# 编译指定模块
mvn clean install -pl modules/core -am

# 运行Web应用
cd web && mvn spring-boot:run

# 打包部署
mvn clean package -DskipTests -P package
```