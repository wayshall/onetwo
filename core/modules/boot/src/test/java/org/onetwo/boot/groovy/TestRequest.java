package org.onetwo.boot.groovy;


/**
 * @author wayshall
 * <br/>
 */
public class TestRequest {
	
	int page;
	int pageSize;
	
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
	@Override
	public String toString() {
		return "TestRequest [page=" + page + ", pageSize=" + pageSize + "]";
	}
	
}
