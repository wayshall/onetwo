package org.onetwo.boot.module.redis;

import java.util.concurrent.atomic.AtomicLong;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class CacheStatis {
	
	final private AtomicLong hitCount = new AtomicLong(0);
	final private AtomicLong missCount = new AtomicLong(0);
	private boolean enabled = false;

	public void addHit(int count) {
		if (enabled) {
			hitCount.addAndGet(count);
		}
	}
	public void addMiss(int count) {
		if (enabled) {
			missCount.addAndGet(count);
		}
	}
}

