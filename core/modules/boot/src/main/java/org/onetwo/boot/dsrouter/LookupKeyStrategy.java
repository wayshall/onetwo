package org.onetwo.boot.dsrouter;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wayshall
 * <br/>
 */
public interface LookupKeyStrategy {
	
	default String getName() {
		String name = this.getClass().getSimpleName();
		String postfix = LookupKeyStrategy.class.getSimpleName();
		if(name.endsWith(postfix)){
			name = name.substring(0, name.length()-postfix.length());
			name = StringUtils.uncapitalize(name);
		}
		return name;
	}
	
	Object lookup();

}
