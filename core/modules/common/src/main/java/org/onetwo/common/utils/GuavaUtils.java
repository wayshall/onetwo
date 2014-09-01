package org.onetwo.common.utils;

import java.util.Iterator;

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
	
	private GuavaUtils(){
	}

}
