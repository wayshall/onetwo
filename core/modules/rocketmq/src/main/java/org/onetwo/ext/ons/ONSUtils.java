package org.onetwo.ext.ons;

import org.onetwo.boot.mq.SendMessageInterceptor;
import org.onetwo.boot.mq.SendMessageInterceptor.InterceptorPredicate;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.Springs;
import org.onetwo.ext.ons.transaction.DatabaseTransactionMessageInterceptor;
import org.slf4j.Logger;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageConst;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author wayshall
 * <br/>
 */
final public class ONSUtils {
	public static final String LOGGER_NAME = "org.onetwo.ext.ons.ONSMessageLog";
	
	public static enum SendMessageFlags implements InterceptorPredicate {
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
			@Override
			public boolean isApply(SendMessageInterceptor inter) {
				return !DatabaseTransactionMessageInterceptor.class.isInstance(inter);
			}
		},
		EnableDatabaseTransactional(){
			@Override
			public boolean isApply(SendMessageInterceptor inter) {
				if(Springs.getInstance().isInitialized()){
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
	
	public static Logger getONSLogger(){
		return JFishLoggerFactory.getLogger(LOGGER_NAME);
	}
	
	public static String getMessageId(MessageExt message){
		return message.getProperty(MessageConst.PROPERTY_UNIQ_CLIENT_MESSAGE_ID_KEYIDX);
	}

	public static long getMessageDiff(MessageExt msg){
		try {
			long offset = msg.getQueueOffset();//消息自身的offset
			String maxOffset = msg.getProperty(MessageConst.PROPERTY_MAX_OFFSET);//当前最大的消息offset
			long diff = Long.parseLong(maxOffset)-offset;//消费当前消息时积压了多少消息未消费
			return diff;
		} catch (Exception e) {
			return 0;
		}
	}

}
