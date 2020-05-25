package org.onetwo.dbm.ui.core;

import java.util.List;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */

public interface UISelectDataProvider {
	
	List<?> findDatas(SelectQueryRequest request);
	
	@Data
	public class SelectQueryRequest {
		String query;
		String selectedValue;
	}

}
