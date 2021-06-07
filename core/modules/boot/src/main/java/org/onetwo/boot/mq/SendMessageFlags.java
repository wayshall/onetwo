package org.onetwo.boot.mq;

import java.util.List;

import org.onetwo.boot.mq.interceptor.DatabaseTransactionMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor.InterceptorPredicate;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.LangUtils;

/**
 * @author wayshall
 * <br/>
 */
public enum SendMessageFlags implements InterceptorPredicate {
	/***
	 * 默认
	 */
	Default(){
		@Override
		public boolean isApply(SendMessageInterceptor inter) {
			//即使配置了事务消息，默认也过滤掉，以兼容以前接口
			return DisableDatabaseTransactional.isApply(inter);
		}
	},
	
	DisableDatabaseTransactional(){
		/***
		 * 过滤掉DatabaseTransactionMessageInterceptor拦截器，使之不执行
		 */
		@Override
		public boolean isApply(SendMessageInterceptor inter) {
			return !DatabaseTransactionMessageInterceptor.class.isInstance(inter);
		}
	},
	
	EnableDatabaseTransactional(){
		/***
		 * 显示启用事务时，强制检测是否有DatabaseTransactionMessageInterceptor拦截器
		 */
		@Override
		public boolean isApply(SendMessageInterceptor inter) {
			if(Springs.getInstance().isInitialized()){
				//没有找到数据库拦截器，则表示没有启用，强行抛错
				List<DatabaseTransactionMessageInterceptor> dbis = Springs.getInstance().getBeans(DatabaseTransactionMessageInterceptor.class);
				if(LangUtils.isEmpty(dbis)){
					throw new BaseException(""+DatabaseTransactionMessageInterceptor.class.getSimpleName()+" not found!");
				}
			}
			if (inter instanceof DatabaseTransactionMessageInterceptor) {
				DatabaseTransactionMessageInterceptor inst = (DatabaseTransactionMessageInterceptor) inter;
				return !inst.useBatchMode();
			}
			return true;
		}
	}
	
	/****
	 * 批量模式
	 * 基于线程变量实现，不适用于复杂的事务模式如嵌套事务
	 * 
	 * 显示启用事务时，强制检测是否有DatabaseTransactionMessageInterceptor拦截器
	 
	EnableBatchTransactional(){
		@Override
		public boolean isApply(SendMessageInterceptor inter) {
			if(Springs.getInstance().isInitialized()){
				//没有找到数据库拦截器，则表示没有启用，强行抛错
				Optional<DatabaseTransactionMessageInterceptor> dbi = Springs.getInstance()
																	.getBeans(DatabaseTransactionMessageInterceptor.class)
																	.stream()
																	.filter(i -> i.useBatchMode())
																	.findFirst();
				if (!dbi.isPresent()) {
					throw new BaseException("batch transactional not supported!");
				}
			}
			if (inter instanceof DatabaseTransactionMessageInterceptor) {
				DatabaseTransactionMessageInterceptor inst = (DatabaseTransactionMessageInterceptor) inter;
				return inst.useBatchMode();
			}
			return true;
		}
	}*/
	
	;
	
	private SendMessageFlags(){}
}
