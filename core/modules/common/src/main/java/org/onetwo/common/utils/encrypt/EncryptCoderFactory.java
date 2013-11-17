package org.onetwo.common.utils.encrypt;

public class EncryptCoderFactory {
	
	public static EncryptCoder aesCoder(){
		return new AESEncryptCoder();
	}
	
	public static EncryptCoder rsaCoder(){
		return new RSAEncryptCoder();
	}

}
