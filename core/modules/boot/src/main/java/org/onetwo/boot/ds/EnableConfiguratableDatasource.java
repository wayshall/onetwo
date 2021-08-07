package org.onetwo.boot.ds;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.sql.DataSource;

import org.springframework.context.annotation.Import;

/**
 * 
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({DatasourceRegistrar.class, 
	ConfiguratableDatasourceConfiguration.class, 
//	PrimaryDataSourceTransactionManagerAutoConfiguration.class
	})
public @interface EnableConfiguratableDatasource {
	
	/***
	 * 要扫码的数据源名字，一般为：
	 * jfish: 
	 * 		datasource: 
	 * 			{name}: 
	 * 				driverClassName=com.mysql.jdbc.Driver
					password=root
					url=jdbc\:mysql\://localhost\:3306/jormtest?useUnicode\=true&amp;characterEncoding\=UTF-8
					username=root
	 * @author weishao zeng
	 * @return
	 */
	String[] value();
	
	/****
	 * 默认为："org.apache.tomcat.jdbc.pool.DataSource"
	 * @author weishao zeng
	 * @return
	 */
	Class<?> implementClass() default DataSource.class;
	
//	Class<? extends RestExecutorFactory> restExecutorFactory() default RestExecutorFactory.class;

}
