package org.onetwo.common.utils;


/**
 * @author wayshall
 * <br/>
 */
public class PageRequest {
	protected int page = 1;
	protected int pageSize = Page.getDefaultPageSize();
	private boolean pagination = true;
	private boolean autoCount = true;
	
	public <E> Page<E> toPageObject(){
		Page<E> pageObject = Page.create(page, pageSize);
		pageObject.setPagination(pagination);
		pageObject.setAutoCount(autoCount);
		return pageObject;
	}
	
	public void noLimited() {
		this.setAutoCount(false);
		this.setPagination(false);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setPageNo(int pageNo) {
		this.page = pageNo;
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

	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}
}
