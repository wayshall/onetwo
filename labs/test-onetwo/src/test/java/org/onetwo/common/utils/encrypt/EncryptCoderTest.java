package org.onetwo.common.utils.encrypt;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

public class EncryptCoderTest {
	
	@Test
	public void testAes(){
		EncryptCoder aes = EncryptCoderFactory.aesCoder();
		String content = "aes数据";
		
		byte[] encryptData = aes.encrypt(aes.getKey(), LangUtils.getBytes(content));
		byte[] dencryptData = aes.dencrypt(aes.getKey(), encryptData);
		
		String dencryptContent = LangUtils.newString(dencryptData);
		Assert.assertEquals(content, dencryptContent);
		
	}
	
	@Test
	public void testRsa(){
		RSAEncryptCoder rsa = (RSAEncryptCoder)EncryptCoderFactory.rsaCoder();
		String content = "rsa数据";
		
		byte[] encryptData = rsa.encrypt(rsa.getKey(), LangUtils.getBytes(content));
		byte[] dencryptData = rsa.dencrypt(rsa.getPrivateKey(), encryptData);
		
		String dencryptContent = LangUtils.newString(dencryptData);
		Assert.assertEquals(content, dencryptContent);
		
	}

}
