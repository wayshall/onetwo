package org.onetwo.common.web.view.jsp.grid;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.filter.BaseInitFilter;
import org.onetwo.common.web.view.jsp.TagUtils;

@SuppressWarnings("serial")
public class GridTag extends BaseGridTag<GridTagBean> {
	
	private String template = TagUtils.getTagPage("grid/grid.jsp");
	private Object dataSource;
	private int colspan = 0;

	private String action;
	
	@Override
	public GridTagBean createComponent() {
		return new GridTagBean();
	}
	

	@Override
	public int doStartTag() throws JspException {
//		Deque<HtmlElement> tagStack = new ArrayDeque<HtmlElement>();
//		setTagStack(tagStack);
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			this.pageContext.include(TagUtils.getTagPage(getTemplate()));
		} catch (Exception e) {
			throw new JspException("render grid error : " + e.getMessage(), e);
		} finally{
			clearComponentFromRequest(getGridVarName());
		}
		return EVAL_PAGE;
	}
	
	
	protected void populateComponent() throws JspException{
		super.populateComponent();
		component.setPage(TagUtils.toPage(dataSource));
		component.setColspan(colspan);
		component.setAction(buildActionString());
		
		component.setQueryString(buildQueryString());
		
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
		return TagUtils.getQueryStringFilterPageNo(request);
	}
	
	
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
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


}
