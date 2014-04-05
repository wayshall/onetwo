package org.onetwo.common.utils.convert;

import java.lang.reflect.Array;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class ToArrayConvertor extends AbstractWithConvertorTypeConvert<Object>{

	public ToArrayConvertor(Convertor convertor) {
		super(convertor);
	}

	@Override
	public Object doConvert(Object source, Class<?> componentType) {
//		if(source==null)
//			return LangUtils.EMPTY_STRING_ARRAY;
		
		Object result = LangUtils.EMPTY_ARRAY;
		if(source.getClass().isArray()){
			int length = Array.getLength(source);
			result = Array.newInstance(componentType, length);
			for(int i=0; i<length; i++){
				Array.set(result, i, this.convertor.convert(Array.get(source, i), componentType));
			}
		}
		if(String.class==componentType){
			result = asStringArray(source);
		}else if(Number.class.isAssignableFrom(componentType)){
			result = asArray(source, componentType);
		}else {
		}
		return result;
	}
	
	private String[] asStringArray(Object source){
		Class<?> c = source.getClass();
		String[] strs = LangUtils.EMPTY_STRING_ARRAY;
		if(String.class==c){
			String str = source.toString();
			if(str.indexOf(',')!=-1){
				strs = StringUtils.split(str, ',');
			}else{
				strs = new String[]{str};
			}
		}
		
		return strs;
	}
	
	private <T> T[] asArray(Object source, Class<T> numberClass){
		Class<?> c = source.getClass();
		T[] longs;
		if(String.class==c){
			String[] strs = convertor.convert(source, Object[].class, String.class);
			longs = (T[])Array.newInstance(numberClass, strs.length);
			for(int i=0; i<strs.length; i++){
				longs[i] = this.convertor.convert(strs[i], numberClass);
			}
		}else{
			T l = this.convertor.convert(source, numberClass);
			longs = (T[])Array.newInstance(numberClass, 1);
			longs[0] = l;
		}
		
		return longs;
	}

}
