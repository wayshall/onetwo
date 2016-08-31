## 声明

这个不是一个开源项目，因为这个项目还达不到开源项目的要求，
包括良好的代码、合理的代码结构、单元测试、承诺和义务、license……

这只是我从工作中抽取的代码，放到网上存盘，所以在此声明：
不对项目的维护做任何承诺，可能会随时重构项目的任何东西，包括包、类及任何公开的接口。


## 代码说明
modules的common模块中有几个utils类直接直接copy了某些apache common代码，
原因是一开始的打算是让common做到无依赖，但一些utils代码又不想全部重写一遍，
所以就直接把代码复制一遍了，后来common模块放弃了做到无依赖的目标，因为感觉很无谓。

modules的ajaxanywhere是直接把开源项目ajaxanywhere的源码fork了进来，
因为ajaxanywhere很久没更新，已经不再维护了，fork了维护方便。



## 构建
本项目基于gradle构建

## 项目结构
本项目包括三部分：modules、plugins和project

### modules：
modules目录是基本模块，一般式针对某些框架或者某些领域的胶水代码和在此之上的扩展。 
比如： 
common是一些常用utils类，配置读取，公共接口的抽取。

orm是基于spring jdbc之上的一个简单orm实现，这个模块的代码已有一段时间没有更新维护。

security是基于spring之上山寨的一个验证和授权模块，和spring security项目没有任何关系。

spring是针对spring框架日常使用的一些封装和基于spring之上的扩展，包含了一个插件机制。 

jfish是基于spring mvc包装的一层框架，里面包含核心启动类和一套web插件机制。 

等等。 

### plugins：
插件目录，这个目录下的所有项目都是基于spring和jfish插件机制下写的插件项目。 

比如： 

plugins/hibernate，是把hibernate集成到jfish框架里的插件。 

plugins/dq，是基于hibernate和jdbc的一个sql查询插件， 
因为我不喜欢mybatis这种写xml里写sql，又在sql嵌入大片xml的丑陋方式，所以直接写了一个分离sql和代码的插件模块。 

等等 

### project：
该目录下是基于框架的一些示例项目。 

