package org.onetwo.boot.core.jwt;

import java.io.Serializable;
import java.util.Map;

import org.onetwo.common.web.userdetails.GenericUserDetail;

/**
 * @author weishao zeng
 * <br/>
 */
public interface JwtUserDetail extends GenericUserDetail<Serializable> {

	Map<String, Object> getProperties();
	
	boolean isAnonymousLogin();

}