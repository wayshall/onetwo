package org.onetwo.common.web.asyn2;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

abstract public class BlockQueueMessageHolder<T> extends AsynMessageHolder<T> {
	
	private BlockingQueue<SimpleMessage<T>> messages = new ArrayBlockingQueue<SimpleMessage<T>>(100);


	public void addMessage(SimpleMessage<T> msg){
		if(msg==null)
			return;
		try {
			this.messages.add(msg);
		} catch (Exception e) {
			logger.error("can not add msg to queue, ignore["+msg.getDetail()+"] : " + e.getMessage());
		}
	}

	public SimpleMessage<T>[] getAndClearMessages() {
		SimpleMessage<T>[] simpleMessages = messages.toArray(new SimpleMessage[messages.size()]);
		this.clearMessages();
		return simpleMessages;
	}

	@Override
	protected Collection<SimpleMessage<T>> getMessages() {
		return messages;
	}

	
}
