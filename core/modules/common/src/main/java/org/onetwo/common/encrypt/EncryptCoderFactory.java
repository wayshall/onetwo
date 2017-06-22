package org.onetwo.common.encrypt;

import java.security.KeyPair;

import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

public class EncryptCoderFactory {

	public static EncryptCoder<SecretKey> aesCoder(){
		return new AESEncryptCoder();
	}
	public static EncryptCoder<SecretKey> aesCoder(boolean generatedSecretKey){
		return new AESEncryptCoder(generatedSecretKey);
	}
	public static EncryptCoder<SecretKey> aesCbcCoder(String base64String){
		return new AESEncryptCoder(AESEncryptCoder.AES_CBC_ALGORITHM, Base64.decodeBase64(base64String));
	}
	
	public static EncryptCoder<KeyPair> rsaCoder(){
		return new RSAEncryptCoder();
	}
	
	public static EncryptCoder<KeyPair> rsaCoder(int size, boolean generatedKeyPair){
		return new RSAEncryptCoder(size, generatedKeyPair);
	}

}
