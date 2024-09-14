## datasource 插件安装
1. 将插件、数据库驱动 放到 `nacos/plugins` 目录
```text
io.github.bes2008.solution.nacosplugins:nacosplugin-all:jar:VERSION           
```

2. 在application.properties中配置数据库

以postgresql 数据库为例，修改 `nacos/conf/application.properties` ：
```properties
# PostgreSQL
spring.sql.init.platform=postgresql
db.num=1
db.url.0=jdbc:postgresql://localhost:5432/db1?currentSchema=nacos
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
db.url.0=jdbc:postgresql://localhost:5432/db1?currentSchema=nacos
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
db.url.0=jdbc:dm://localhost:5236?schema=nacos
db.user.0=SYSDBA
db.password.0=dameng777
db.pool.config.driverClassName=dm.jdbc.driver.DmDriver

```

4. 注意事项
```markdown
1） 要指定数据库忽略大小写
```


## 支持Kingbase
1. jdbc配置
```properties
spring.sql.init.platform=kingbase
db.num=1
db.url.0=jdbc:kingbase8://localhost:5432/theDb?currentSchema=nacos&clientEncoding=utf8
db.user.0=username
db.password.0=thePassword
db.pool.config.driverClassName=com.kingbase8.Driver
```

## 支持magicdata

1. jdbc配置
```properties
spring.sql.init.platform=magicdata
db.num=1
db.url.0=jdbc:magicdata://localhost:3660/theDb?currentSchema=nacos&clientEncoding=utf8
db.user.0=username
db.password.0=thePassword
db.pool.config.driverClassName=com.bes.magicdata.Driver
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
db.url.0=jdbc:sqlserver://localhost:1433;databaseName=nacos;encrypt=false
db.user.0=sa
db.password.0=SA123456Pswd
db.pool.config.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
```   

## 支持MySQL

1. schema初始化
```shell
mysql --password=root_password --host=localhost --port=3306 --user root < create_schema.sql
mysql --password=root_password --host=localhost --port=3306 --user root < create_tables.sql
```
2. JDBC配置
```properties
# MySQL
spring.sql.init.platform=mysql
db.num=1
db.url.0=jdbc:mysql://localhost:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=30000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useServerPrepStmts=true&allowPublicKeyRetrieval=true
db.user.0=root
db.password.0=root_password
db.pool.config.driverClassName=com.mysql.cj.jdbc.Driver
# mysql默认使用nacos 内置的mysql插件，如果要禁用内置mysql插件，可以添加下面的配置
spring.sql.plugin.builtin.enabled=false
```



## 支持Oracle
1. 脚本执行
```shell
# 先用 system 创建 C##NACOS用户
sqlplus system/Oracle123_Pswd@localhost:1521/FREE @./create-schema.sql
# 再用 C##NACOS用户创建表
sqlplus C##NACOS/NACOS_PASSWORD@localhost:1521/FREE @./create-tables.sql
```
2. jdbc配置
```properties

# Oracle
spring.sql.init.platform=oracle
db.num=1
# 在oracle中，schema就是用户名，所以不需要另外指定schema
db.url.0=jdbc:oracle:thin:@//localhost:1521/FREE
db.user.0=C##NACOS
db.password.0=NACOS_PASSWORD
db.pool.config.driverClassName=oracle.jdbc.OracleDriver
```

3. 目前使用Oracle的问题
  + 目前nacos 适配Oracle已完毕，但在启动过程中，nacos.log中会有报错。
    + 问题原因：是 nacos 启动是会判定 config_info_tag, config_info_beta表是否存在。 这个方法是直接定死的使用了limit 1，没有通过插件的方式。
    + 问题归属：这个属于 nacos 的Bug，已在 [github 上提交了相关问题](https://github.com/alibaba/nacos/issues/12569)。
    + 问题版本：<=2.4.1
    + 影响范围：只会影响到dump接口，功能不受影响

## 支持Derby

1. 配置
```properties
# derby 默认使用 nacos 内置的 derby 插件，如果要禁用内置 derby 插件，可以添加下面的配置
spring.sql.plugin.builtin.enabled=false
```
2. 启动
```shell
# 必须以 standalone 模式启动时，且不配置为其它数据库的情况下，才会启用 derby数据库
<NACOS_HOME>/bin/startup.sh -m standalone 
```