package org.onetwo.dbm.ui.spi;

import org.onetwo.dbm.ui.vo.UISelectDataRequest;

/**
 * @author weishao zeng
 * <br/>
 */

public interface DUISelectDataProviderService {

	Object getDatas(UISelectDataRequest request);
	
}
