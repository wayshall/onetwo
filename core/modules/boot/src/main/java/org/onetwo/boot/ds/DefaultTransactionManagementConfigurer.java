package org.onetwo.boot.ds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * 多数据源多事务管理器多情况下，自定义设置默认的PlatformTransactionManager
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
