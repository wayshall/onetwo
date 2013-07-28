package org.onetwo.common.web.view.jsp.grid2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.filter.BaseInitFilter;
import org.onetwo.common.web.view.jsp.TagUtils;
import org.onetwo.common.web.view.jsp.grid.BaseGridTag;
import org.onetwo.common.web.view.jsp.grid.GridTagBean;

@SuppressWarnings("serial")
public class Grid2Tag extends BaseGridTag<GridTagBean> {
	
	private String template = TagUtils.getTagPage("grid2/grid.jsp");
	private Object dataSource;
	private int colspan = 0;

	private String action;
	
	private boolean toolbar;
	
	@Override
	public GridTagBean createComponent() {
		return new GridTagBean();
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			this.pageContext.include(getTemplate());
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
		component.setToolbar(toolbar);
		
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


}
