package org.onetwo.common.web.preventor;

public interface TokenValueGenerator {
	
	public String generatedTokenValue(RequestToken token);
	
	public boolean validateToken(RequestToken token, String targetTokenString);
	
	
}
