package org.onetwo.common.web.asyn;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

abstract public class BlockQueueMessageHolder extends AsyncMessageHolder {
	
	private BlockingQueue<SimpleMessage> messages;

	public BlockQueueMessageHolder(){
		this(1000);
	}
			
	public BlockQueueMessageHolder(int size){
		messages = new ArrayBlockingQueue<SimpleMessage>(size);
	}

	@Override
	protected Collection<SimpleMessage> getMessages() {
		return messages;
	}

	
}
