package org.onetwo.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

@XmlRootElement
public class Page<T> implements Serializable {
	/**
	 *  
	 */
	private static final long serialVersionUID = 5041683742265451593L;
	
	public static <T> Page<T> create(){
		return new Page<T>();
	}
	public static final String ASC = ":asc";
	public static final String DESC = ":desc";
	
	public static final int DEFAULT_PAGE_SIZE = 20;

	public static final int PAGE_SEP_COUNT = 5;
	public static final int PAGE_SHOW_COUNT = 10;

	protected int pageNo = 1;
	protected int pageSize = 20;
	protected String orderBy = null;
	protected String order = null;
	protected boolean autoCount = true;

	protected List<T> result = new ArrayList<T>();
	protected long totalCount = -1;
	
	protected int first = -1;
	
	public Page(){
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		if(pageNo<1)
			pageNo = 1;
		return pageNo;
	}

	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}
	
	public void setPageNo(String pageNoStr){
		int pageNo = Integer.valueOf(pageNoStr);
		this.setPageNo(pageNo);
	}

	/*public Page<T> pageNo(final int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}*/

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(final int pageSize) {
		if(pageSize<=0)
			this.pageSize = DEFAULT_PAGE_SIZE;
		else
			this.pageSize = pageSize;
	}
	
	public int getSize(){
		return getResult().size();
	}

	public Page<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	/****
	 * base 1 start
	 * @return
	 */
	public int getFirst() {
		if(first>0)
			return first;
		return ((pageNo - 1) * pageSize) + 1;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	public Page<T> orderBy(final String theOrderBy) {
		setOrderBy(theOrderBy);
		return this;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(final String order) {
		String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr))
				throw new IllegalArgumentException("错误的排序：" + orderStr);
		}

		this.order = StringUtils.lowerCase(order);
	}

	public Page<T> order(final String theOrder) {
		setOrder(theOrder);
		return this;
	}

	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}

	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	public Page<T> autoCount(final boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(final List<T> result) {
		this.result = result;
	}

	public long getTotalCount() {
		if (totalCount < 0) {
			return 0L;
		}
		return totalCount;
	}

	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}
	
	public void setNoLimited(){
		setAutoCount(false);
		setFirst(-1);
		setPageSize(-1);
	}
	
	public void setLimited(int start, int count){
		setAutoCount(false);
		setFirst(start);
		setPageSize(count);
	}
	
	public void setLimited(int count){
		this.setLimited(1, count);
	}

	public int getTotalPages() {
		if (totalCount < 0)
			return 0;

		int count = (int)(totalCount / pageSize);
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	public int getNextPage() {
		if (isHasNext())
			return pageNo + 1;
		else
			return pageNo;
	}

	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	public int getPrePage() {
		if (isHasPre())
			return pageNo - 1;
		else
			return pageNo;
	}
	
	public int getStartPage(){
		int start = 1;
		if(getPageNo()>PAGE_SEP_COUNT)
			start = getPageNo()-PAGE_SEP_COUNT;
		int end = getEndPage();
		if(end==0)
			return 0;
		int differ = end-start+1;
		if(differ<PAGE_SHOW_COUNT){
			start = start - (PAGE_SHOW_COUNT-differ);
		}
		if(start<1)
			start = 1;
		return start;
	}
	
	public int getEndPage(){
		int end = getPageNo()+PAGE_SEP_COUNT;
		int totalPage = getTotalPages();
		
		if(end>=totalPage){
			end = totalPage;
		}else{
			if(end<PAGE_SHOW_COUNT)
				end = PAGE_SHOW_COUNT;
		}
		if(end>totalPage)
			end = totalPage;
		return end;
	}
	
	public int getStartPage2(){
		int start = 1;
		if(getPageNo()>10)
			start = getPageNo()-10;
		return start;
	}
	
	public int getEndPage2(){
		int end = getPageNo()+9;
		if(getTotalPages()<end)
			end = getTotalPages();
		if(end<1)
			end = 1;
		return end;
	}
}