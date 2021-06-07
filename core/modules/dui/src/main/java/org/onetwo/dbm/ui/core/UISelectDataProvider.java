package org.onetwo.dbm.ui.core;

import java.util.List;

import org.onetwo.dbm.ui.vo.UISelectDataRequest;

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

		public boolean isInitQuery() {
			return UISelectDataRequest.isRequestInitQuery(query);
		}
	}

}
