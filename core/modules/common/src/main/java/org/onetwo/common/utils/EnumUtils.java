package org.onetwo.common.utils;

import java.util.List;


final public class EnumUtils { 
	
	public static String[] asStrings(Enum<?>... enums){
		String[] strings = new String[enums.length];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = enums[i].toString();
		}
		return strings;
	}
	
	public static int[] asInts(Enum<?>... enums){
		int[] ints = new int[enums.length];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = enums[i].ordinal();
		}
		return ints;
	}
	
	public static <T extends Enum<T>> List<T> asEnumList(Class<T> clazz, String...strings){
		List<T> enums = LangUtils.newArrayList(strings.length);
		for (int i = 0; i < strings.length; i++) {
			enums.add(Enum.valueOf(clazz, strings[i]));
		}
		return enums;
	}
	
	private EnumUtils(){
	}

}
