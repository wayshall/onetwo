package org.onetwo.common.encrypt;

import java.security.KeyPair;

import javax.crypto.SecretKey;

public class EncryptCoderFactory {

	public static EncryptCoder<SecretKey> aesCoder(){
		return new AESEncryptCoder();
	}
	public static EncryptCoder<SecretKey> aesCoder(int size, boolean generatedSecretKey){
		return new AESEncryptCoder(size, generatedSecretKey);
	}
	
	public static EncryptCoder<KeyPair> rsaCoder(){
		return new RSAEncryptCoder();
	}
	
	public static EncryptCoder<KeyPair> rsaCoder(int size, boolean generatedKeyPair){
		return new RSAEncryptCoder(size, generatedKeyPair);
	}

}
