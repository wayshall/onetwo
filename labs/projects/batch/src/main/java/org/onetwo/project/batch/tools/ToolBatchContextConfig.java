package org.onetwo.project.batch.tools;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.onetwo.common.spring.config.JFishProfile;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.project.batch.tools.entity.PsamEntity;
import org.onetwo.project.batch.tools.service.PsamReader;
import org.onetwo.project.batch.tools.service.PsamWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@JFishProfile
@ImportResource("classpath:applicationContext.xml")
@EnableBatchProcessing(modular=true)
@ComponentScan(basePackageClasses=ToolMain.class)
public class ToolBatchContextConfig {
	
	@Autowired
	private JobBuilderFactory jobs;
	
	@Autowired
	private StepBuilderFactory steps;
	
	@Autowired
	private SessionFactory sessionFactory;
	
//	@Autowired
//	private ItemReader<PsamEntity> psamReader;
	
	@Bean
	public Job exportPsamJob(){
		return jobs.get("exportPsamJob").start(exportPsamStep()).build();
	}
	
	@Bean
	public Step exportPsamStep(){
		return steps.get("exportPsamStep").<PsamEntity, PsamEntity>chunk(10000)
					.reader(psamReader())
					.writer(psamWriter())
					.build();
	}
	
//	@Bean
	public ItemReader<PsamEntity> psamReader2(){
		HibernateCursorItemReader<PsamEntity> reader = new HibernateCursorItemReader<PsamEntity>();
		Map<String, Object> params = LangUtils.asMap("areaCode", "5500", "startId", 1L, "endId", 10L);
		reader.setQueryString("from PsamEntity e where e.areaCode=:areaCode and id >= :startId and id <= :endId");
		reader.setParameterValues(params);
//		reader.setUseStatelessSession(true);
		reader.setSessionFactory(sessionFactory);
		return reader;
	}

	@Bean
	public ItemReader<PsamEntity> psamReader(){
		PsamReader reader = new PsamReader();
		Map<Object, Object> params = LangUtils.asMap("areaCode", "5500", "id:>=", 30000L, "id:<=", 30099L);
		reader.setParams(params);
		return reader;
	}
	
	@Bean
	public ItemWriter<PsamEntity> psamWriter(){
		PsamWriter writer = new PsamWriter();
		return writer;
	}

}
