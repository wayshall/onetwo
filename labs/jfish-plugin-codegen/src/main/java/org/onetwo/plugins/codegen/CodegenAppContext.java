package org.onetwo.plugins.codegen;

import org.onetwo.plugins.codegen.generator.DefaultDataSourceFactory;
import org.onetwo.plugins.codegen.generator.DefaultTableComponentFacotry;
import org.onetwo.plugins.codegen.generator.DefaultTableManagerFactory;
import org.onetwo.plugins.codegen.generator.TableComponentFacotry;
import org.onetwo.plugins.codegen.model.service.TemplateService;
import org.onetwo.plugins.codegen.model.service.impl.DatabaseServiceImpl;
import org.onetwo.plugins.codegen.model.service.impl.FileTemplateServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodegenAppContext {

	
	public CodegenAppContext(){
	}

	@Bean
	public DefaultDataSourceFactory dataSourceFactory(){
		DefaultDataSourceFactory dsf = new DefaultDataSourceFactory();
		return dsf;
	}
	
	@Bean
	public DefaultTableManagerFactory tableManagerFactory(){
		DefaultTableManagerFactory tmf = new DefaultTableManagerFactory();
		return tmf;
	}

	@Bean
	public TableComponentFacotry tableComponentFacotry(){
		TableComponentFacotry tcf = new DefaultTableComponentFacotry();
		return tcf;
	}
	
	@Bean
	public DatabaseServiceImpl databaseServiceImpl(){
		return new DatabaseServiceImpl();
	}

	@Bean
	public TemplateService templateServiceImpl(){
//		return new TemplateServiceImpl();
		return new FileTemplateServiceImpl();
	}
	
}
