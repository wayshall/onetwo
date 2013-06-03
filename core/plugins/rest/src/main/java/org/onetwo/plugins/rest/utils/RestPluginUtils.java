package org.onetwo.plugins.rest.utils;

import org.onetwo.common.utils.Page;
import org.onetwo.plugins.rest.RestResult;

public class RestPluginUtils {

	public static final <E> PageData<E> newPageData(){
		return newPageData(null);
	}
	
	public static final <E> PageData<E> newPageData(Page<E> p){
		PageData<E> page = new PageData<E>();
		if(p==null)
			return page;
		page.setPage_no(p.getPageNo());
		page.setPage_size(p.getPageSize());
		page.setTotal_count(p.getTotalCount());
		page.setTotal_pages(p.getTotalPages());
		page.setList(p.getResult());
		return page;
	}
	
	public static final <E> RestResult<PageData<E>> newPageRestResult(Page<E> p){
		RestResult<PageData<E>> result = new RestResult<PageData<E>>();
		result.setData(newPageData(p));
		return result;
	}
}
