package org.onetwo.common.utils;
/**
 * 元组辅助类
 * @author weishao zeng
 * <br/>
 */
public class Tuple<K, V> {
	
	public static <K1, V1> Tuple<K1, V1> of(K1 key, V1 val) {
		return new Tuple<>(key, val);
	}

	private final K key;
	private final V value;
	
	private Tuple(K key, V value) {
		Assert.notNull(key, "key can not be null");
		this.key = key;
		this.value = value;
	}
	
	public K getKey() {
		return key;
	}
	
	public V getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple<?, ?> other = (Tuple<?, ?>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
