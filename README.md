# nacos-plugins

#### 介绍
针对Nacos提供多种插件支持：
+ nacos-plugin-datasource： 理论上可支持任意关系型数据库，实际已测试过的数据库清单：
  + DM (达梦)
  + Kingbase （金仓）
  + MSSQL (SQLServer)
  + OpenGauss （高斯）
  + Oracle(8, 9, 10, 11, 12 ... )
  + PostgreSQL
  + Derby

#### 软件架构
作为nacos的插件使用

#### 版本映射

|        插件版本        |   构建时使用的nacos版本    |  支持的Nacos版本  | 测试过的nacos版本        |
|:------------------:|:------------------:|:------------:|-------------------------------------------------|
|      `1.0.x`       |       `2.4.1`       | 理论上`>=2.2.0，<=2.4.3` | `2.3.2`<br/>`2.4.1`<br/>`2.4.2` |
| `2.0.0` ~ `2.0.14` |       `2.4.2`       | 理论上`>=2.2.0, <=2.4.3` | `2.3.2`<br/>`2.4.1`<br/>`2.4.2` |
| `2.5.1` (尚未发布) | 2.5.1 | 理论上：`<= 2.5.1` | 2.4.2<br/>2.5.0<br/>2.5.1 |

支持的nacos 版本，从2.2.0开始原因：nacos 2.2.0才支持这一套插件系统，在此之前需要直接修改nacos的源码。


#### 安装教程
1. [nacosplugin-datasource 安装](./nacosplugin-datasource/installation.MD)

#### 使用说明



## Nacos 数据库变更

- 2.2.0 => 2.4.3
  - 无变化
- 2.4.3 => 2.5.0
  - 移除表：config_info_aggr
  - 新增表：config_info_gray 
  - 变更表：his_config_info，新增了3个字段：publish_type、gray_name、ext_info
- 2.5.0 => 2.5.1
  - 移除表：config_info_beta、config_info_tag
- 2.5.1 => 3.0.3 
  - 无变化

