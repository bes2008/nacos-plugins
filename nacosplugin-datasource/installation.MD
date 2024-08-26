## datasource 插件安装
1. 将插件、数据库驱动 放到 `nacos/plugins` 目录
```text
io.github.bes2008.solution.nacosplugins:nacosplugin-all:jar:1.0.0           
```

2. 在application.properties中配置数据库

以postgresql 数据库为例，修改 `nacos/conf/application.properties` ：
```properties
# PostgreSQL
spring.sql.init.platform=postgresql
db.num=1
db.url.0=jdbc:postgresql://192.168.188.131:5432/db1?currentSchema=nacos
db.user.0=postgresql
db.password.0=postgresql
db.pool.config.driverClassName=org.postgresql.Driver
```

3. 初始数据库schema

使用DBeaver 等工具，连接到数据库，执行相关schema初始化。脚本位置：
```text
nacosplugin-datasource-sqlscripts/resources/sql/ 目录下
```
4. 启动nacos访问

---

## PostgreSQL 测试时，注意事项
1. postgresql 相关依赖要放全，且要与服务端兼容，建议 >= 42.2.0 版本。
2. 使用Oracle JDK 8，使用 OpenJDK 8可能会遇到 PostgreSQL SCRAM 认证相关问题
3. 驱动[下载地址](https://mvnrepository.com/artifact/org.postgresql/postgresql)

