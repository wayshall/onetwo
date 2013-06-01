package org.onetwo.common.web.s2.tag.grid;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxanywhere.AAUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsException;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.web.utils.StrutsUtils;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings({ "unchecked", "serial" })
public class DataGrid extends AbstractDataGridComponent {
	public static final String TABLEINFO = "tableInfo";
	
	public static final String DATAGRID_TOOLBAR_KEY = "datagrid-toolbar";
	
	public static final String DEFAULT_VAR_KEY = "model";
	
	public static enum PagiPosition {
		None,
		PagebarOnly,//只渲染页脚
		GridBottom,//表格底部
		OuterGrid//表格外面
	}
	
	
	protected final static Map<String, Object> varkeys;
	
	static {
		Map<String, Object> vk = new HashMap<String, Object>();
		
		vk.put("cssKeys", new HashMap<String, String>(){
			{
				put("checkAll","dg-checkbox-all"); 
				put("checkField","dg-checkbox-field"); 
				put("buttonDelete","dg-button-delete"); 
				put("buttonEdit","dg-button-edit");  
				put("toolbarButtonDelete","dg-toolbar-button-delete"); 
				put("toolbarButtonEdit","dg-toolbar-button-edit"); 
			}
		});
		
		vk.put("pageKeys", new HashMap<String, String>(){
			{
				put("commonToolbar",DATAGRID_TOOLBAR_KEY); 
			}
		});
		
		varkeys = Collections.unmodifiableMap(vk);
	}
	
	protected String dataSource;
	protected Page page;
//	protected Iterator iterator;
	protected String action;

	protected PagiPosition pagiPosition = PagiPosition.GridBottom;
	protected String paginationTemplate = "common";
	
	protected boolean ajax;
	private String zoneName;
	
	protected TableInfo tableInfo;
	
	protected List<DataGridRow> rows = new ArrayList<DataGridRow>();
	protected DataGridRow iteratorRow ;
	
	protected String openTemplate = "datagrid-open";
	
	protected boolean hasForm = true;
	
	protected Object currentModel;

	protected String var;
	
	protected boolean includeCommonToolbar;
	

	protected String condition;
	private boolean renderTag = true;
	
	private Map hiddenFields = new HashMap();
	
	private String pageParamPrefix;
	
	public DataGrid(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		iteratorRow = new DataGridRow(stack, request, response, false);
		iteratorRow.setCreatedByGrid(true);
		iteratorRow.setType(DataGridRow.Type.iterator);
		
		this.cssClass = "dg-table ";
	}

	@Override
	public String getDefaultOpenTemplate() {
		if(StringUtils.isBlank(openTemplate))
			openTemplate = super.getDefaultOpenTemplate();
		return openTemplate;
	}

	public void setOpenTemplate(String openTemplate) {
		this.openTemplate = openTemplate;
	}

	@Override
	protected String getDefaultTemplate() {
		return super.getDefaultTemplate();
	}
	
	public Map<String, Object> getVarkeys(){
		return varkeys;
	}
	
	@Override
	public void afterPropertySet() {
		super.afterPropertySet();
		
		if(StringUtils.isBlank(var))
			this.var = DEFAULT_VAR_KEY;
		
		Object value = null;
		if (StringUtils.isBlank(dataSource))
			value = this.parameters.get("dataSource");
		else
			value = this.findValue(this.dataSource);

		if(value==null){
			this.dataSource = name;
			value = this.findValue(name);
		}
		
		if(value instanceof Page){
			page = (Page) value;
		}else{
			List list = null;
			list = MyUtils.asList(value);
			if(list==null) list = Collections.EMPTY_LIST;
			page = new Page();
			page.setResult(list);
			page.setTotalCount(list.size());
			page.setPageSize(list.size());
		}
	}
	
	protected String getActualName(){
		String actualName = findString(this.name);
		if(StringUtils.isBlank(actualName))
			actualName = this.name;
		if(actualName.indexOf('.')!=-1)
			actualName = actualName.replace('.', '_');
		return actualName;
	}

	@Override
	public void evaluateExtraParams() {
		super.evaluateExtraParams();
		
		String actualName = getActualName();
		
		if(ajax){
			String ajaxInstName = actualName + "AjaxInst";
			addParameter("ajaxInstName", ajaxInstName);
		}
		
		addParameter("action", this.getUri());
		addParameter("ajax", ajax);
		addParameter("formName", actualName+"Form");
		addParameter("zoneName", this.zoneName);
		
		
		if(StringUtils.isNotBlank(pageParamPrefix)){
			addParameter("dataSource", this.pageParamPrefix);
			addParameter("pageParamPrefix", this.pageParamPrefix);
		}else{
			addParameter("dataSource", this.dataSource);
			addParameter("pageParamPrefix", this.dataSource);
		}
		
		
		addParameter("page", page);
		
	}
    public boolean start(Writer writer) {
		if(StringUtils.isNotBlank(condition)){
			renderTag = (Boolean) getStack().findValue(this.condition);
			return renderTag;
		}
    	
    	boolean result = false;
    	if(isAjax())
    		ajaxStart(writer);
//		result = super.start(writer);
    	result = true;
    	tableInfo = new TableInfo(this.page.getSize());
    	this.putInContext(TABLEINFO, tableInfo);
    	return result;
    }

	protected void ajaxStart(Writer writer){
		try {
			String actualName = getActualName();
			this.zoneName = actualName + "Zone";
			writer.write(AAUtils.getZoneStartDelimiter(zoneName));
		} catch (IOException e) {
            logger.error("Could not open template", e);
            e.printStackTrace();
		}
		/*if (AAUtils.isAjaxRequest(request) && !AAUtils.getZonesToRefresh(request).contains(name))
			return false;
		else
			return true;*/
	}
	protected boolean ajaxEnd(Writer writer, String body) {
		try {
			writer.write(AAUtils.getZoneEndDelimiter(zoneName));
		} catch (IOException e) {
            logger.error("Could not open template", e);
            e.printStackTrace();
		} finally{
		}
		return true;
	}

    public boolean end(Writer writer, String body) {
    	if(!renderTag)
    		return false;
    	
    	for(DataGridRow r : this.rows){
    		if(r.isIterator()){
    			this.colspan = r.colspan;
    			break;
    		}
    	}
    	/*String dgEnd = "datagrid end :";
    	UtilTimerStack.push(dgEnd);*/

    	boolean rs = false;
		this.bodyContent = body;
		evaluateParams();
    	try {
//            super.end(writer, body, false);
            mergeTemplate(writer, buildTemplateName(template, getDefaultTemplate()));
//            super.end(writer, body);
		} catch (Exception e) {
			throw new StrutsException(e);
		} finally{
            popComponentStack();
//			UtilTimerStack.pop(dgEnd);
		}
    	if(isAjax())
    		ajaxEnd(writer, body);
    	return rs;
    }

    protected void popComponentStack() {
    	if(this.currentModel!=null)
    		popModel();
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
	
	public void addRow(DataGridRow row){
		this.rows.add(row);
	}

	protected String getUri() {
		String surl = request.getAttribute("javax.servlet.forward.request_uri").toString();
		if(StringUtils.isBlank(action))
			return surl;
		
		if(action.startsWith("?")){
			String left = action.substring(1);
			String[] symbols = StringUtils.split(left, "|");
			int index = 0;
			for(String symbol : symbols){
				if(StringUtils.isBlank(symbol))
					continue;
				String qstr = this.processUrlSymbol(symbol);
				if(StringUtils.isNotBlank(qstr)){
					if(index==0)
						surl += "?";
					else
						surl += "&";
					surl += qstr;
				}
			}
		}
		else{
			surl = this.findString(action);
		}
//		System.out.println("surl: " + surl);
		return surl;
	}
	
	protected String processUrlSymbol(String symbol){
		String surl = "";
		if(symbol.equals("queryString") || ":queryString".equals(symbol) || ":qstr".equals(symbol)){
			surl = request.getQueryString();
		}else if(":post2get".equals(symbol)){
			surl = StrutsUtils.getPostParametersWithout("page.", "aa", "*Page").toParamString();
		}else if(":post2hidden".equals(symbol)){
			this.hiddenFields.putAll(StrutsUtils.getPostParametersWithout("page.", "aa", "*Page"));
		}else{
			surl = this.findString(symbol);
		}
		return surl;
	}

	public List<DataGridRow> getRows() {
		return rows;
	}

	public void addField(DataGridField field){
		if(!this.rows.contains(this.iteratorRow))
			this.rows.add(iteratorRow);
		this.iteratorRow.addField(field);
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

	public Iterator getIterator() {
		return this.page.getResult().iterator();
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isAjax() {
		return ajax;
	}

	public void setAjax(boolean ajax) {
		this.ajax = ajax;
	}

	public void setName(String name){
		super.setName(name);
	}
	
    public static class TableInfo {
    	private int size;
    	private int index;
    	
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
    
    public boolean isPagiOnGridBottom(){
    	return PagiPosition.GridBottom.equals(this.pagiPosition);
    }
    
    public boolean isPagiOuterGrid(){
    	return PagiPosition.OuterGrid.equals(this.pagiPosition);
    }
    
    public boolean isPagination(){
    	return !PagiPosition.None.equals(this.pagiPosition);
    }
    
    public boolean isPagebarOnly(){
    	return PagiPosition.PagebarOnly.equals(this.pagiPosition);
    }

    /**********
     * zjk-number|OuterGrid : 在表格外面渲染zjk-number页码模板
     * @param pagination
     */
	public void setPagination(String pagination){
		if(StringUtils.isBlank(pagination))
			return ;
		String[] strs = StringUtils.split(pagination, "|");
		if(strs.length==1){
			try {
				this.pagiPosition = PagiPosition.valueOf(strs[0].trim());
			} catch (Exception e) {
				this.paginationTemplate = strs[0].trim();
			}
		} else {
			this.paginationTemplate = strs[0].trim();
			this.pagiPosition = PagiPosition.valueOf(strs[1].trim());
		}
		if(PagiPosition.PagebarOnly.equals(this.pagiPosition)){
			this.ajax = false;
		}
	}

	public String getPaginationTemplate() {
		return paginationTemplate;
	}

	public void setPaginationTemplate(String paginationTemplate) {
		this.paginationTemplate = paginationTemplate;
	}

	public boolean isHasForm() {
		return hasForm;
	}

	public void setHasForm(boolean hasForm) {
		this.hasForm = hasForm;
	}
	public <T> T getModel() {
		return (T)currentModel;
	}

	public boolean pushModel(Object entity) {
		try {
			this.tableInfo.index++;
			if(entity==null)
				return false;
			this.currentModel = entity;
			stack.push(entity);
			Object oldModel = stack.getContext().get(var);
			if (oldModel == null || (oldModel != null && !oldModel.equals(entity))) {
				stack.getContext().put(var, entity);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean popModel(){
		stack.pop();
		stack.getContext().remove(var);
		this.currentModel = null;
		return true;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getValue(){
		return this.value;
	}

	public Object getCurrentModel() {
		return currentModel;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public boolean isIncludeCommonToolbar() {
		return includeCommonToolbar;
	}

	public void setIncludeCommonToolbar(boolean includeCommonToolbar) {
		this.includeCommonToolbar = includeCommonToolbar;
		if(this.includeCommonToolbar){
			DataGridRow toolbar = new DataGridRow(stack, request, response, false);
			toolbar.setTemplate("@"+DATAGRID_TOOLBAR_KEY);
			this.rows.add(0, toolbar);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List getDataSourceList(){
		return page.getResult();
	}
	

    public void setCssClass(String cssClass) {
        this.cssClass += cssClass;
    }

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@SuppressWarnings("rawtypes")
	public Map getHiddenFields() {
		return hiddenFields;
	}

	public String getPageParamPrefix() {
		return pageParamPrefix;
	}

	public void setPageParamPrefix(String pageParamPrefix) {
		if(StringUtils.isNotBlank(pageParamPrefix))
			this.pageParamPrefix = pageParamPrefix;
	}
	
	
}
