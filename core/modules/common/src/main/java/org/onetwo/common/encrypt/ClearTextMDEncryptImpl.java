package org.onetwo.common.encrypt;

import org.onetwo.common.utils.Assert;

public class ClearTextMDEncryptImpl implements MDEncrypt{
	
	private String name = "cleartext";

	public ClearTextMDEncryptImpl(){
	}
	public ClearTextMDEncryptImpl(String name){
		this.name = name;
	}

	public String getLabel() {
		return "{"+name+"}";
	}

	@Override
	public boolean checkEncrypt(String source, String encrypt) {
		Assert.hasText(source);
		String noLabels = MDEncryptUtils.trimLabel(encrypt);
		return source.equalsIgnoreCase(noLabels);
	}

	@Override
	public String encrypt(String source) {
		Assert.hasText(source);
		return getLabel()+source;
	}

	@Override
	public String encryptWithSalt(String source, int length) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String encryptWithSalt(String source) {
		throw new UnsupportedOperationException();
	}
	@Override
	public byte[] encryptBytes(byte[] source, byte[] salt) {
		throw new UnsupportedOperationException();
	}
	@Override
    public boolean checkEncrypt(String source, String saltStr, String encrypt) {
		throw new UnsupportedOperationException();
    }
	@Override
    public String encryptWithSalt(String source, byte[] salt) {
		throw new UnsupportedOperationException();
    }
	@Override
    public String encryptWithSalt(String source, String salt) {
		throw new UnsupportedOperationException();
    }

}
