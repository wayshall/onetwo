# dbm
------
基于spring jdbc实现的简单orm

   
##使用
如果是基于onetwo本框架的使用，已利用boot的autoconfig功能自动集成，无需任何配置。  
如果是非onetwo的项目，只需要在spring配置类（即有@Configuration注解的类）上加上注解@EnableJFishDbm，以启用dbm功能。
```java     
  
	@EnableJFishDbm
	public static class TestContextConfig {
	}   
   
```

##BaseEntityManager接口
大多数数据库操作都可以通过BaseEntityManager接口来完成。




