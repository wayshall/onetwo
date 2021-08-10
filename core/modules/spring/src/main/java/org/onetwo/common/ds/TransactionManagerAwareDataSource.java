package org.onetwo.common.ds;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author weishao zeng
 * <br/>
 */
public class TransactionManagerAwareDataSource extends DelegatingDataSource {
	
	private PlatformTransactionManager transactionManager;

	

	public TransactionManagerAwareDataSource(DataSource targetDataSource) {
		super(targetDataSource);
		this.transactionManager = new DataSourceTransactionManager(targetDataSource);
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

}
