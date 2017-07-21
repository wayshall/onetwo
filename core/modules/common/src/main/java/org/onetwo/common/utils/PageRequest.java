package org.onetwo.common.utils;


/**
 * @author wayshall
 * <br/>
 */
public class PageRequest {
	protected int page = 1;
	protected int pageSize = 20;
	
	public <E> Page<E> toPageObject(){
		return Page.create(page, pageSize);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
