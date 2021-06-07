package org.onetwo.common.utils;

import org.onetwo.common.exception.BaseException;

/**
 * @author weishao zeng
 * <br/>
 */

final public class BinaryUtils {
	
	/***
	 * 获取数值二进制第n位的数字
	 * @author weishao zeng
	 * @param value
	 * @param pos
	 * @return 1 or 0
	 */
	public static byte getBitValue(byte value, int pos) {
		return (byte)((value >> pos) & 1);
	}

	public static int getBitValue(int value, int pos) {
		return (value >> pos) & 1;
	}
	
	/***
	 * 获取字节的高四位
	 * @author weishao zeng
	 * @param value
	 * @return
	 */
	public static byte getHighBits(byte value) {
		// high. symbol >>> is unsigned right shift 
		return (byte)(value >>> 4 & 0xF);
	}
	
	/***
	 * 获取字节的低四位
	 * @author weishao zeng
	 * @param value
	 * @return
	 */
	public static byte getLowBits(byte value) {
		// high. symbol >>> is unsigned right shift 
		return (byte)(value & 0xF);
	}

	/****
	 * 把16进制字符串转为10进制数字
	 * @author weishao zeng
	 * @param hex
	 * @return
	 */
	public static int hexToInt(String hex) {
		byte[] bytes = LangUtils.hex2Bytes(hex);
		int result = bytesToInt(bytes);
		return result;
	}
	
	/****
	 * 把数组转为十进制数字
	 * @author weishao zeng
	 * @param bytes
	 * @return
	 */
	public static int bytesToInt(byte[] bytes) {
		if (bytes.length > 4) {
			throw new BaseException("error int value: " + bytes);
		}

		int result = 0;
		int maxLength = bytes.length-1;
		for (int i = 0; i < bytes.length; i++) {
			// 转成二进制 bytes[i] & 0xFF, i=0为高位，按实际长度左移n个8位
			result = result | ( (bytes[i] & 0xFF) << 8*(maxLength-i) );
		}
		return result;	
	}
	
	private BinaryUtils() {
	}

}
