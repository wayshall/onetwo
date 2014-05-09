package org.onetwo.common.utils.encrypt;

public class EncryptCoderFactory {

	public static EncryptCoder aesCoder(){
		return new AESEncryptCoder();
	}
	public static EncryptCoder aesCoder(int size){
		return new AESEncryptCoder(size);
	}
	
	public static EncryptCoder rsaCoder(){
		return new RSAEncryptCoder();
	}
	
	public static EncryptCoder rsaCoder(int size){
		return new RSAEncryptCoder(size);
	}

}
