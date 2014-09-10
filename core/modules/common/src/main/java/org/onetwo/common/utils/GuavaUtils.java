package org.onetwo.common.utils;

import java.util.Iterator;
import java.util.Map.Entry;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;

public final class GuavaUtils {

	public static Iterable<String> EMPTY_ITERABLE = new Iterable<String>() {
												      @Override public Iterator<String> iterator() {
												          return Iterators.emptyIterator();
												        }
												    };
												    
	public static String[] split(String str, char splitChar) {
		return StringUtils.isBlank(str)?LangUtils.EMPTY_STRING_ARRAY:CUtils.asStringArray(Splitter.on(splitChar).trimResults().omitEmptyStrings().split(str));
	}
	
	public static Iterable<String> iterable(String str, char splitChar) {
		return StringUtils.isBlank(str)?EMPTY_ITERABLE:Splitter.on(splitChar).trimResults().omitEmptyStrings().split(str);
	}
	
	public static String join(Iterable<?> strs, String joiner) {
		return Joiner.on(joiner).skipNulls().join(strs);
	}
	
	public static String join(Iterable<? extends Entry<?, ?>> strs, String joiner, String keyValueSeparator) {
		return Joiner.on(joiner).skipNulls().withKeyValueSeparator(keyValueSeparator).join(strs);
	}
	
	private GuavaUtils(){
	}

}
