package org.onetwo.common.spring.dozer;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class DozerBeanFactoryBean implements FactoryBean<DozerBean>, InitializingBean {

	private DozerBean dozerBean;
	private String basePackage;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.dozerBean = DozerFacotry.createDozerBean(null, basePackage);
	}

	@Override
	public DozerBean getObject() throws Exception {
		return dozerBean;
	}

	@Override
	public Class<?> getObjectType() {
		return DozerBean.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

}
