package org.onetwo.common.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
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
	
		//aes的key size只支持16, 24 和 32个字节
		String keyString = RandomStringUtils.randomAlphabetic(32);
		aes = EncryptCoderFactory.aesCbcCoder(keyString);
		encryptData = aes.encrypt(aes.getKey(), LangUtils.getBytes(content));
		dencryptData = aes.dencrypt(aes.getKey(), encryptData);
		
		dencryptContent = LangUtils.newString(dencryptData);
		Assert.assertEquals(content, dencryptContent);
		
	}
	
	@Test
	public void testPkcs7Padding(){
		//iv的长度为16个字节
		String iv = RandomStringUtils.randomAlphabetic(16);
		String keyString = RandomStringUtils.randomAlphabetic(32);
		//使用pkcs7Padding会抛错：Cannot find any provider supporting AES/CBC/PKCS7Padding
		//java 不支持PKCS7Padding,只支持PKCS5Padding 但是PKCS7Padding 和 PKCS5Padding 没有什么区别要实现在java端用PKCS7Padding填充,需要用到bouncycastle组件来实现 所以需要一个jar 来支持.bcprov-jdk16-146.jar
//		EncryptCoder<?> aes = EncryptCoderFactory.testPkcs7Padding(keyString.getBytes(), iv.getBytes());
		EncryptCoder<?> aes = EncryptCoderFactory.pkcs5Padding(keyString.getBytes(), iv.getBytes());
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
