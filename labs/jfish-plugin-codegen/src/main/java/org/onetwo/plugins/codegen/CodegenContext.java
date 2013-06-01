package org.onetwo.plugins.codegen;

import org.onetwo.plugins.codegen.generator.DefaultDataSourceFactory;
import org.onetwo.plugins.codegen.generator.DefaultTableComponentFacotry;
import org.onetwo.plugins.codegen.generator.DefaultTableManagerFactory;
import org.onetwo.plugins.codegen.generator.FreemarkerTemplate;
import org.onetwo.plugins.codegen.generator.TableComponentFacotry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses=CodegenPlugin.class)
public class CodegenContext {
	
	public CodegenContext(){
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
	public FreemarkerTemplate freemarkerTemplate(){
		FreemarkerTemplate ft = new FreemarkerTemplate();
		return ft;
	}
	
}
