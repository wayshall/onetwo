package org.onetwo.common.utils.encrypt;


public interface EncryptCoder<T> {

	public byte[] getKey();
	
	public T generatedKey();

	public byte[] encrypt(byte[] encryptKey, byte[] byteContent);

	public byte[] dencrypt(byte[] dencryptKey, byte[] byteContent);

}