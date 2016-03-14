package org.onetwo.common.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

public class EncryptCoderTest {
	
	@Test
	public void testAes(){
		EncryptCoder<?> aes = EncryptCoderFactory.aesCoder();
		String content = "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcd";
		
		byte[] encryptData = aes.encrypt(aes.getKey(), LangUtils.getBytes(content));
		byte[] dencryptData = aes.dencrypt(aes.getKey(), encryptData);
		
		String dencryptContent = LangUtils.newString(dencryptData);
		Assert.assertEquals(content, dencryptContent);
		
	}
	
	@Test
	public void testRsa(){
		RSAEncryptCoder rsa = (RSAEncryptCoder)EncryptCoderFactory.rsaCoder();
		String content = "测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa";
		
		System.out.println("publickey: " + LangUtils.toHex(rsa.getKey()).length());
		//公钥加密
		byte[] encryptData = rsa.encrypt(rsa.getKey(), LangUtils.getBytes(content));
		String encryptStr = LangUtils.newString(Base64.encodeBase64(encryptData));
		encryptData = Base64.decodeBase64(LangUtils.getBytes(encryptStr));
		//私钥解密
		byte[] dencryptData = rsa.dencrypt(rsa.getPrivateKey(), encryptData);
		String dencryptContent = LangUtils.newString(dencryptData);
		Assert.assertEquals(content, dencryptContent);
		
	}
	
	@Test
	public void testRsaHex(){
		RSAEncryptCoder rsa = (RSAEncryptCoder)EncryptCoderFactory.rsaCoder();
		String content = "测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa测试rsa";
		
		String hexKey = LangUtils.toHex(rsa.getKey());
		byte[] encryptData = rsa.encrypt(LangUtils.hex2Bytes(hexKey), LangUtils.getBytes(content));
		byte[] dencryptData = rsa.dencrypt(rsa.getPrivateKey(), encryptData);
		
		String dencryptContent = LangUtils.newString(dencryptData);
		Assert.assertEquals(content, dencryptContent);
	}
	
}
