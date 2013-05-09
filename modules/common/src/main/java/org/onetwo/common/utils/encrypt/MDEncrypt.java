package org.onetwo.common.utils.encrypt;

public interface MDEncrypt {
	
	public boolean checkEncrypt(String source, String encrypt);

	public String encrypt(String source);
	
	public String encryptWithSalt(String source, int length);
	
	public String encryptWithSalt(String source);
	
	public byte[] encryptBytes(byte[] source, byte[] salt);
}
