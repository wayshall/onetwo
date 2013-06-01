package org.onetwo.common.web.s2.tag.template;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.StringUtils;

import com.opensymphony.xwork2.util.ValueStack;

public class Replace extends BaseTemplateBean {

	protected String bodyContent;
	
	protected Page page;

	protected String condition;
	
	private boolean renderTag = true;
	
	public Replace(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		this.page = (Page)this.findAncestor(Page.class);
		if(page==null)
			throw new ServiceException("the replace tag must wrap by page tag!");
	}

	@Override
	public boolean start(Writer writer) {
		if(StringUtils.isNotBlank(condition)){
			renderTag = (Boolean) getStack().findValue(this.condition);
			return renderTag;
		}
		return true;
	}
	

	@Override
	public boolean end(Writer writer, String body) {
		if(!renderTag)
			return false;

		if(page.isIgnoreIfNotTemplate()){
			return super.end(writer, body, true);
		}else{
			this.bodyContent = body;
			putBodyInContext(name, bodyContent);
			return false;
		}
	}
	
	public boolean usesBody(){
		return true;
	}
	
	public String getName(){
		return name;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

}
