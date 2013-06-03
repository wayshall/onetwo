package org.onetwo.common.web.s2.tag.webtag.compenent;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.s2.tag.WebUIClosingBean;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings({"unchecked"})
public class Page extends WebUIClosingBean {
	public static final String VALUE_KEY = "pageNo";
	
	public static final String TEMPLATE_OPEN = "page-close";
	public static final String TEMPLATE_CLOSE = "page-close";
	
	private String var;
	private String target;
	private String href;
	
	private String dataSource;
	
	private Object actualHref;
	
	private org.onetwo.common.utils.Page page;
	
	private boolean popValue = false;
	
	public Page(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	@Override
	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		
		page = (org.onetwo.common.utils.Page)findValue(dataSource);
		
		if(page == null){
			page = (org.onetwo.common.utils.Page)findValue("page");
		}
		
		if(page == null) {
			throw new ServiceException("DateSource required");
		}
	}
	
	protected void findPropertyValue(){
		
		actualHref = this.parseLink(href);
		
		if(actualHref == null) {
			actualHref = findValue(href);
		}
		
		addParameter("href", actualHref);
		addParameter("target", target);
		addParameter("page", page);
	}
	
	protected String parseLink(String href){
		ValueStack stack = getStack();
		String hrefStr = null;
		try{
			//stack.getContext().put(VALUE_KEY, linkValue);
			if(StringUtils.isNotBlank(href))
				hrefStr = SiteConfig.getInstance().getProperty(href, href, false);
			else
				throw new ServiceException("not link mapped with the key:"+href);
		}catch(Exception e){
			logger.error("parse link error: href["+href+"]", e);
		}finally{
			stack.getContext().remove(VALUE_KEY);
		}
		return hrefStr;
	}

    protected Class getValueClassType() {
        return null;
    }

    public boolean start(Writer writer) {
        evaluateParams();
    	if(StringUtils.isNotBlank(var))
    		stack.getContext().put(var, this);
    	
    	findPropertyValue();
    	
    	return true;
    }
    
    public boolean usesBody(){
    	return true;
    }

    public boolean end(Writer writer, String body) {
    	ValueStack stack = getStack();
        try {
            addParameter("body", body);
            mergeTemplate(writer, buildTemplateName(template, getDefaultTemplate()));
        } catch (Exception e) {
            throw new StrutsException(e);
        }
        finally {
        	if(StringUtils.isNotBlank(var))
        		stack.getContext().remove(var);
        	if(popValue)
        		stack.pop();
            popComponentStack();
        }

        return false;
    }
	
	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE_CLOSE;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Object getActualHref() {
		return actualHref;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

}
