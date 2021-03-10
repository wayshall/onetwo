package org.onetwo.boot.module.oauth2ssoclient;
/**
 * 验证类型
 * @author weishao zeng
 * <br/>
 */
public enum TokenAuthenticationTypes {
	/**
	 * Send an Authorization header.
	 */
	header,

	/**
	 * 附加到url参数上
	 */
	query,

	/**
	 * Send in the form body.
	 */
	form,

	/**
	 * Do not send at all.
	 */
	none
	
}
