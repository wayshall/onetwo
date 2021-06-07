package org.onetwo.boot.module.redis;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class CacheData<T> {
	public static <D> CacheDataBuilder<D> builder() {
		return new CacheDataBuilder<D>();
	}
	
	public static class CacheDataBuilder<D> {
		private D data;
		private Long expire;
		private TimeUnit timeUnit = TimeUnit.SECONDS;
		private Date expireAt;
		public CacheDataBuilder<D> value(D data) {
			this.data = data;
			return this;
		}
		public CacheDataBuilder<D> expire(Long expire) {
			this.expire = expire;
			return this;
		}
		public CacheDataBuilder<D> timeUnit(TimeUnit timeUnit) {
			this.timeUnit = timeUnit;
			return this;
		}
		
		public CacheData<D> build() {
			return new CacheData<D>(data, expire, timeUnit, expireAt);
		}
		public CacheDataBuilder<D> expireAt(Date expireAt) {
			this.expireAt = expireAt;
			return this;
		}
	}
	
	private T value;
	private Long expire;
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	private Date expireAt;
	
	public CacheData(T value, Long expire, TimeUnit timeUnit, Date expireAt) {
		super();
		this.value = value;
		this.expire = expire;
		this.timeUnit = timeUnit;
		this.expireAt = expireAt;
	}
	
}

