package org.onetwo.common.spring.mcache;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.onetwo.common.date.DateUtil;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;

public class CacheElement {
	
	public static CacheElement create(CacheModel cacheModel, Object value){
		Assert.notNull(cacheModel);
		return create(cacheModel.getKey(), value, cacheModel.getExpire());
	}
	
	public static CacheElement create(Serializable key, Object value, int expire){
		Assert.notNull(key);
		CacheElement element = new CacheElement();
		element.key = key;
		element.expire = expire;
		element.value = value;
		return element;
	}

//	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	
	protected Serializable key;
	/*****
	 * SECONDS
	 * 多少秒后过期
	 * 默认永远有效
	 */
	protected int expire;
	
	private Object value;
	private long createAt;
	private Date createAtTime;
	
	private CacheElement(){
		this.createAt = System.nanoTime();
		this.createAtTime = new Date();
	}
	
	/*******
	 * 是否有效
	 * @return
	 */
	public boolean isIndate(){
		if(expire<=0)
			return true;
		long now = System.nanoTime();
		long interval = now - createAt;
		interval = TimeUnit.NANOSECONDS.toSeconds(interval);
		return (interval<=expire);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Serializable getKey() {
		return key;
	}

	public int getExpire() {
		return expire;
	}
	
	public String toString(){
		return LangUtils.append("{key:", key, ", expire time(second):", expire, ", createAt:", DateUtil.formatDateTime(createAtTime),"}");
	}

}
