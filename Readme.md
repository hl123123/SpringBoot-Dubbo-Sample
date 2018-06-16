# SpringBoot-Dubbo-Sample

## 概述
基于Spring Boot、Dubbo、Redis、Mybtis搭建的分布式开发工程样例，已支持MySql、PostgreSQL数据库。

## 部署
可以手工或利用Jenkins等自动化工具进行部署。
在用Maven对各模块进行编译时，在命令行最后加上`-P 参数`，可在编译时引入不同的Spring Boot配置文件，从而对各类环境进行区分，参数包括开发环境`dev`和生产环境`prod`。
