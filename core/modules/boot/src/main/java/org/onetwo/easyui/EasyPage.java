package org.onetwo.easyui;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.convert.Types;

public class EasyPage<T> {
	private static final String MYBAITS_PAGE_SUPPORT = "com.github.pagehelper.Page";
	
	public static <E> EasyPage<E> create(List<E> rows){
		EasyPage<E> page = new EasyPage<>(rows);
		return page;
	}

//	protected int first = -1;
	protected int page = 1;
	protected int pageSize = 20;
	protected long total = -1;
	protected List<T> rows = new ArrayList<T>();
	
	
	public EasyPage() {
		super();
	}

	public EasyPage(List<T> rows) {
		super();
		setRows(rows);
	}

	/*public int getFirst() {
		if(first>0)
			return first;
		return ((page - 1) * pageSize) + 1;
	}

	public void setFirst(int first) {
		this.first = first;
	}*/

	public long getTotal() {
		if (total < 0) {
			return 0L;
		}
		return total;
	}

	public void setTotal(final long totalCount) {
		this.total = totalCount;
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
	public List<T> getRows() {
		return rows;
	}
	final public void setRows(List<T> rows) {
		this.rows = rows;
		if(MYBAITS_PAGE_SUPPORT.equals(rows.getClass().getName())){
			this.total = Types.convertValue(ReflectUtils.getProperty(rows, "total"), long.class);
		}else{
			this.total = rows.size();
		}
	}
	
}
