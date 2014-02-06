package org.onetwo.plugins.jasper;

import org.onetwo.plugins.jasper.web.JasperReslover;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JasperMvcContext {
	
	@Bean
	public JasperReslover jasperReslover(){
		JasperReslover jasper = new JasperReslover();
		return jasper;
	}
	
	
}
