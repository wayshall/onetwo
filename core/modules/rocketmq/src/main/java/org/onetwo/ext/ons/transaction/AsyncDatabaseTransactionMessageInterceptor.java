package org.onetwo.ext.ons.transaction;

import org.onetwo.boot.core.web.async.AsyncTaskDelegateService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wayshall
 * <br/>
 */
public class AsyncDatabaseTransactionMessageInterceptor extends DefaultDatabaseTransactionMessageInterceptor {
	@Autowired
	private AsyncTaskDelegateService delegateService;

	@Override
	public void afterCommit(SendMessageEvent event){
		delegateService.run(()->super.afterCommit(event));
	}
	
	@Override
	public void afterRollback(SendMessageEvent event){
		delegateService.run(()->super.afterRollback(event));
	}
}
