## datasource 插件安装
1. 将插件、插件依赖、数据库驱动、数据库驱动依赖，放到 nacos plugins目录
```text
# 插件及插件依赖，如下为maven-dependency-plugin输出日志重点部分
[INFO] --- maven-dependency-plugin:2.8:tree (default-cli) @ nacosplugin-datasource ---
[INFO] io.github.bes2008.solution.nacos.plugins:nacosplugin-datasource:jar:1.0.0
[INFO] +- io.github.bes2008.solution.sqlhelper:sqlhelper-dialect:jar:5.0.2:compile
[INFO] |  +- io.github.bes2008.solution.sqlhelper:sqlhelper-common:jar:5.0.2:compile
[INFO] |  +- io.github.bes2008.solution.langx:langx-java-el:jar:5.4.4:compile
[INFO] +- io.github.bes2008.solution.langx:langx-java:jar:5.4.4:compile            
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

