package org.onetwo.ext.alimq;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * who when do what on target
 * @author weishao zeng
 * <br/>
 */
@Data
public class BaseDomainEvent {
	
	/***
	 * 操作用户，，可为null
	 */
	private String userId;
	/***
	 * 事件名称，不能为null
	 */
	@NotNull
	private DomainEvent event;
	/***
	 * 聚合id，一般不为空
	 */
	private String dataId;
	/***
	 * 产生的时间，可为null
	 */
	private Date occurOn;
	
	public static interface DomainEvent {
		String getName();
	}
	
	final public String toKey() {
		Date occurOn = this.occurOn==null?new Date():this.occurOn;
		String key = getEvent().getName() + "." + getUserId() + "." + getDataId() + "." + Long.toString(occurOn.getTime(), 36);
		return key;
	}
}

