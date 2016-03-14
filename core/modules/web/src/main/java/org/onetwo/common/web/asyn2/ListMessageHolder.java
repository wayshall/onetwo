package org.onetwo.common.web.asyn2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
@SuppressWarnings("serial")
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
	public List<SimpleMessage> getAndClearMessages() {
		List<SimpleMessage> simpleMessages = null;
		lock.lock();
		try {
			simpleMessages = new ArrayList<SimpleMessage>(messages);
			this.clearMessages();
		} finally{
			lock.unlock();
		}
		return simpleMessages;
	}

	protected List<SimpleMessage> getMessages() {
		return messages;
	}

	
}
