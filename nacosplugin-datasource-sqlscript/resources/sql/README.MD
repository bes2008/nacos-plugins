在DDL语句中，identifier(表名、列名)的要求：
+ 尽量统一不加引号
+ 如果不想统一加引号，按如下规则来处理：
  + 不加引号的identifier，必须使用小写形式 （原因: nacos 中的代码中，将列名、表名都是定死的小写），由数据库内部自行处理。
  + 加引号的identifier，按照数据库对不加引号的identifier的处理方式来处理。
    + 例如：oracle 对不加引号的 identifier 默认全部转换成大写形式。那么在使用引号时，就必须全部使用大写形式。



## 数据库兼容情况

- 与PostgreSQL 兼容
  - PostgreSQL
  - Magicdata
  - OpenGauss
  - PanWei
  - Kingbase
- 与Oracle 兼容
  - DM
  - Oracle
- 与 MySQL 兼容
  - mysql
- 与 MSSQL 兼容
  - mssql

