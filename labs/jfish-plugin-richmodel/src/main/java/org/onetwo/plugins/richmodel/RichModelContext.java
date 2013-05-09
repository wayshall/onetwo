package org.onetwo.plugins.richmodel;

import org.onetwo.common.fish.spring.JFishDaoImplementor;
import org.onetwo.common.fish.spring.config.JFishConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=RichModelPlugin.class)
public class RichModelContext implements JFishConfigurer{
	
	@Bean
	public RichModelMappedEntryListener richModelMappedEntryListener(){
		RichModelMappedEntryListener rich = new RichModelMappedEntryListener();
		return rich;
	}
	

	@Override
	public RichModelEntityManager jfishEntityManager(JFishDaoImplementor jfishDao) {
		JFishRichModelEntityManagerImpl richEm = new JFishRichModelEntityManagerImpl();
		richEm.setJfishDao(jfishDao);
		return richEm;
	}
	
}
