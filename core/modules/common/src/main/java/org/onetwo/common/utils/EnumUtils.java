package org.onetwo.common.utils;

import java.util.Arrays;
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
	
	public static Enum<? extends Enum<?>>[] asEnums(Class<? extends Enum<?>> clazz, String...strings){
		Enum<? extends Enum<?>>[] enums = new Enum<?>[strings.length];
		for (int i = 0; i < strings.length; i++) {
			enums[i] = Enum.valueOf((Class)clazz, strings[i]);
		}
		return enums;
	}
	
	public static List<? extends Enum<?>> asEnumList(Class<? extends Enum<?>> clazz, String...strings){
		Enum<? extends Enum<?>>[] enums = asEnums(clazz, strings);
		return Arrays.asList(enums);
	}
	
	private EnumUtils(){
	}

}
