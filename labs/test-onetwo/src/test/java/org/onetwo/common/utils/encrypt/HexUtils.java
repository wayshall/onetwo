package org.onetwo.common.utils.encrypt;

import org.onetwo.common.utils.MyUtils;

public abstract class HexUtils {

	private static final String HEX_CHAR = "0123456789ABCDEF";

	public static String toHex3(byte[] bytes){
		StringBuilder buf = new StringBuilder();
		for(byte b : bytes){
			String s = Integer.toHexString(b);
			s = MyUtils.append(s, -2, "0");//补0
			buf.append(s);
		}
		return buf.toString().toUpperCase();
	}
	
	public static String toHex2(byte[] bytes){
		StringBuilder buf = new StringBuilder();
		for(byte b : bytes){
			buf.append(Integer.toHexString(((0x000000ff & b) | 0xffffff00)).substring(6));//这个运算只为了保证，但少于10时，不丢失0
		}
		return buf.toString().toUpperCase();
	}
	
	public static String toHex(byte[] bytes){
		StringBuilder buf = new StringBuilder();
		for(byte b : bytes){
			buf.append(HEX_CHAR.charAt((b >>> 4 & 0xf)));//high
			buf.append(HEX_CHAR.charAt((b & 0xf)));//low
		}
		return buf.toString();
	}
	

	public static byte[] hex2Bytes2(String str){
		byte[] bytes = null;
		char[] chars = str.toCharArray();

		int numb1;
		int numb2;
		for(int i=0; i<chars.length; i=i+2){
			numb1 = HEX_CHAR.indexOf(chars[i]) << 4  & 0xf0 ;
			numb2 = HEX_CHAR.indexOf(chars[i+1]) & 0xf;
			bytes = org.apache.commons.lang.ArrayUtils.add(bytes, (byte)((numb1 | numb2) & 0xff));
		}
		return bytes;
	}
	
	public static byte[] hex2Bytes(String str){
		byte[] bytes = null;
		char[] chars = str.toCharArray();
		char ch;
		int decNumb;
		
		String hignStr="", lowStr="";
		for(int i=0; i<chars.length; i++){
			ch = chars[i];
			decNumb = HEX_CHAR.indexOf(ch);
			if(i%2==0){//高位
				hignStr = Integer.toBinaryString(decNumb);
				hignStr = MyUtils.append(hignStr, 4, "0");
			}else{//低位
				lowStr = Integer.toBinaryString(decNumb);
				lowStr = MyUtils.append(lowStr, 4, "0");
				String n = hignStr+lowStr;
				byte bt = Byte.parseByte(n.substring(1), 2);
				if(n.subSequence(0, 1).equals("1"))
					bt = (byte)(bt-128);
				bytes = org.apache.commons.lang.ArrayUtils.add(bytes, bt);
			}
		}
		return bytes;
	}
	
}
