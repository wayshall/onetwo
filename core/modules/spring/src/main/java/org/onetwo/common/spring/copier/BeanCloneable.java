package org.onetwo.common.spring.copier;
/**
 * @author wayshall
 * <br/>
 */
public interface BeanCloneable {
	
	default <T> T asBean(Class<T> targetClass){
		return CopyUtils.copy(targetClass, this);
	}

}
