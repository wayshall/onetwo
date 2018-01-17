package org.onetwo.ext.ons.transaction;

import java.util.Set;

import org.onetwo.ext.ons.producer.SendMessageContext;

/**
 * @author wayshall
 * <br/>
 */
public interface SendMessageRepository {

	void save(SendMessageContext ctx);

	Set<SendMessageContext> findCurrentSendMessageContext();

	void clearCurrentContexts();

}