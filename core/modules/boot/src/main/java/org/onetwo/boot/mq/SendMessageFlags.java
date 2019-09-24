package org.onetwo.boot.mq;

import org.onetwo.boot.mq.interceptor.DatabaseTransactionMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor.InterceptorPredicate;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.Springs;

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
				DatabaseTransactionMessageInterceptor dbi = Springs.getInstance().getBean(DatabaseTransactionMessageInterceptor.class);
				if(dbi==null){
					throw new BaseException(""+DatabaseTransactionMessageInterceptor.class.getSimpleName()+" not found!");
				}
			}
			return true;
		}
	};
	
	private SendMessageFlags(){}
}
