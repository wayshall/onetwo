package org.onetwo.common.web.preventor;

import org.onetwo.common.utils.encrypt.MDEncrypt;
import org.onetwo.common.utils.encrypt.MDFactory;

public class Md5TokenValueGenerator implements TokenValueGenerator {

	protected MDEncrypt encrypt = MDFactory.MD5;
	
	@Override
	public String generatedTokenValue(RequestToken token) {
		return encrypt.encryptWithSalt(token.getValue());
	}

	@Override
	public boolean validateToken(RequestToken token, String targetTokenString) {
		return encrypt.checkEncrypt(token.getValue(), targetTokenString);
	}

}
