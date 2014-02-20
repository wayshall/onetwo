package org.onetwo.plugins.jasper;

import org.onetwo.plugins.jasper.web.JasperReslover;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JasperMvcContext implements InitializingBean {
	
//	private DataSource dataSource;
	
//	@Resource
//	private ApplicationContext applicationContext;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		this.dataSource = SpringUtils.getBean(applicationContext, DataSource.class);
	}

	@Bean
	public JasperReslover jasperReslover(){
		JasperReslover jasper = new JasperReslover();
		return jasper;
	}
	
	
}
