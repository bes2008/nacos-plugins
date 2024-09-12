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

#### 软件架构
作为nacos的插件使用

#### 版本映射

| 插件版本      | 构建时使用的nacos版本 | 支持的Nacos版本 | 测试过的nacos版本 |
| ------------- | :-------------------: | :-------------: | ----------------- |
| 1.0.x         |         2.4.1         |  理论上是 all   | 2.3.2、2.4.1      |
| 2.0.0 - 2.0.5 |         2.4.1         |  理论上是 all   | 2.3.2、2.4.1      |
|               |                       |                 |                   |




#### 安装教程
1. [nacosplugin-datasource 安装](./nacosplugin-datasource/installation.MD)

#### 使用说明


#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
