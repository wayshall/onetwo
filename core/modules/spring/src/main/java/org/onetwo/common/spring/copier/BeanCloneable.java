package org.onetwo.common.spring.copier;

import org.onetwo.common.utils.LangUtils;

/**
 * @author wayshall
 * <br/>
 */
public interface BeanCloneable {
	
	default <T> T asBean(Class<T> targetClass, String... excludeProperties){
		if (LangUtils.isEmpty(excludeProperties)) {
			return CopyUtils.copy(targetClass, this);
		} else {
			return CopyUtils.copyIgnoreProperties(targetClass, this, excludeProperties);
		}
	}

}
