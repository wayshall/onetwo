package org.onetwo.dbm.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class DbmTransactionSynchronization extends TransactionSynchronizationAdapter {
	
	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	final private DbmSessionResourceHolder sessionHolder;
	
	public DbmTransactionSynchronization(DbmSessionResourceHolder sessionHolder){
		this.sessionHolder = sessionHolder;
	}
	
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public void suspend() {
		TransactionSynchronizationManager.unbindResource(sessionHolder.getSessionFactory());
	}

	@Override
	public void resume() {
		TransactionSynchronizationManager.bindResource(sessionHolder.getSessionFactory(), sessionHolder);
	}

	@Override
	public void beforeCommit(boolean readOnly) {
		if(TransactionSynchronizationManager.isActualTransactionActive()){
			if(logger.isDebugEnabled()){
				logger.debug("spring transaction synchronization committing for dbmSession: {}, and dbmSession flush.", this.sessionHolder.getSession());
			}
			this.sessionHolder.getSession().flush();
		}
	}

	@Override
	public void afterCompletion(int status) {
		if(!this.sessionHolder.isOpen()){
			if(logger.isDebugEnabled()){
				logger.debug("spring transaction synchronization closing for dbmSession: {}, and dbmSession flush.", this.sessionHolder.getSession());
			}
			TransactionSynchronizationManager.unbindResource(this.sessionHolder.getSessionFactory());
			this.sessionHolder.reset();
		}
	}
}
