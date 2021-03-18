package org.onetwo.ext.alimq;

import java.util.List;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class BatchConsumContext {
	private List<ConsumContext> contexts;
	private ConsumContext currentContext;
//	private boolean willSkipConsume;
	
	public BatchConsumContext(List<ConsumContext> contexts) {
		super();
		this.contexts = contexts;
	}

//	public boolean isWillSkipConsume() {
//		return willSkipConsume;
//	}
}
