package org.onetwo.common.web.asyn2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
abstract public class ListMessageHolder<T> extends AsynMessageHolder<T> {
	
	private List<SimpleMessage<T>> messages = new ArrayList<SimpleMessage<T>>();
    private final ReentrantLock lock = new ReentrantLock(false);


	public void addMessage(SimpleMessage<T> msg){
		if(msg==null)
			return;
		lock.lock();
		try {
			this.messages.add(msg);
			this.totalCount++;
			this.triggerCounters(msg);
		}finally{
			lock.unlock();
		}
	}
	

	@Override
	public SimpleMessage<T>[] getAndClearMessages() {
		SimpleMessage<T>[] simpleMessages = null;
		lock.lock();
		try {
			simpleMessages = messages.toArray(new SimpleMessage[messages.size()]);
			this.clearMessages();
		} finally{
			lock.unlock();
		}
		return simpleMessages;
	}

	public List<SimpleMessage<T>> getMessages() {
		return messages;
	}

	
}
