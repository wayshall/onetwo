package org.onetwo.ext.alimq;

import lombok.Builder;
import lombok.Data;

import org.onetwo.ext.ons.producer.TraceableProducer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;


/**
 * @author wayshall
 * <br/>
 */
public interface ProducerListener {

	void beforeSendMessage(SendMessageContext ctx);
	void afterSendMessage(SendMessageContext ctx, SendResult sendResult);
	void onSendMessageError(SendMessageContext ctx, Throwable throable);


	@Data
	@Builder
	public class SendMessageContext {
		final private Message message;
		final private TraceableProducer source;
		final private Producer producer;
		final private TransactionProducer transactionProducer;
	}
}
