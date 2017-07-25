package org.onetwo.common.utils;


/**
 * @author wayshall
 * <br/>
 */
public class PageRequest {
	protected int pageNo = 1;
	protected int pageSize = 20;
	private boolean pagination = true;
	
	public <E> Page<E> toPageObject(){
		Page<E> p = Page.create(pageNo, pageSize);
		p.setPagination(pagination);
		return p;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}


	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isPagination() {
		return pagination;
	}

	public void setPagination(boolean pagination) {
		this.pagination = pagination;
	}

}
