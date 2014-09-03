package org.onetwo.common.web.asyn2;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

abstract public class BlockQueueMessageHolder extends AsynMessageHolder {
	
	private BlockingQueue<SimpleMessage> messages = new ArrayBlockingQueue<SimpleMessage>(100);


	/*public void addMessage(SimpleMessage msg){
		if(msg==null)
			return;
		try {
			this.messages.add(msg);
			this.triggerCounters(msg);
		} catch (Exception e) {
			logger.error("can not add msg to queue, ignore["+msg.getDetail()+"] : " + e.getMessage());
//			e.printStackTrace();
		}
	}*/


	@Override
	protected Collection<SimpleMessage> getMessages() {
		return messages;
	}

	
}
