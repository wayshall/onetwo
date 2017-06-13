package org.onetwo.common.web.preventor;

import org.onetwo.common.md.Hashs;
import org.onetwo.common.md.MessageDigestHasher;

public class Md5TokenValueGenerator implements TokenValueGenerator {

	protected MessageDigestHasher encrypt = Hashs.MD5;
	
	@Override
	public String generatedTokenValue(RequestToken token) {
		return encrypt.hashWithRandomSalt(token.getValue(), -1);
	}

	@Override
	public boolean validateToken(RequestToken token, String targetTokenString) {
		return encrypt.checkHash(token.getValue(), targetTokenString);
	}

}
