package org.onetwo.ext.permission;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author weishao zeng
 * <br/>
 */
public interface RootMenuClassProvider {
	
	List<Class<?>> rootMenuClassList();

	
	default List<Class<?>> rootMenuClassListByProfiles(Collection<String> profiles) {
		return Collections.emptyList();
	};

}
