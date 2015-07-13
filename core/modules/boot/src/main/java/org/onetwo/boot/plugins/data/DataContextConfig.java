package org.onetwo.boot.plugins.data;

import org.onetwo.boot.plugins.data.service.DictionaryService;
import org.onetwo.boot.plugins.data.web.controller.DictionaryImportController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataContextConfig {
	
	@Bean
	@ConditionalOnBean(DictionaryService.class)
	@ConditionalOnMissingBean(DictionaryImportController.class)
	public DictionaryImportController dictionaryImportController(){
		return new DictionaryImportController();
	}

}
