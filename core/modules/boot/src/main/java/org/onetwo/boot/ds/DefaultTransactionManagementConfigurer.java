package org.onetwo.boot.ds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * @author weishao zeng
 * <br/>
 */
public class DefaultTransactionManagementConfigurer implements TransactionManagementConfigurer {

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return transactionManager;
	}

}
