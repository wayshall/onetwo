package org.onetwo.common.web.s2.tag.component;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsException;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.onetwo.common.exception.WebException;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.TreeModel;
import org.onetwo.common.web.s2.tag.RowType;
import org.onetwo.common.web.s2.tag.component.StrutsTable.TableRowable;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings({"serial", "unchecked"})
@StrutsTag(name = "row", tldTagClass = "com.project.base.tag.StrutsRowTag", description = "render html row")
public class StrutsRow extends TableUIBean implements TableRowable {
	
	public static final String DEFAULT_CLASS_PREFIX = "struts_";

	public static final String OPEN_TEMPLATE = "row";
	public static final String TEMPLATE = "row-close";
	public static final String ONE_TEMPLATE = "row-one";

	protected String type;
	protected Iterator iterator;
	protected StrutsTable parentTable;
	protected boolean evalbody = true;
	
//	protected List<StrutsColumn> columns;

	public StrutsRow(ValueStack statck, HttpServletRequest request, HttpServletResponse reponse) {
		super(statck, request, reponse);
	}
	
	/*public void addColumn(StrutsColumn coluomn){
		if(this.columns == null)
			 this.columns = new ArrayList<StrutsColumn>();
		this.columns.add(coluomn);
	}
	
	public List<StrutsColumn> getColumns() {
		return columns;
	}*/

	@Override
	public void afterPropertySet() {
		super.afterPropertySet();
		parentTable = findAncestorByType(StrutsTable.class);
		if (isPage() && parentTable == null)
			throw new WebException("row-page 标签必须包含在table标签内！");
		
		if(RowType.TREE.equals(this.type)){
			List<TreeModel> list  = this.parentTable.getPage().getResult();
			List newList = new ArrayList();
			for(TreeModel tm : list){
				newList.addAll(tm.toList());
			}

			if(!isEvalbody())
				addParameter("rowDataList", newList);
			else
				this.iterator = newList.iterator();
			
		}else{
			if(!isEvalbody()){
				List dataList = this.parentTable.getPage().getResult();
				addParameter("rowDataList", dataList);
				this.putInContext(dataList);
			}else
				this.iterator = this.parentTable.getIterator();
		}
		addParameter("parentTable", parentTable);
		addParameter("tableName", parentTable.getParameters().get("name"));
		addParameter("formName", parentTable.getParameters().get("formName"));
		addParameter("isIteratorEmpty", (iterator == null || !iterator.hasNext()));
		
	}



	@Override
	public void evaluateExtraParams() {
		super.evaluateExtraParams();
		StrutsTable table = findAncestorByType(StrutsTable.class);
		this.defaultUITheme = table.getTheme();

		addParameter("colCount", table.getColCountValue());
		addParameter("type", this.type);
		addParameter("isHeader", this.isHeader());
		addParameter("isIterator", this.isIterator());
		addParameter("isPage", this.isPage());
		addParameter("isRow", this.isRow());
		
//		addParameter("columns", this.getColumns());
	}

	@Override
	public boolean start(Writer writer) {
		if(!isEvalbody()){
//			super.start(writer);
			return false;
		}
		if (!this.isIterator())
			return super.start(writer);

		ValueStack stack = getStack();

		if (iterator != null && iterator.hasNext()) {
			Object currentValue = iterator.next();
			stack.push(currentValue);

			putInContext(currentValue);
			super.start(writer);
			return true;
		}
		super.start(writer);
		return false;
	}

	public void putInContext(Object value) {
		if (StringUtils.isNotBlank(name))
			stack.getContext().put(name, value);
	}

	public void pop() {
		stack.pop();
	}

	public boolean endTag(Writer writer, String body) {
		return super.end(writer, body);
	}

	public boolean end(Writer writer, String body) {
		if (!this.isIterator())
			return false;

		if (iterator == null || !iterator.hasNext())
			return false;

		ValueStack stack = getStack();
		stack.pop();
		try {
            evaluateParams();
			if(StringUtils.isNotBlank(body))
				addParameter("body", body);
			
			mergeTemplate(writer, buildTemplateName(template, getDefaultTemplate()));
		} catch (Exception e) {
			throw new StrutsException(e);
		}
		
		Object currentValue = iterator.next();
		stack.push(currentValue);

		putInContext(currentValue);
		int index = this.parentTable.getTableInfo().getIndex();
		this.parentTable.getTableInfo().setIndex(index+1);

		evaluateParams();

		super.start(writer);
		return true;
	}

	public boolean isHeader() {
		return RowType.isHeader(type);
	}

	public boolean isIterator() {
		return RowType.isIterator(type);
	}

	public boolean isPage() {
		return RowType.isPage(type);
	}

	public boolean isRow() {
		return RowType.isRow(type);
	}

	@Override
	protected String getDefaultTemplate() {
		if (this.isPage())
			return MyUtils.append(this.type, "-", TEMPLATE);
		return TEMPLATE;
	}

	@Override
	public String getDefaultOpenTemplate() {
		if (this.isPage())
			return MyUtils.append(this.type, "-", OPEN_TEMPLATE);
		return OPEN_TEMPLATE;
	}

	public void setType(String type) {
		if (!RowType.VALUES.contains(type))
			this.type = RowType.VALUES.get(0);
		else
			this.type = type;
	}

	public StrutsTable getParentTable() {
		return parentTable;
	}
	
    @StrutsTagAttribute(description="The css class to use for element")
    public void setCssClass(String cssClass) {
    	if(cssClass==null)
    		cssClass = "";
    	String css = DEFAULT_CLASS_PREFIX + type;
    	this.cssClass = css + " " + cssClass;
    }

	public String getType() {
		return type;
	}

	public boolean isEvalbody() {
		return evalbody;
	}

	public void setEvalbody(boolean evalbody) {
		this.evalbody = evalbody;
	}

}
