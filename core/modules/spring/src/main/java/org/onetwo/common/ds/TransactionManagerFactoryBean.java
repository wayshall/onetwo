package org.onetwo.common.ds;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author weishao zeng
 * <br/>
 */
public class TransactionManagerFactoryBean implements FactoryBean<PlatformTransactionManager> {
	
	private TransactionManagerAwareDataSource dataSource;
	
	public TransactionManagerFactoryBean(TransactionManagerAwareDataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public PlatformTransactionManager getObject() throws Exception {
		return dataSource.getTransactionManager();
	}

	@Override
	public Class<?> getObjectType() {
		return PlatformTransactionManager.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
