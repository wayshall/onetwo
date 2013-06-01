package org.onetwo.common.web.s2.tag.webtag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.web.s2.tag.WebUIClosingTag;
import org.onetwo.common.web.s2.tag.webtag.compenent.Page;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class PageTag extends WebUIClosingTag {

	private String var;
	private String target;
	private String href;
	private String dataSource;
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new Page(stack, req, res);
	}

	protected void populateParams(){
		super.populateParams();
		Page page = (Page)getComponent();
		page.setTarget(target);
		page.setHref(href);
		page.setVar(var);
		page.setDataSource(dataSource);
	}
	

    public int doEndTag() throws JspException {
		int rs = EVAL_PAGE;
		try {
//	        component.end(pageContext.getOut(), getBody());
	        rs = super.doEndTag();
		} catch (Exception e) {
			String msg = "parse page tag["+this.getClass()+", name:"+name+", href="+href+"] end error";
			logger.error(msg, e);
			throw new ServiceException(msg);
		}
        return rs;
    }
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

}
