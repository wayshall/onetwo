package org.onetwo.plugins.jsonrpc.client.core;

import java.util.concurrent.atomic.AtomicLong;

public class SeqIdGenerator implements RequestIdGenerator {
	
	private AtomicLong seq = new AtomicLong(1);

	@Override
	public long generateId() {
		return seq.getAndIncrement();
	}
	
	

}
