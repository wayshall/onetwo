package org.onetwo.ext.ons.transaction;

import java.util.Collection;

import org.onetwo.ext.ons.producer.SendMessageContext;

/**
 * @author wayshall
 * <br/>
 */
public interface SendMessageRepository {

	void save(SendMessageContext ctx);

	/*Set<SendMessageContext> findCurrentSendMessageContext();

	void clearCurrentContexts();*/

	void remove(Collection<SendMessageContext> msgCtxs);

}