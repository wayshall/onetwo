package org.onetwo.boot.module.jms;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.jms.Message;

import org.onetwo.boot.mq.exception.ConsumeException;
import org.onetwo.boot.mq.exception.MQException;
import org.onetwo.common.exception.MessageOnlyServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

public class DelegateJmsMessageConsumer {
	final Logger logger = JFishLoggerFactory.getLogger(getClass());
	
	@Autowired
	private DelegateJmsMessageConsumer delegateJmsMessageConsumer;
	@Autowired
	private JmsConsumeMessageStoreService jmsConsumeMessageStoreService;
	
	public void processMessage(JmsConsumeContext context) {
//		Object result = null;
		if (context.isDatabaseIdempotent()) {
			delegateJmsMessageConsumer.consumeMessageWithTransactional(context);
		} else {
			consumeMessage(context);
		}
//		return result;
	}
	
	public void consumeMessage(JmsConsumeContext context) {
		Method listenMethod = context.getListenMethod();
		Parameter[] parameters = listenMethod.getParameters();
		if (parameters.length!=1) {
			throw new MQException("Parameter length of IdempotentJmsListener must be 1.");
		}
		
//		MethodParameter methodParamter = new MethodParameter(listenMethod, 0);
//		Class<?> parameterType = methodParamter.getParameterType();
		Class<?> parameterType = listenMethod.getParameterTypes()[0];
		
		Object consumerArg = null;
		if (parameterType==JmsConsumeContext.class) {
			consumerArg = context;
		} else if (parameterType==JmsMessage.class) {
			consumerArg = context.getMessageBody(JmsMessage.class);
		} else if (parameterType==Message.class){
			// Message message
			consumerArg = context.getMessage();
		} else {
			consumerArg = context.getMessageBody(parameterType);
		}

		try {
			ReflectionUtils.invokeMethod(listenMethod, context.getConsumer(), consumerArg);
		} catch (MessageOnlyServiceException e) {
			// 此异常无需回滚
			if (logger.isInfoEnabled()) {
				String msg = buildErrorMessage(context);
				logger.info("jms consumer will not rollback: {}", msg);
			}
//			consumerListenerComposite.onConsumeMessageError(currentConetxt, e);
			// 忽略消费，不再重复消费
//			currentConetxt.markWillSkipConsume();
		} catch (Throwable e) {
			String msg = buildErrorMessage(context);
//			logger.error(msg, e);
//			consumerListenerComposite.onConsumeMessageError(currentConetxt, e);
			ConsumeException consumeEx = new ConsumeException(msg, e);
			throw consumeEx;
		}
	}
	
	
	@Transactional(noRollbackFor = MessageOnlyServiceException.class)
	public void consumeMessageWithTransactional(JmsConsumeContext context) {
		// save consume to database
		jmsConsumeMessageStoreService.save(context);
		this.consumeMessage(context);
	}
	

	protected String buildErrorMessage(JmsConsumeContext context) {
		String msgId = context.getMessageId();
		String msg = "jms-consumer["+context.getClientId()+"] consumed message error. " + 
					"id: " +  msgId + 
//					", key: " + currentConetxt.getMessage().getKeys() +
					", destination: " + context.getDestination();
		return msg;
	}

}
