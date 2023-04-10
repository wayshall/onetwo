package org.onetwo.ext.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationExtractor {
	
	Object extract(Authentication authentication);

}
