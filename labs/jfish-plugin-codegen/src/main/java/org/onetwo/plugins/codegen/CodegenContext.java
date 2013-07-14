package org.onetwo.plugins.codegen;

import org.onetwo.plugins.codegen.controller.CodegenController;
import org.onetwo.plugins.codegen.generator.FreemarkerTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
@ComponentScan(basePackageClasses=CodegenController.class)
public class CodegenContext {

	
	public CodegenContext(){
	}


	@Bean 
	public DatabaseInterceptor databaseInterceptor(){
		return new DatabaseInterceptor();
	}
	@Bean 
	public MappedInterceptor mappedDatabaseInterceptor(){
		return new MappedInterceptor(new String[]{"/codegen/**"}, new String[]{"/codegen/init"}, databaseInterceptor());
	}


	@Bean
	public FreemarkerTemplate freemarkerTemplate(){
		FreemarkerTemplate ft = new FreemarkerTemplate();
		return ft;
	}
	
}
