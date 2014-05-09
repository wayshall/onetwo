package org.onetwo.common.utils.encrypt;

public interface EncryptCoder {

	public byte[] getKey();

	public byte[] encrypt(byte[] encryptKey, byte[] byteContent);

	public byte[] dencrypt(byte[] dencryptKey, byte[] byteContent);

}