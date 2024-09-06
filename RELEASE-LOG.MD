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