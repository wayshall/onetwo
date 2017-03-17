package org.onetwo.dbm.support;

import java.sql.Connection;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

public class DbmTransactionImpl implements DbmTransaction {
	
	final private TransactionStatus transactionStatus;
	final private PlatformTransactionManager transactionManager;
	final private boolean containerAutoCommit;
	private Connection connection;
	
	public DbmTransactionImpl(PlatformTransactionManager transactionManager, TransactionStatus transactionStatus, boolean containerAutoCommit) {
		this.transactionStatus = transactionStatus;
		this.transactionManager = transactionManager;
		this.containerAutoCommit = containerAutoCommit;
	}

	@Override
	public void commit() {
		this.transactionManager.commit(transactionStatus);
	}

	@Override
	public void rollback() {
		this.transactionManager.rollback(transactionStatus);
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

}
