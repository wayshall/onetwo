package org.onetwo.boot.mq;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.onetwo.boot.mq.interceptor.SendMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor.InterceptorPredicate;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.google.common.collect.ImmutableList;

/**
 * 可拦截的消息发送器
 * 统一包装发送逻辑
 * @author wayshall
 * <br/>
 */
public class InterceptableMessageSender<R> {
	
	private List<SendMessageInterceptor> sendMessageInterceptors;
	

	public InterceptableMessageSender(List<SendMessageInterceptor> sendMessageInterceptors) {
		super();
		sendMessageInterceptors = sendMessageInterceptors==null?Collections.emptyList():sendMessageInterceptors;
		AnnotationAwareOrderComparator.sort(sendMessageInterceptors);
		this.sendMessageInterceptors = ImmutableList.copyOf(sendMessageInterceptors);
	}

	public R sendIntercetableMessage(final InterceptorPredicate interPredicate, Function<List<SendMessageInterceptor>, R> sendAction){
		
		final List<SendMessageInterceptor> messageInterceptors = this.sendMessageInterceptors;
		/*
		 * 去掉InterceptorPredicate接口的getIncreasingInterceptors方法实现，这个东西不太好
		InterceptorPredicate interceptorPredicate = interPredicate==null?SendMessageFlags.Default:interPredicate;
		List<SendMessageInterceptor> messageInterceptors = Lists.newArrayList(sendMessageInterceptors);
		List<SendMessageInterceptor> increasingInters = interceptorPredicate.getIncreasingInterceptors();
		if(!increasingInters.isEmpty()){
			messageInterceptors.addAll(interceptorPredicate.getIncreasingInterceptors());
			AnnotationAwareOrderComparator.sort(messageInterceptors);
		}*/
		
		return sendAction.apply(messageInterceptors);
	}
}
