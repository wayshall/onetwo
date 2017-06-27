package org.onetwo.boot.module;

import org.springframework.util.ClassUtils;

/**
 * @author wayshall
 * <br/>
 */
public abstract class ModuleUtils {
	
	public static final String WECHAT_CLASS_NAME = "org.onetwo.ext.apiclient.wechat.EnableWechatClient";
	
	public static boolean isWechatModulePresent(){
		return ClassUtils.isPresent(WECHAT_CLASS_NAME, null);
	}

}
