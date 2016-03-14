package org.onetwo.common.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.onetwo.common.utils.LangUtils;

abstract public class MDEncryptUtils {

	/***
	 * 
	 * @param encryptStr
	 * @return 
	 */

	public static String getLabel(String encryptStr){
		return getLabel(encryptStr, "");
	}

	public static String trimLabel(String encryptStr){
		String trimLabel = encryptStr;
		int index = encryptStr.indexOf('}');
		if(index!=-1)
			trimLabel = encryptStr.substring(index+1);
		return trimLabel;
	}
	
	public static String getLabel(String encryptStr, String def){
		return getLabel(encryptStr, false, def);
	}
	
	public static String getLabel(String encryptStr, boolean trimBrace, String def){
		String label = "";
		int index = encryptStr.indexOf('}');
		if(index!=-1)
			label = encryptStr.substring(0, index+1);
		else
			label = def;
		return trimBrace?label.substring(1, label.length()-1):label;
	}
	


	public static String encode(String str){
		return encode(str.getBytes(), null);
	}
	public static String encode(byte[] bytes){
		return encode(bytes, null);
	}
	
	public static String encode(byte[] bytes, String charset){
		return LangUtils.newString(Base64.encodeBase64(bytes), charset);
	}

	public static void main(String[] args){
		String str = "{sha}asdfasdf";

		String s = "345asdfafasdsfsadfas";
		String r = encode(s);
		System.out.println(s.length()+"->"+r.length()+":"+r);
		s = "345asdasdfasdfafafasdsfsadfas";
		r = encode(s);
		System.out.println(s.length()+"->"+r.length()+":"+r);
		s = "3445w345asdfafasghklihdfgnh9t78dsfsadfas";
		r = encode(s);
		System.out.println(s.length()+"->"+r.length()+":"+r);
	}

}
