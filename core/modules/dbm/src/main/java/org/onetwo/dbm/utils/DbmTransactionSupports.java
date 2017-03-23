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
	/****
	 * 如果不是通过spring自带的实现了TransactionAspectSupport的拦截器管理实务，就会获取不到
	 * @return
	 * @throws NoTransactionException
	 */
	public static TransactionInfo currentTransactionInfo() throws NoTransactionException {
		return TransactionAspectSupport.currentTransactionInfo();
	}
}
