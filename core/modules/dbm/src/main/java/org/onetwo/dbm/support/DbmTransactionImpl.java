package org.onetwo.dbm.support;

import java.sql.Connection;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

public class DbmTransactionImpl implements DbmTransaction {
	
	final private TransactionStatus status;
	final private PlatformTransactionManager transactionManager;
	final private boolean containerAutoCommit;
	private Connection connection;
	
	public DbmTransactionImpl(PlatformTransactionManager transactionManager, TransactionStatus transactionStatus, boolean containerAutoCommit) {
		this.status = transactionStatus;
		this.transactionManager = transactionManager;
		this.containerAutoCommit = containerAutoCommit;
	}

	@Override
	public void commit() {
		if(containerAutoCommit){
//			throw new UnsupportedOperationException("the transaction that managed by container can not be commoit manual!");
			//ignore
			return ;
		}
		this.transactionManager.commit(status);
	}

	@Override
	public void rollback() {
		if(containerAutoCommit){
//			throw new UnsupportedOperationException("the transaction that managed by container can not be rollback manual!");
			return ;
		}
		this.transactionManager.rollback(status);
	}

	public boolean isContainerAutoCommit() {
		return containerAutoCommit;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public TransactionStatus getStatus() {
		return status;
	}

}
