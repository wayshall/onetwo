package org.onetwo.common.utils.encrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;

public class LangCoder {

	private static final String AES_KEY = "AES";
	private static final int DEFAULT_AES_LENGTH = 128;
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	

//	public static byte[] aesEncrypt(String key, String content){
//		return aes(LangUtils.getBytes(key), DEFAULT_AES_LENGTH, Cipher.ENCRYPT_MODE, LangUtils.getBytes(content));
//	}
//
//	public static byte[] aesDencrypt(String key, String content){
//		return aes(LangUtils.getBytes(key), DEFAULT_AES_LENGTH, Cipher.DECRYPT_MODE, LangUtils.getBytes(content));
//	}
//	
	
	public static byte[] generateKey(String key, int length){
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(AES_KEY);
		} catch (NoSuchAlgorithmException e) {
			throw new BaseException("KeyGenerator error: " + e.getMessage() , e);
		}
		keyGenerator.init(length, new SecureRandom(LangUtils.getBytes(key)));
//		keyGenerator.init(length);
		SecretKey skey = keyGenerator.generateKey();
		byte[] skeyEncoded = skey.getEncoded();
		
		return skeyEncoded;
	}
	
	public static byte[] aesEncrypt(String key, String byteContent){
		return aesEncrypt(LangUtils.hex2Bytes(key), LangUtils.getBytes(byteContent));
	}
	
	public static byte[] aesDencrypt(String key, String byteContent){
		return aesDencrypt(LangUtils.hex2Bytes(key), LangUtils.getBytes(byteContent));
	}
	
	public static byte[] aesEncrypt(byte[] key, byte[] byteContent){
		return aes(key, Cipher.ENCRYPT_MODE, byteContent);
	}
	
	public static byte[] aesDencrypt(byte[] key, byte[] byteContent){
		return aes(key, Cipher.DECRYPT_MODE, byteContent);
	}
//	static SecretKeySpec skeySpec;
	private static byte[] aes(byte[] key, int opmode, byte[] byteContent){
		//构造密匙
		SecretKeySpec skeySpec = new SecretKeySpec(key, AES_KEY);
//		SecretKeySpec skeySpec = new SecretKeySpec(skeyEncoded, AES_KEY);
		//密码器
		Cipher aesCipher = null;
		byte[] result = null;
		try{
			aesCipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			aesCipher.init(opmode, skeySpec);
			result = aesCipher.doFinal(byteContent);
		}catch (Exception e) {
			throw new BaseException("Cipher error: " + e.getMessage() , e);
		}
		return result;
	}
	
}
