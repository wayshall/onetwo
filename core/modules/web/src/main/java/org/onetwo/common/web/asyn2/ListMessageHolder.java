package org.onetwo.common.web.asyn2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
abstract public class ListMessageHolder extends AsyncMessageHolder {
	
	private List<SimpleMessage> messages = new ArrayList<SimpleMessage>();
    private final ReentrantLock lock = new ReentrantLock(false);


	public void addMessage(SimpleMessage msg){
		if(msg==null)
			return;
		lock.lock();
		try {
			this.messages.add(msg);
			countMessage(msg);
//			this.totalCount++;
//			this.triggerCounters(msg);
		}finally{
			lock.unlock();
		}
	}
	

	@Override
	public SimpleMessage[] getAndClearMessages() {
		SimpleMessage[] simpleMessages = null;
		lock.lock();
		try {
			simpleMessages = messages.toArray(new SimpleMessage[messages.size()]);
			this.clearMessages();
		} finally{
			lock.unlock();
		}
		return simpleMessages;
	}

	public List<SimpleMessage> getMessages() {
		return messages;
	}

	
}
