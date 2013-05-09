package org.onetwo.common.web.s2.tag.template;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Include;
import org.apache.struts2.dispatcher.Dispatcher;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.s2.tag.WebUIClosingBean;

import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.ValueStack;

public class Page extends WebUIClosingBean {

	private String _extends;
	private boolean ignoreIfNotTemplate;
	
	public Page(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	public boolean isIgnoreIfNotTemplate() {
		return ignoreIfNotTemplate;
	}

	public void setIgnoreIfNotTemplate(boolean ignoreIfNotTemplate) {
		this.ignoreIfNotTemplate = ignoreIfNotTemplate;
	}

	@Override
	public boolean start(Writer writer) {
		return true;
	}

	@Override
	public boolean end(Writer writer, String body) {
		/*if(StringUtils.isBlank(_extends))
			throw new ServiceException("the page must extends a template page!");*/
		try {
			String page = findString(_extends);
			if(StringUtils.isBlank(page)){
				if(isIgnoreIfNotTemplate()){
					//ignore
				}else{
					LangUtils.throwBaseException("can not find the tempalte page : " + _extends);
				}
			}else{
				Include include = new Include(stack, request, response);
				Container container = Dispatcher.getInstance().getContainer();
		        container.inject(include);
				include.setValue(page);
				include.end(writer, "");
			}
		} catch (Exception e) {
			throw new ServiceException("render page error : " + e.getMessage());
		}
		return false;
	}

	public void setExtends(String _extends) {
		this._extends = _extends;
	}

}
