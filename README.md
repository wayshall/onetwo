# onetwo（jfish）
------
基于spring boot的快速开发框架   
spring-boot技术交流群：  8060215


##环境要求
JDK8+


##maven   
当前snapshot版本：**4.4.0-SNAPSHOT**   

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

##示例项目   
基于jfish的web示例项目：
[web-sample](https://github.com/wayshall/onetwo-web-sample)  
基于jfish的后台管理示例项目：
[web-manager](https://github.com/wayshall/onetwo-web-manager)   


##[common模块](https://github.com/wayshall/onetwo/tree/master/core/modules/common)
通用模块，一些工具类封装。

##[spring模块](https://github.com/wayshall/onetwo/tree/master/core/modules/spring)
依赖到spring相关的封装
   

##[内嵌tomcat模块](https://github.com/wayshall/onetwo/tree/master/core/modules/tomcat)
简单的内嵌tomcat容器实现

##[poi模块](https://github.com/wayshall/onetwo/tree/master/core/modules/poi)
基于poi，对操作excel的简单封装。
   
##[dbm模块](https://github.com/wayshall/onetwo/tree/master/core/modules/dbm)

基于spring jdbc实现的简单orm   

单独使用dbm的示例项目：
[boot-dbm-sample](https://github.com/wayshall/boot-dbm-sample)

##[security模块](https://github.com/wayshall/onetwo/tree/master/core/modules/security)
基于spring security实现的动态rbac和基于Java代码的权限管理  
   

##[boot模块](https://github.com/wayshall/onetwo/tree/master/core/modules/boot)
boot模块，基于boot和freemarker上封装了一点点的插件机制。
   

##[web-admin插件](https://github.com/wayshall/onetwo/tree/master/core/plugins/web-admin)
基于boot模块的插件机制，使用jquery-easyui编写的简单后台管理，包含了基本的权限管理和菜单功能。
