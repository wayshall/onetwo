package org.onetwo.common.web.view.jsp.datagrid;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.filter.BaseInitFilter;
import org.onetwo.common.web.view.jsp.TagUtils;
import org.onetwo.common.web.view.jsp.grid.BaseGridTag;
import org.onetwo.common.web.view.jsp.grid.GridTagBean;

@SuppressWarnings("serial")
public class DataGridTag extends BaseGridTag<GridTagBean> {
	private static final String AJAX_POSTFIX = "Ajax";
	private static final String AJAX_INST_POSTFIX = "AjaxInst";
	
	private String template = TagUtils.getTagPage("datagrid/grid.jsp");
	private Object dataSource;
	private int colspan = 0;

	private String action;
	
	private boolean toolbar;
	private boolean generatedForm = true;
	private boolean pagination = true;
	
	private PaginationType paginationType = PaginationType.link;
	
	private boolean searchForm;
	

	private boolean ajaxSupported = false;
	private String ajaxZoneName;
	private String ajaxInstName;
	
	@Override
	public GridTagBean createComponent() {
		GridTagBean gbean = new GridTagBean();
		return gbean;
	}

	/*@Override
	public int doStartTag() throws JspException {
		Deque<HtmlElement> tagStack = new ArrayDeque<HtmlElement>(5);
		setTagStack(tagStack);
		return super.doStartTag();
	}*/

	@Override
	protected int endTag() throws JspException {
		try {
			BodyContent bc = this.getBodyContent();
			if(bc!=null){
				this.component.setBodyContent(bc.getString());
			}
			this.pageContext.include(getTemplate());
		} catch (Exception e) {
			throw new JspException("render grid error : " + e.getMessage(), e);
		} finally{
			clearComponentFromRequest(getGridVarName());
			
//			getTagStack().pop();
//			clearTagStackFromRequest();
		}
		return EVAL_PAGE;
	}
	
	
	protected void populateComponent() throws JspException{
		super.populateComponent();
		component.setPage(TagUtils.toPage(dataSource));
		component.setColspan(colspan);
		component.setAction(buildActionString());
		
		component.setQueryString(buildQueryString());
		component.setToolbar(toolbar);
		component.setPagination(pagination);
		component.setPaginationType(paginationType);
		component.setGeneratedForm(generatedForm);
		component.setSearchForm(searchForm);
		component.setAjaxSupported(ajaxSupported);
		
		ajaxZoneName = getName() + AJAX_POSTFIX;
		ajaxInstName = getName() + AJAX_INST_POSTFIX;
		component.setAjaxZoneName(ajaxZoneName);
		component.setAjaxInstName(ajaxInstName);
		
		setComponentIntoRequest(getGridVarName(), component);
	}

	protected String buildActionString() {
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		if(StringUtils.isNotBlank(action))
			return action;
		
		String surl = BaseSiteConfig.getInstance().getBaseURL()+(String)request.getAttribute(BaseInitFilter.REQUEST_URI);
		return surl;
	}

	protected String buildQueryString() {
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		String str = request.getQueryString();
		if(StringUtils.isBlank(str))
			return "";
		CasualMap params = new CasualMap(str);
		params.filter("pageNo");
		str = params.toParamString();
		return str;
	}
	
	
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = TagUtils.getViewPage(template);
	}

	public void setDataSource(Object dataSource) {
		this.dataSource = dataSource;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setToolbar(boolean toolbar) {
		this.toolbar = toolbar;
	}

	public void setPaginationType(String paginationType) {
		this.paginationType = PaginationType.valueOf(paginationType);
	}

	public void setGeneratedForm(boolean generatedForm) {
		this.generatedForm = generatedForm;
	}

	public void setPagination(boolean pagination) {
		this.pagination = pagination;
	}

	public void setSearchForm(boolean searchForm) {
		this.searchForm = searchForm;
	}

	public void setAjaxZoneName(String ajaxZoneName) {
		this.ajaxZoneName = ajaxZoneName;
	}

	public void setAjaxSupported(boolean ajaxSupported) {
		this.ajaxSupported = ajaxSupported;
	}

	public String getAjaxInstName() {
		return ajaxInstName;
	}
}
