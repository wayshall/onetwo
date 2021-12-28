package org.onetwo.ext.poi.utils;
/**
 * @author weishao zeng
 * <br/>
 */

import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.PageRequest;

public interface PagableExcelDataReader<T> {
	
	public default PageRequest initPageRequest() {
		PageRequest pageRequest = new PageRequest();
		// 默认每次获取10000条
		pageRequest.setPageSize(10000);
		return pageRequest;
	}
	
	Page<T> getNextPage(PageRequest pageRequest);

}
