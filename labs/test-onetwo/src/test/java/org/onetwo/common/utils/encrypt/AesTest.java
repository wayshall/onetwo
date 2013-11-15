package org.onetwo.common.utils.encrypt;

import javax.crypto.Cipher;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

public class AesTest {
	
	@Test
	public void testAes(){
		String key = "aabbcc";
//		byte[] keya = LangUtils.getBytes(key);
//		byte[] keya = new byte[]{-10,98,-93,-77,-61,-59,37,48,-112,22,-35,-18,-97,-20,65,24};
		byte[] keya = LangCoder.generateKey(key, 128);
		String content = "AES数据";
		
		byte[] contentBytes = LangUtils.getBytes(content);
		System.out.println("加密前字符串["+content.length()+"]: " + content);
		System.out.println("加密前数组["+contentBytes.length+"]: " + LangUtils.toString(contentBytes));
		byte[] encodeBytes = LangCoder.aesEncrypt(keya, contentBytes);
		String encodeHex = LangUtils.toHex(encodeBytes);
		System.out.println("加密后数组["+encodeBytes.length+"]: " + LangUtils.toString(encodeBytes));
		System.out.println("加密后十六进制字符串["+encodeHex.length()+"]: " + encodeHex);
		
		byte[] encodeBytes2 = LangUtils.hex2Bytes(encodeHex);
		System.out.println("加密后数组["+encodeBytes2.length+"]: " + LangUtils.toString(encodeBytes2));
		
//		byte[] keyb = LangCoder.generateKey(key, 128);
		byte[] decodeBytes = LangCoder.aesDencrypt(keya, encodeBytes2);
		String decodeString = LangUtils.newString(decodeBytes, LangUtils.UTF8);
		System.out.println("解密后数组["+decodeBytes.length+"]: " + LangUtils.toString(decodeBytes));
		System.out.println("加密后字符串["+decodeString.length()+"]: " + decodeString);
		
		Assert.assertEquals(content, decodeString);
	}

}
