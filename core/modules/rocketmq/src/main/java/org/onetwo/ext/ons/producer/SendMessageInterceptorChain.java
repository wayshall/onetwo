package org.onetwo.ext.ons.producer;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.producer.SendMessageInterceptor.InterceptorPredicate;
import org.slf4j.Logger;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;

public class SendMessageInterceptorChain {
//	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	final private Supplier<SendResult> actualInvoker;
	
	final private List<SendMessageInterceptor> interceptors;
	final private Iterator<SendMessageInterceptor> iterator;
	private SendMessageContext sendMessageContext;
	final private InterceptorPredicate interceptorPredicate;
	
	private SendResult result;
	private Throwable throwable;
	private boolean debug = true;
	final private Logger logger = ONSUtils.getONSLogger();

	public SendMessageInterceptorChain(List<SendMessageInterceptor> interceptors, Supplier<SendResult> actualInvoker, InterceptorPredicate interceptorPredicate) {
		super();
		this.actualInvoker = actualInvoker;
		this.interceptors = interceptors;
		this.iterator = this.interceptors.iterator();
		this.interceptorPredicate = interceptorPredicate;
	}

	public SendResult invoke(){
		int index = 0;
		if(iterator.hasNext()){
			SendMessageInterceptor interceptor = iterator.next();
			if(interceptorPredicate==null || interceptorPredicate.isApply(interceptor)){
				if(debug && logger.isInfoEnabled()){
					logger.info("{}-> {}", LangUtils.repeatString(index, "--"), interceptor.getClass().getSimpleName());
				}
				result = interceptor.intercept(this);
				index++;
			}else{
				result = this.invoke();
			}
		}else{
//			result = actualInvoker.get();
			result = doSendRawMessage();
		}
		return result;
	}
	
	private SendResult doSendRawMessage(){
		try {
			return actualInvoker.get();
		} catch (ONSClientException e) {
			handleException(e, sendMessageContext.getMessage());
		}catch (Throwable e) {
			handleException(e, sendMessageContext.getMessage());
		}
		return null;
	}

	protected void handleException(Throwable e, Message message){
		final Logger logger = ONSUtils.getONSLogger();
		if(logger.isErrorEnabled()){
			logger.error("send message topic: {}, tags: {}, key: {}", message.getTopic(), message.getTag(), message.getKey());
		}
		
		if(e instanceof ONSClientException){
			throw (ONSClientException)e;
		}else{
			throw new BaseException("发送消息失败", e);
		}
	}
	
	public SendResult getResult() {
		return result;
	}
	
	public Throwable getThrowable() {
		return throwable;
	}

	public SendMessageContext getSendMessageContext() {
		return sendMessageContext;
	}

	void setSendMessageContext(SendMessageContext sendMessageContext) {
		this.sendMessageContext = sendMessageContext;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
