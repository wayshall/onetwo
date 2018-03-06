package org.onetwo.boot.mq;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.onetwo.boot.mq.SendMessageInterceptor.InterceptorPredicate;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.google.common.collect.Lists;

/**
 * @author wayshall
 * <br/>
 */
public class InterceptableMessageSender<R> {
	
	private List<SendMessageInterceptor> sendMessageInterceptors;
	

	public InterceptableMessageSender(List<SendMessageInterceptor> sendMessageInterceptors) {
		super();
		sendMessageInterceptors = sendMessageInterceptors==null?Collections.emptyList():sendMessageInterceptors;
		AnnotationAwareOrderComparator.sort(sendMessageInterceptors);
		this.sendMessageInterceptors = sendMessageInterceptors;
	}

	public R sendIntercetableMessage(final InterceptorPredicate interPredicate, Function<List<SendMessageInterceptor>, R> sendAction){
		InterceptorPredicate interceptorPredicate = interPredicate==null?SendMessageFlags.Default:interPredicate;
		
		List<SendMessageInterceptor> messageInterceptors = Lists.newArrayList(sendMessageInterceptors);
		List<SendMessageInterceptor> increasingInters = interceptorPredicate.getIncreasingInterceptors();
		if(!increasingInters.isEmpty()){
			messageInterceptors.addAll(interceptorPredicate.getIncreasingInterceptors());
			AnnotationAwareOrderComparator.sort(messageInterceptors);
		}
		
		return sendAction.apply(messageInterceptors);
	}
}
