package org.onetwo.boot.webmgr;

import java.util.Map;

/**
 * @author weishao zeng
 * <br/>
 */
public interface WebManagementCommand {
	
	String getName();
	
	Object invoke(Map<String, Object> data);

}
