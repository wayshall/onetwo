package org.onetwo.common.web.s2.tag.component;

import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.Page;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("unchecked")
@StrutsTag(name = "table", tldTagClass = "org.onetwo.common.web.s2.tag.StrutsTableTag", description = "Render HTML Table tag.", allowDynamicAttributes = true)
public class StrutsTable extends TableUIBean {
	
	public static interface TableRowable {

		public StrutsTable getParentTable();
		public boolean isHeader();
		public String getType();
	}
	
	public static final String DEFAULT_CLASS = "struts_table";
	
	public static final String OPEN_TEMPLATE = "table";
	public static final String TEMPLATE = "table-close";

	public static final String TABLEINFO = "tableInfo";
	
	private static final boolean DEFAULT_SKIP_INF_NOT_INCLUDED = false;
	
	protected String dataSource;
	protected Page page;
//	protected Iterator iterator;
	protected String colCount;
	protected String action;
	protected String dataFormat;

	protected boolean pagination;
	
	protected boolean skipIfNotIncluded = DEFAULT_SKIP_INF_NOT_INCLUDED;
	protected boolean ajax;
	
	protected TableInfo tableInfo;
	
	public StrutsTable(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	@Override
	public String getDefaultOpenTemplate() {
		return OPEN_TEMPLATE;
	}

	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE;
	}

	@Override
	public void evaluateExtraParams() {
		super.evaluateExtraParams();

		addParameter("colCount", getColCountValue());
		addParameter("action", this.getUri());
		addParameter("ajax", ajax);
		addParameter("skipIfNotIncluded", this.skipIfNotIncluded);
		addParameter("formName", this.name+"Form");
		addParameter("dataFormat", dataFormat);
		
		Object value = null;
		if (StringUtils.isBlank(dataSource))
			value = this.parameters.get("dataSource");
		else
			value = this.findValue(this.dataSource);

		if(value==null){
			this.dataSource = name;
			value = this.findValue(name);
		}
		addParameter("dataSource", this.dataSource);
		
		if(value==null)
			return ;
		
		if(value instanceof Page){
			page = (Page) value;
//			this.iterator = page.getResult().iterator();
			this.pagination = true;
			addParameter("page", page);
		}else{
//			this.iterator = MakeIterator.convert(value);
			List list = null;
			list = MyUtils.asList(value);
			page = new Page();
			page.setResult(list);
			page.setTotalCount(list.size());
			page.setPageSize(list.size());
		}
		
		addParameter("pagination", this.pagination);
	}
    public boolean start(Writer writer) {
        boolean result = super.start(writer);
    	tableInfo = new TableInfo(this.page.getSize());
    	this.putInContext(TABLEINFO, tableInfo);
    	return result;
    }

    protected void popComponentStack() {
        super.popComponentStack();
        this.removeInContext(TABLEINFO);
    }
    
	public TableInfo getTableInfo() {
		return tableInfo;
	}

	protected void putInContext(String name, Object value) {
		stack.getContext().put(name, value);
	}

	protected void removeInContext(String name) {
		stack.getContext().remove(name);
	}

	protected String getUri() {
		String surl = request.getAttribute("javax.servlet.forward.request_uri").toString();
		if(StringUtils.isBlank(action))
			return surl;
		
		if(action.startsWith("?")){
			String left = action.substring(1);
			if(left.equals("queryString")){
				StringBuffer url = new StringBuffer(surl);
				String q = request.getQueryString();
				if(!StringUtils.isBlank(q))
					url.append("?").append(q);
				surl = url.toString();
			}else{
				surl += "?" + this.findString(left);
			}
		}
		else
			surl = this.findString(action);
//		System.out.println("surl: " + surl);
		return surl;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDataSource() {
		return dataSource;
	}

	public Page getPage() {
		return page;
	}

	public void setColCount(String colCount) {
		this.colCount = colCount; 
	}

	public int getColCountValue() {
		return this.findValue(this.colCount, 1, Integer.class);
	}

	public Iterator getIterator() {
		return this.page.getResult().iterator();
	}

	public boolean isPagination() {
		return pagination;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isSkipIfNotIncluded() {
		return skipIfNotIncluded;
	}

	public void setSkipIfNotIncluded(boolean skipIfNotIncluded) {
		this.skipIfNotIncluded = skipIfNotIncluded;
	}

	public boolean isAjax() {
		return ajax;
	}

	public void setAjax(boolean ajax) {
		this.ajax = ajax;
	}
	
    @StrutsTagAttribute(description="The css class to use for element")
    public void setCssClass(String cssClass) {
    	if(StringUtils.isBlank(cssClass)){
    		this.cssClass = DEFAULT_CLASS;
    	}else{
    		this.cssClass = DEFAULT_CLASS + " " + cssClass;
    	}
    }
    
    public static class TableInfo {
    	private int size;
    	private  int index;
    	
		public TableInfo(int size) {
			super();
			this.size = size;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
    	
    }

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

}
