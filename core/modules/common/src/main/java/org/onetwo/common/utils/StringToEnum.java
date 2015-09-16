package org.onetwo.common.utils;


public class StringToEnum<T extends Enum<T>> {
	private final Class<T> enumType;

	public StringToEnum(Class<T> enumType) {
		this.enumType = enumType;
	}

	public T convert(String source) {
		if (source.length() == 0) {
			return null;
		}
		try {
			return (T) Enum.valueOf(this.enumType, source.trim());
		} catch (IllegalArgumentException e) {
			try {
				return (T) Enum.valueOf(this.enumType, source.trim().toUpperCase());
			} catch (IllegalArgumentException e2) {
				throw e;
			}
		}
	}
}
