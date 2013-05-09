package org.onetwo.plugins.fmtag.directive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.onetwo.common.ftl.directive.HtmlElement;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtag.directive.DataRow.RowType;

@SuppressWarnings("rawtypes")
public class DataGrid extends HtmlElement {

	public static enum PagiPosition {
		None,
		PagebarOnly,//只渲染页脚
		GridBottom,//表格底部
		OuterGrid//表格外面
	}
	
	boolean debug;
	String requestUri;
	
	String cssClass;
	
	Page page;
	List<DataRow> rows = new ArrayList<DataRow>();
	private DataRow iteratorRow;
	int colspan = 0;
	String toolbar;
	private List<DataField> searchFields;
	
	private PagiPosition pagiPosition = PagiPosition.GridBottom;
	private String paginationTemplate = DataGridDirective.DEFAULT_PAGINATION;
	
	boolean ajax;
	String formName;
	String ajaxInstName;
	String zoneName;
	
	String dataSource;
	String formMethod;
	String action;
	String bodyContent;
	
	String themeDir;
	
	public DataGrid(){
	}
	
	public DataRow getIteratorRow() {
		if(iteratorRow==null){
			this.iteratorRow = createDefaultIteratorRow();
		}
		return iteratorRow;
	}

	public DataRow createDefaultIteratorRow() {
		DataRow row = new DataRow(RowType.iterator);
		if(!rows.contains(row)){
			row.renderHeader = true;
			addRow(row);
		}
		return row;
	}

	/*public void addIteratorField(DataField field){
		this.iteratorRow.addField(field);
		if(!this.rows.contains(iteratorRow)){
			this.iteratorRow.renderHeader = true;
			this.rows.add(iteratorRow);
		}
	}*/
	
	public void addRow(DataRow row){
		if(row.isIterator()){
			this.iteratorRow = row;
		}
		this.rows.add(row);
	}
	public DataRow getCurrentRow(){
		return rows.get(rows.size()-1);
	}
	public Page getPage() {
		return page;
	}
	public List<DataRow> getRows() {
		return rows;
	}
	public int getColspan() {
		if(colspan<1 && iteratorRow!=null){
			this.colspan = this.iteratorRow.getFields().size();
		}
		return colspan;
	}

	public boolean isDebug() {
		return debug;
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

	public boolean isAjax() {
		return ajax;
	}

	public String getFormName() {
		if(StringUtils.isBlank(formName)){
			this.formName = getActualName() + "Form";
		}
		return formName;
	}

	public String getAjaxInstName() {
		if(StringUtils.isBlank(ajaxInstName)){
			this.ajaxInstName = getActualName() + "AjaxInst";
		}
		return ajaxInstName;
	}
	
	public String getActualName(){
		String actualName = getName();
		if(actualName.indexOf('.')!=-1){
			actualName = actualName.replace('.', '_');
		}
		return actualName;
	}

	public String getDataSource() {
		return dataSource;
	}

	public String getPaginationTemplate() {
		return paginationTemplate;
	}

	public String getFormMethod() {
		return formMethod;
	}

	public String getActualFormMethod() {
		if(!"get".equalsIgnoreCase(formMethod))
			return "post";
		return "get";
	}

	public String getZoneName() {
		return zoneName;
	}

	public String getAction() {
		return action;
	}

	public String getToolbar() {
		return toolbar;
	}

	public String getRequestUri() {
		return requestUri;
	}
	
	public void addSearchField(DataField field){
		if(this.searchFields==null)
			this.searchFields = new ArrayList<DataField>();
		this.searchFields.add(field);
	}

	@SuppressWarnings("unchecked")
	public List<DataField> getSearchFields() {
		if(this.searchFields==null)
			return Collections.EMPTY_LIST;
		return searchFields;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public String getThemeDir() {
		return themeDir;
	}
	
	public void sortFieldsByShowOrder(){
		for(DataRow row : this.rows){
			Collections.sort(row.getFields());
		}
	}

	public boolean hasToolbar(){
		return StringUtils.isNotBlank(toolbar);
	}
	
	public List<String> getToolbars(){
		return getToolbarList();
	}
	
	/*****
	 * 如果以冒号开始，则以${__dg__.themeDir}为模板目录
	 * 否则，直接使用toolbar本身的路径
	 * @return
	 */
	public List<String> getToolbarList(){
		if(StringUtils.isNotBlank(toolbar) && toolbar.indexOf('|')!=-1){
			// lib/test.ftl|:delete
			return LangUtils.asList(StringUtils.split(toolbar, "|,"));
		}else{
			//兼容旧写法  example /lib/test.ftl:delete
			String[] strs = StringUtils.split(toolbar, ":");
			if(LangUtils.isEmpty(strs))
				return null;
			List<String> list = new ArrayList<String>();
			for(String str : strs){
				if(str.startsWith("/") || str.startsWith("[")){
					list.add(str);
				}else{
					list.add(":"+str);
				} 
			}
			return list;
		}
	}

//	private Map hiddenFields = new HashMap();
	
}
