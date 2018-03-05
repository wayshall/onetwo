package org.onetwo.ext.ons.producer;

import java.util.List;
import java.util.function.Supplier;

import org.onetwo.boot.mq.SendMessageInterceptor;
import org.onetwo.boot.mq.SendMessageInterceptor.InterceptorPredicate;
import org.onetwo.common.exception.BaseException;
import org.onetwo.ext.ons.ONSUtils;
import org.slf4j.Logger;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;

public class SendMessageInterceptorChain extends org.onetwo.boot.mq.SendMessageInterceptorChain {
//	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	final private Supplier<SendResult> actualInvoker;
	private SendMessageContext sendMessageContext;
	
	public SendMessageInterceptorChain(List<SendMessageInterceptor> interceptors, Supplier<SendResult> actualInvoker, InterceptorPredicate interceptorPredicate) {
		super(interceptors, interceptorPredicate);
		this.actualInvoker = actualInvoker;
	}

	protected SendResult doSendRawMessage(){
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
	
	public SendMessageContext getSendMessageContext() {
		return sendMessageContext;
	}

	void setSendMessageContext(SendMessageContext sendMessageContext) {
		this.sendMessageContext = sendMessageContext;
	}


}
