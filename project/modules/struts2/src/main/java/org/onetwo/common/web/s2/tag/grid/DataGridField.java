package org.onetwo.common.web.s2.tag.grid;

import java.io.Writer;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.views.annotations.StrutsTag;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.WebException;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.utils.StrutsUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(name = "column", tldTagClass = "com.project.base.tag.StrutsColumnTag", description = "Render HTML Column tag.", allowDynamicAttributes = true)
public class DataGridField extends AbstractDataGridComponent {
	
	public static final String METHOD_PREFIX = "showModel";
	
	public static enum RenderState {
		begin,
		renderHeader,
		renderField,
		end
	}
	
	public static enum ValueType {
		auto,
		body,
		radio,
		checkbox,
		select
	}
	
	protected boolean sortable;
	
	protected DataGrid dataGrid;
	
	protected DataGridRow dataRow;

//	protected DataType dataType;
	
	protected String dataFormat;

	protected String defaultValue;
	
	protected String link;
	
	protected String valueFetcher;
	
	protected ValueType type = ValueType.auto;
	
	protected RenderState state;
	

	protected String condition;
	protected boolean appendTodg = true;
	

	public DataGridField(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		this.colspan = 1;
		this.dataRow = (DataGridRow)this.findAncestor(DataGridRow.class);
		if(dataRow!=null){
			this.dataGrid = dataRow.getDataGrid();
		}else{
			this.dataGrid = (DataGrid)this.findAncestor(DataGrid.class);
		}
		if (dataGrid == null)
			throw new WebException("column标签必须包含在datagrid标签内！");
	}

	@Override
	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		this.defaultUITheme = dataGrid.getTheme();
		
		if(StringUtils.isBlank(template) && (isRadio() || isCheckbox())){
			String temp = "/" + this.getTemplateDir() + "/" + getTheme() + "/" + DataGrid.DATAGRID_THEME+"-"+this.type.toString();
			this.setTemplate(temp);
		}
	}

	@Override
	public boolean start(Writer writer) {
		if(StringUtils.isNotBlank(condition)){
			appendTodg = (Boolean) getStack().findValue(this.condition);
			if(!appendTodg)
				return false;
		}
		setState(RenderState.begin);
        evaluateParams();
        return true;
	}
	
	public boolean end(Writer writer, String body) {
		try {
			this.bodyContent = body;
			if(!appendTodg)
				return false;
			setState(RenderState.end);
			addFieldToRow(this);
		} finally{
			this.popComponentStack();
		}
		return false;
	}
	
	public void addFieldToRow(DataGridField field){
		if(dataRow!=null && !this.dataRow.isCreatedByGrid())
			this.dataRow.addField(this);
		else
			this.dataGrid.addField(this);
	}
	
	public boolean usesBody(){
		return true;
	}
	
	/**********
	 * 在action查找方法执行
	 * @param entity
	 * @param action
	 * @return
	 */
	protected Object executeFetcher(Object entity, Object action){
		Object actualValue = null;
		//showModel
		Method fetcher = ReflectUtils.findMethod(true, action.getClass(), valueFetcher);
		if(fetcher!=null){
			actualValue = ReflectUtils.invokeMethod(fetcher, action);
			return actualValue;
		}
		if(entity!=null){
			//showModel(entity)
			fetcher = ReflectUtils.findMethod(true, action.getClass(), valueFetcher, entity.getClass());
			if(fetcher!=null){
				actualValue = ReflectUtils.invokeMethod(fetcher, action, entity);
				return actualValue;
			}
			//showModel(entity, dataGridField)
			fetcher = ReflectUtils.findMethod(true, action.getClass(), valueFetcher, entity.getClass(), DataGridField.class);
			if(fetcher!=null)
				actualValue = ReflectUtils.invokeMethod(fetcher, action, entity, this);
		}else{
			//showModel(dataGridField)
			fetcher = ReflectUtils.findMethod(true, action.getClass(), valueFetcher, DataGridField.class);
			if(fetcher!=null)
				actualValue = ReflectUtils.invokeMethod(valueFetcher, action, this);
		}
		if(fetcher==null)
			throw new ServiceException("can not find valueFetcher : " + valueFetcher);
		
		return actualValue;
	}
	
	protected Object autoValue(Object entity){
		Object actualValue = null;
		try {
			if(StringUtils.isNotBlank(valueFetcher)){
				Object action = ActionContext.getContext().getActionInvocation().getAction();
				if(action!=null){
					if(valueFetcher.startsWith(METHOD_PREFIX)){
						actualValue = executeFetcher(entity, action);
					}else
						throw new ServiceException("the method must named start with : " + METHOD_PREFIX);
				}
			}else{
				if(StringUtils.isBlank(value))
					value = name;
				actualValue = findValue(value);
			}
			
			if(actualValue==null && StringUtils.isNotBlank(defaultValue))
				actualValue = this.findValue(defaultValue);
			
			if(actualValue instanceof Date){
				actualValue = DateUtil.format(dataFormat, (Date)actualValue);
			}
			
			if(actualValue instanceof Number && this.dataFormat != null) {
				NumberFormat nf = new DecimalFormat(this.dataFormat);
				nf.setRoundingMode(RoundingMode.HALF_UP);
				actualValue = nf.format(actualValue);
			}
		} finally{
//			stack.pop();
		}
		return actualValue;
	}
	
	
	public Object getFieldValue(){
		return this.getFieldValue(null);
	}
	
	public Object getFieldValue(Object entity){
		Object actualValue = null;
		
		if(ValueType.body.equals(type)){
			actualValue = this.bodyContent;
		}else{
			actualValue = autoValue(entity);
		}
		
		return actualValue;
	}
	

    @Override
	protected String getDefaultTemplate() {
		return null;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		if(StringUtils.isNotBlank(link)){
			Object obj = findString(link);
			if(obj!=null){
				String linkaddr = obj.toString();
				if(!StrutsUtils.isHttp(linkaddr) && linkaddr.startsWith("/")){
					linkaddr = SiteConfig.getInstance().getBaseURL() + linkaddr;
				}
				return linkaddr;
			}
		}
		return link;
	}

	public void setValueFetcher(String valueFetcher) {
		this.valueFetcher = valueFetcher;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public DataGrid getDataGrid() {
		return dataGrid;
	}

	public void setType(String type) {
		if(StringUtils.isBlank(type))
			return ;
		this.type = ValueType.valueOf(type);
	}
	
	public boolean isAuto(){
		return ValueType.auto.equals(type);
	}
	
	public boolean isRadio(){
		return ValueType.radio.equals(type);
	}
	
	public boolean isCheckbox(){
		return ValueType.checkbox.equals(type);
	}
	
	public boolean isSelect(){
		return ValueType.select.equals(type);
	}
	
	public void setRenderHeader(){
		setState(RenderState.renderHeader);
	}
	
	public boolean isRenderHeader(){
		return RenderState.renderHeader.equals(state);
	}
	
	public void setRenderField(){
		setState(RenderState.renderField);
	}
	
	public boolean isRenderField(){
		return RenderState.renderHeader.equals(state);
	}

	public RenderState getState() {
		return state;
	}

	public void setState(RenderState state) {
		this.state = state;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

}
