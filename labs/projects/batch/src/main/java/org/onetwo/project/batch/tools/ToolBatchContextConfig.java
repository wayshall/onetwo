package org.onetwo.project.batch.tools;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.config.JFishProfile;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.batch.ExcelFileItemReader;
import org.onetwo.project.batch.tools.entity.PsamEntity;
import org.onetwo.project.batch.tools.service.ExportPsamReader;
import org.onetwo.project.batch.tools.service.ExportPsamWriter;
import org.onetwo.project.batch.tools.service.ImportExcelMapper;
import org.onetwo.project.batch.tools.service.ImportPsamWriter;
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
	
	private int psamCount = 10000;
	
//	@Autowired
//	private ItemReader<PsamEntity> psamReader;
	
	@Bean
	public Job exportPsamJob(){
		return jobs.get("exportPsamJob").start(exportPsamStep()).build();
	}
	
	@Bean
	public Step exportPsamStep(){
		return steps.get("exportPsamStep").<PsamEntity, PsamEntity>chunk(psamCount)
					.reader(exportPsamReader())
					.writer(exportPsamWriter())
					.build();
	}
	
	@Bean
	public ItemReader<PsamEntity> exportPsamReader(){
		HibernateCursorItemReader<PsamEntity> reader = new HibernateCursorItemReader<PsamEntity>();
		reader.setFetchSize(100);
		Map<String, Object> params = LangUtils.asMap("areaCode", "5500", "startId", 30000L, "endId", 30099L);
		reader.setQueryString("from PsamEntity e where e.areaCode=:areaCode and id >= :startId and id <= :endId");
		reader.setParameterValues(params);
//		reader.setUseStatelessSession(true);
		reader.setSessionFactory(sessionFactory);
		return reader;
	}

	@Bean
	public ItemReader<PsamEntity> exportPsamReader2(){
		ExportPsamReader reader = new ExportPsamReader();
		Map<Object, Object> params = LangUtils.asMap("areaCode", "5500", "id:>=", 30000L, "id:<=", 30099L);
		reader.setParams(params);
		return reader;
	}
	
	@Bean
	public ItemWriter<PsamEntity> exportPsamWriter(){
		ExportPsamWriter writer = new ExportPsamWriter();
		return writer;
	}
	
	
	@Bean
	public Job importPsamJob(){
		return jobs.get("importPsamJob").start(importPsamStep()).build();
	}
	
	@Bean
	public Step importPsamStep(){
		return steps.get("importPsamStep").<PsamEntity, PsamEntity>chunk(psamCount)
				.reader(importPsamReader())
				.writer(importPsamWriter())
				.build();
	}
	
	@Bean
	public ItemReader<PsamEntity> importPsamReader(){
		ExcelFileItemReader<PsamEntity> reader = new ExcelFileItemReader<PsamEntity>();
		reader.setResource(SpringUtils.classpath("psam_lingnantong.xls"));
		reader.setRowMapper(new ImportExcelMapper());
		return reader;
	}
	
	@Bean
	public ItemWriter<PsamEntity> importPsamWriter(){
		ImportPsamWriter writer = new ImportPsamWriter();
		return writer;
	}
	

}
