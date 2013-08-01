package org.onetwo.common.hibernate;

import org.hibernate.cfg.ImprovedNamingStrategy;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

public class ExtLocalSessionFactoryBean extends LocalSessionFactoryBean {

	public ExtLocalSessionFactoryBean(){
		this.setNamingStrategy(new ImprovedNamingStrategy());
		this.setEntityInterceptor(new TimestampInterceptor());
	}
	
}
