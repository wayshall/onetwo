package org.onetwo.dbm.ui.core;

import java.util.List;

/**
 * @author weishao zeng
 * <br/>
 */

public interface UISelectDataProvider {
	
	List<?> findDatas(String query);

}
