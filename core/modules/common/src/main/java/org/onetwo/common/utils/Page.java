package org.onetwo.common.utils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Page<T> implements Serializable {
	/**
	 *  
	 */
	private static final long serialVersionUID = 5041683742265451593L;

	public static <E> Page<E> create(){
		return new Page<E>();
	}

	public static <E2, E1> Page<E2> createByPage(Page<E1> p, Function<? super E1, ? extends E2> mapper){
		Page<E2> page = Page.create();
		page.pageNo = p.pageNo;
		page.pageSize = p.pageSize;
		page.order = p.order;
		page.orderBy = p.orderBy;
		page.first = p.first;
		page.autoCount = p.autoCount;
		page.pagination = p.pagination;
		if(mapper!=null){
			List<E2> rs = p.result.stream().map(mapper).collect(Collectors.toList());
			page.setResult(rs);
		}
		return page;
	}
	public static <E> Page<E> create(Integer pageNo){
		Page<E> page = new Page<>();
		page.setPageNo(pageNo);
		return page;
	}
	public static <E> Page<E> create(Integer pageNo, Class<E> clazz){
		Page<E> page = new Page<>();
		page.setPageNo(pageNo);
		return page;
	}
	public static <E> Page<E> create(Integer pageNo, Integer pageSize){
		Page<E> page = new Page<>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return page;
	}
	
	
	public static void setDefaultPageSize(int defaultPageSize) {
		Page.DefaultPageSize = defaultPageSize;
	}

	public static int getDefaultPageSize() {
		return DefaultPageSize;
	}

	public static final String PAGINATION_KEY = "pagination";
	public static final String ASC = ":asc";
	public static final String DESC = ":desc";
	
	private static int DefaultPageSize = 20;

	/***
	 * 分页显示时，显示相距当前页的数量，默认为当前页前后5页
	 */
	private static final int PAGE_SEP_COUNT = 5;
	/****
	 * 一共显示几个分页，默认为10个页码
	 */
	private static final int PAGE_SHOW_COUNT = 10;

	protected int pageNo = 1;
	protected int pageSize = DefaultPageSize;
	protected String orderBy = null;
	protected String order = null;
	protected Boolean autoCount;

	protected List<T> result = Collections.emptyList();
	protected long totalCount = 0;
	
	protected int first = -1;
	
	private boolean pagination = true;
	
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

	public void setPageNo(final Integer pageNo) {
		if(pageNo==null){
			return ;
		}
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}
	
	/*public void setPageNo(String pageNoStr){
		int pageNo = Integer.valueOf(pageNoStr);
		this.setPageNo(pageNo);
	}*/

	/*public Page<T> pageNo(final int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}*/

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(final Integer pageSize) {
		if(pageSize==null){
			return ;
		}
		if(pageSize<=0)
			this.pageSize = DefaultPageSize;
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
//		this.orderBy = SqlUtils.check(orderBy);
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
		if(StringUtils.isBlank(order))
			return ;
		if (!StringUtils.equals(DESC, order) && !StringUtils.equals(ASC, order))
			throw new IllegalArgumentException("错误的排序：" + order);

		this.order = StringUtils.lowerCase(order);
	}
	
	public boolean needSort(){
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}
	
	public String getOrderString(){
		return needSort()?this.order.substring(1):"";
	}

	public Page<T> order(final String theOrder) {
		setOrder(theOrder);
		return this;
	}

	public boolean isAutoCount() {
		if(autoCount==null){
			return isPagination();
		}else{
			return autoCount;
		}
//		return isPagination() && autoCount;
	}

	/****
	 * 不设置autoCount，是否自动count将会由pagination属性决定；
	 * 若设置了，则根据设置决定
	 * @author wayshall
	 * @param autoCount
	 */
	public void setAutoCount(final Boolean autoCount) {
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
//		this.result.datas(result);
		this.result = result;
	}

	public long getTotalCount() {
		long totalCount = this.totalCount;
		if (totalCount < 0) {
			return 0L;
		}
		return totalCount;
	}

	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}
	
	public Page<T> noLimited(){
		/*setAutoCount(false);
		setFirst(-1);
		setPageSize(-1);*/
		setAutoCount(false);
		setPagination(false);
		return this;
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
		long totalCount = this.totalCount;
		if (totalCount <= 0)
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

	public boolean isPagination() {
		return pagination;
	}

	public void setPagination(boolean pagination) {
		this.pagination = pagination;
	}

	public <R> Page<R> mapToNewPage(Function<? super T, ? extends R> mapper){
		return createByPage(this, mapper);
	}
	
}