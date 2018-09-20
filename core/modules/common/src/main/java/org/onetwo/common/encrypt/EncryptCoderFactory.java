package org.onetwo.common.encrypt;

import java.security.KeyPair;

import javax.crypto.SecretKey;

import org.onetwo.common.encrypt.Crypts.AESAlgs;
import org.onetwo.common.utils.LangUtils;

/***
 * aes的key size只支持16, 24 和 32个字节
 * 
 * @author wayshall
 *
 */
public class EncryptCoderFactory {

	public static EncryptCoder<SecretKey> aesCoder(){
		return new AESEncryptCoder();
	}
	public static EncryptCoder<SecretKey> aesCoder(boolean generatedSecretKey){
		return new AESEncryptCoder(generatedSecretKey);
	}
	public static EncryptCoder<SecretKey> aesCbcCoder(String key){
		byte[] keyBytes = LangUtils.getBytes(key);
		return new AESEncryptCoder(AESAlgs.ECB_PKCS5Padding, keyBytes);
		// 
//		return new AESEncryptCoder(AESEncryptCoder.AES_CBC_ALGORITHM, Base64.decodeBase64(base64String));
	}

	/***
	 * java内置不支持PKCS7Padding,只支持PKCS5Padding 
	 * 但是PKCS7Padding 和 PKCS5Padding 没有什么区别
	 * 要实现在java端用PKCS7Padding填充,需要用到bouncycastle
	 * @author wayshall
	 * @param aesKey
	 * @param iv
	 * @return
	 */
	public static EncryptCoder<SecretKey> pkcs7Padding(byte[] aesKey, byte[] iv){
		AESEncryptCoder coder = new AESEncryptCoder(AESAlgs.CBC_PKCS7Padding, aesKey);
		coder.initer(AESCoder.createAesIniter(iv));
		return coder;
	}

	/****
	 * iv的长度为16个字节
	 * @author wayshall
	 * @param aesKey
	 * @param iv
	 * @return
	 */
	public static EncryptCoder<SecretKey> pkcs5Padding(byte[] aesKey, byte[] iv){
		AESEncryptCoder coder = new AESEncryptCoder(AESAlgs.CBC_PKCS5Padding, aesKey);
		coder.initer(AESCoder.createAesIniter(iv));
		return coder;
	}
	
	public static EncryptCoder<KeyPair> rsaCoder(){
		return new RSAEncryptCoder();
	}
	
	public static EncryptCoder<KeyPair> rsaCoder(int size, boolean generatedKeyPair){
		return new RSAEncryptCoder(size, generatedKeyPair);
	}

}
