package org.onetwo.common.web.s2.tag.component;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsException;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.onetwo.common.exception.WebException;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.s2.tag.component.StrutsTable.TableRowable;
import org.onetwo.common.web.utils.StrutsUtils;

import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(name = "column", tldTagClass = "com.project.base.tag.StrutsColumnTag", description = "Render HTML Column tag.", allowDynamicAttributes = true)
public class StrutsColumn extends TableUIBean {

	public static final String OPEN_TEMPLATE = "column";
	public static final String CLOSE_TEMPLATE = "column-close";
	public static final String ONE_TEMPLATE = "column-one";
	
	protected boolean autoValue;
	protected String sortable = "false";
	protected TableRowable parentRow;

	protected int colspan;
	protected int rowspan;
	
	protected String dataType;
	
	protected String dataFormat;
	
	protected String link;
	
	public StrutsColumn(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		this.parentRow = this.findAncestorByType(TableRowable.class);
		if (parentRow == null)
			throw new WebException("column标签必须包含在row标签内！");
		
			this.addParameter("parentRow", this.parentRow);
			this.addParameter("dataSource", this.parentRow.getParentTable().getDataSource());
			this.addParameter("tableName", this.parentRow.getParentTable().getParameters().get("name"));
			this.addParameter("formName", this.parentRow.getParentTable().getParameters().get("formName"));
		
	}

	@Override
	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		this.addParameter("autoValue", autoValue);
		boolean s = this.findValue(sortable, false, boolean.class);
		
		this.addParameter("sortable", s);
		this.addParameter("colspan", colspan);
		this.addParameter("rowspan", rowspan);
		Object obj = findValue(link);
		if(obj!=null){
			String linkaddr = obj.toString();
			if(!StrutsUtils.isHttp(linkaddr) && linkaddr.startsWith("/")){
				linkaddr = SiteConfig.getInstance().getBaseURL() + linkaddr;
			}
			this.addParameter("link", linkaddr);
		}
		
		if(StringUtils.isNotBlank(dataType))
			this.addParameter("dataType", findString(dataType));
		
		if(StringUtils.isNotBlank(dataFormat))
			this.addParameter("dataFormat", findString(dataFormat));
		
		
		Boolean isParentRowHeader = false;
		if(parentRow!=null){
			isParentRowHeader = parentRow.isHeader();
			this.defaultUITheme = parentRow.getParentTable().getTheme();
			addParameter("page", parentRow.getParentTable().getPage());
		}
		addParameter("parentRowHeader", isParentRowHeader);

	}

	@Override
	public boolean start(Writer writer) {
		if(isNewIteratorColumn()){
			return true;
		}
		return super.start(writer);
	}

	public boolean end(Writer writer, String body) {
		if(isNewIteratorColumn()){
	        evaluateParams();
	        try {
				if(StringUtils.isNotBlank(body))
					addParameter("body", body);
				
	            mergeTemplate(writer, buildTemplateName(template, getDefaultTemplate()));
	        } catch (Exception e) {
	            throw new StrutsException(e);
	        }
	        finally {
	            popComponentStack();
	        }

	        return false;
		}
        return super.end(writer, body);
	}

	@Override
	public String getDefaultOpenTemplate() {
		return OPEN_TEMPLATE;
	}
	
	public boolean usesBody(){
		if(isNewIteratorColumn())
			return true;
		return false;
	}
	
	public boolean isNewIteratorColumn(){
//		return RowType.isNewIterator(this.parentRow.getType());
		return true;
	}

	@Override
	protected String getDefaultTemplate() {
		if(isNewIteratorColumn())
			return ONE_TEMPLATE;
		return CLOSE_TEMPLATE;
	}

    @StrutsTagAttribute(description="Whether to auto out value to the column", type="Boolean", defaultValue="true")
	public void setAutoValue(boolean autoValue) {
		this.autoValue = autoValue;
	}

	public void setSortable(String sortable) {
		this.sortable = sortable;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
