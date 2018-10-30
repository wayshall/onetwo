package org.onetwo.ext.alimq;

import java.util.Date;

import lombok.Data;

/**
 * who when do what on target
 * @author weishao zeng
 * <br/>
 */
@Data
public class BaseDomainEvent {
	
	private String userId;
	/***
	 * 事件名称
	 */
	private DomainEvent event;
	/***
	 * 聚合id
	 */
	private String dataId;
	/***
	 * 产生的时间
	 */
	private Date occurOn = new Date();
	
	public static interface DomainEvent {
		String getName();
	}
	
	final public String toKey() {
		String key = getEvent().getName() + "." + getUserId() + "." + getDataId() + "." + Long.toString(getOccurOn().getTime(), 36);
		return key;
	}
}

