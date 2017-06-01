package org.onetwo.common.md;

import org.onetwo.common.utils.Assert;

public class ClearTextMDEncryptImpl implements MessageDigestHasher{
	
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
	public boolean checkHash(String source, String encrypt) {
		Assert.hasText(source);
		String noLabels = MessageDigestHashUtils.trimLabel(encrypt);
		return source.equalsIgnoreCase(noLabels);
	}

	@Override
	public String hash(String source) {
		Assert.hasText(source);
		return getLabel()+source;
	}

	@Override
	public String hashWithRandomSalt(String source, int length) {
		return hash(source);
	}

	@Override
	public String hash(byte[] source, byte[] salt) {
		throw new UnsupportedOperationException();
	}
	@Override
    public boolean checkHash(String source, String saltStr, String encrypt) {
		return checkHash(source, saltStr, encrypt);
    }
	@Override
    public String hashWithSalt(String source, byte[] salt) {
		return hash(source);
    }
	@Override
    public String hashWithSalt(String source, String salt) {
		return hash(source);
    }

}
