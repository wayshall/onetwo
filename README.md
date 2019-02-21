# onetwo（zifish）
------
基于spring boot的快速开发框架   
spring-boot技术交流群：  604158262



## 环境要求
JDK8+


## maven   
当前snapshot版本：**4.7.2-SNAPSHOT**   

若使用snapshot版本，请添加snapshotRepository仓储：   
```xml   
<repository>
     <id>oss</id>
     <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>   
```   

## 示例项目   
基于zifish的web示例项目：
[web-sample](https://github.com/wayshall/onetwo-web-sample)  
基于zifish的后台管理示例项目：
[web-manager](https://github.com/wayshall/onetwo-web-manager)   


## [common模块](https://github.com/wayshall/onetwo/tree/master/core/modules/common)
通用模块，一些工具类封装。

## [spring模块](https://github.com/wayshall/onetwo/tree/master/core/modules/spring)
依赖到spring相关的封装
   

## [内嵌tomcat模块](https://github.com/wayshall/onetwo/tree/master/core/modules/tomcat)
简单的内嵌tomcat容器实现

## [poi模块](https://github.com/wayshall/onetwo/tree/master/core/modules/poi)
基于poi，对操作excel的简单封装。
   
## [dbm模块](https://github.com/wayshall/onetwo/tree/master/core/modules/dbm)

基于spring jdbc实现的简单orm   

单独使用dbm的示例项目：
[boot-dbm-sample](https://github.com/wayshall/boot-dbm-sample)

## [security模块](https://github.com/wayshall/onetwo/tree/master/core/modules/security)
基于spring security实现的动态 RBAC 权限管理  
   

## [boot模块](https://github.com/wayshall/onetwo/tree/master/core/modules/boot)
boot模块：
- 基于boot之上封装插件机制
- freemarker增加插件路径分派支持
- 封装第三方库使用
   

## [zifish-plugins插件项目](https://github.com/wayshall/zifish-plugins)
包含了基于本框架开发的插件。
- web-admin插件：使用jquery-easyui编写的简单后台管理，包含了基本的权限管理和菜单功能。
- swaggerext插件


## 捐赠
如果你觉得这个项目帮到了你，请用支付宝打赏一杯咖啡吧~~~   

![支付宝](doc/alipay2.jpg) 