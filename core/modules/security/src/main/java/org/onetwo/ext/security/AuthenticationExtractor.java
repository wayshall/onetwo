package org.onetwo.ext.security;

import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.springframework.security.core.Authentication;

public interface AuthenticationExtractor {
	
	GenericUserDetail<?> extract(Authentication authentication);

}
