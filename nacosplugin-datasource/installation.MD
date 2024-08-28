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

## 支持PostgreSQL
1. postgresql 相关依赖要放全，且要与服务端兼容，建议 >= 42.2.0 版本。
2. 使用Oracle JDK 8，使用 OpenJDK 8可能会遇到 PostgreSQL SCRAM 认证相关问题
3. 驱动[下载地址](https://mvnrepository.com/artifact/org.postgresql/postgresql)
4. JDBC 配置
```properties
# PostgreSQL
spring.sql.init.platform=postgresql
db.num=1
db.url.0=jdbc:postgresql://192.168.188.131:5432/db1?currentSchema=nacos
db.user.0=postgresql
db.password.0=postgresql
db.pool.config.driverClassName=org.postgresql.Driver
```

## 支持DM
1. jdbc驱动选择：
```markdown
+ 在JDK8上运行时，选择：com.dameng:DmJdbcDriver18:8.1.3.140
+ 在JDK7上运行时，选择：com.dameng:DmJdbcDriver17:8.1.3.140
+ 在JDK6上运行时，选择：com.dameng:DmJdbcDriver16:8.1.3.140
```
2. SQL脚本的执行

使用 DBeaver 连接到达梦数据库上时，一次只能执行一条SQL语句。

想要执行SQL脚本文件，需要提供的命令行工具：${dmdbms}/disql

```shell
./disql SYSDBA/dameng777@localhost:5236 \`./create-schema.sql
./disql SYSDBA/dameng777@localhost:5236 \`./create-tables.sql
```
3. JDBC 配置
```properties

# DM
spring.sql.init.platform=dm
db.num=1
db.url.0=jdbc:dm://192.168.137.133:5236?schema=nacos
db.user.0=SYSDBA
db.password.0=dameng777
db.pool.config.driverClassName=dm.jdbc.driver.DmDriver

```

## 支持SqlServer
1. jdbc 驱动选择：
参考[微软官方说明](https://learn.microsoft.com/zh-cn/sql/connect/jdbc/microsoft-jdbc-driver-for-sql-server-support-matrix?view=sql-server-ver16)，建议选择`12.4.1`版本
2. 使用database而不是schema
```markdown
在SqlServer中，每个Database都会默认创建一堆固定的schema，执行sql时，默认就会使用一个名为 dbo的schema。
虽然也可以通过SQL方式创建 自定义的schema，但无法在session中指定默认schema的，所以不要使用schema，而是使用database。
```

3. jdbc 配置
```properties
# SqlServer
spring.sql.init.platform=sqlserver
db.num=1
db.url.0=jdbc:sqlserver://192.168.137.133:1433;databaseName=nacos;encrypt=false
db.user.0=sa
db.password.0=SA123456Pswd
db.pool.config.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
```   