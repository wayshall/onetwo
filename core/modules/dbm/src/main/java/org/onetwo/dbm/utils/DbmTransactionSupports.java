package org.onetwo.dbm.utils;

import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

abstract public class DbmTransactionSupports extends TransactionAspectSupport {

	public static PlatformTransactionManager currentPlatformTransactionManager() throws NoTransactionException {
		TransactionInfo info = currentTransactionInfo();
		if(info==null){
			return null;
		}
		return info.getTransactionManager();
	}
	public static TransactionInfo currentTransactionInfo() throws NoTransactionException {
		return TransactionAspectSupport.currentTransactionInfo();
	}
}
