## 2.0.11
1. 支持中国移动磐维数据库
## 2.0.10
1. 处理按照配置类型（格式）查询
2. 处理低版本没有encrypted_data_key字段的问题
## 2.0.9
1. 处理数据库兼容模式
## 2.0.8
1. 代码整理，简化 *Mapper 类
2. 避免dialect等重复创建，重复打印日志
3. 对介质瘦身
4. 默认的 ddl identifier quote mode 修改为 unquoted
## 2.0.7
1. 支持禁用nacos默认插件derby, mysql
2. 提供derby 的sql schema
3. 支持derby数据库
## 2.0.6
1. 解决OpenGauss中将`''`处理成NULL的情况
## 2.0.5
1. 完善版本比较机制，支持自行构建在版本。
## 2.0.4
1. 解决nacos中某些sql带有 \` 引号， 给其它数据库带来SQL语法错误的问题
## 2.0.3
1. 完善对oracle数据库的支持，重点针处理 oracle 会把 `''` 作为 `null` 处理。
## 2.0.2
1. 兼容nacos中的不合理SQL，对应nacos [github上的bug](https://github.com/alibaba/nacos/issues/12598)
2. 完善 oracle 的sql 脚本

## 2.0.1
1. derby, mysql 仍然使用官方的
2. 兼容nacos中的不合理设计，对于部分sql不使用?替换（nacos设计有问题），对应nacos [github上的bug](https://github.com/alibaba/nacos/issues/12585)

## 2.0.0
1. 代码重构，用一套代码即可实现数据库的支持

## 1.0.0
+ 新特性：
  + 提供nacos 数据源插件， nacos-plugin-datasource， 支持多种数据库：
    + DM (达梦)
    + Kingbase （金仓）
    + MagicData
    + MSSQL (SQLServer)
    + OpenGauss （高斯）
    + Oracle(8, 9, 10, 11, 12 ... )
    + PostgreSQL