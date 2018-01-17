package org.onetwo.ext.ons.producer;

import lombok.Builder;
import lombok.Data;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.TransactionProducerBean;

/**
 * @author wayshall
 * <br/>
 */
@Data
@Builder
public class SendMessageContext {
	
	final private Message message;
	final private TraceableProducer source;
	final private ProducerBean producer;
	final private TransactionProducerBean transactionProducer;
	final private SendMessageInterceptorChain chain;

}
