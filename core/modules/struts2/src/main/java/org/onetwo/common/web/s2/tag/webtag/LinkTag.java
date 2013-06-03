package org.onetwo.common.web.s2.tag.webtag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.web.s2.tag.WebUIClosingTag;
import org.onetwo.common.web.s2.tag.webtag.compenent.Link;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class LinkTag extends WebUIClosingTag {

	private String var;
	private String target;
	private String image;
	private String href;
	private String text;
	private String type;
	private Integer dataIndex;
	
	private String condition;

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new Link(stack, req, res);
	}

	protected void populateParams(){
		super.populateParams();
		Link link = (Link)getComponent();
		link.setTarget(target);
		link.setImage(image);
		link.setHref(href);
		link.setText(text);
		link.setType(type);
		link.setCondition(condition);
		link.setDataIndex(dataIndex);
		link.setVar(var);
		link.setId(id);
		link.setName(name);
	}
	

    public int doEndTag() throws JspException {
		int rs = EVAL_PAGE;
		try {
//	        component.end(pageContext.getOut(), getBody());
	        rs = super.doEndTag();
		} catch (Exception e) {
			Link link = (Link)getComponent();
			String msg = "parse link tag["+this.getClass()+", id:"+id+", name:"+name+", href="+href+", value="+value+", comp="+link.toString()+"] end error";
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

	public void setImage(String image) {
		this.image = image;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setDataIndex(Integer dataIndex) {
		this.dataIndex = dataIndex;
	}

	public void setVar(String var) {
		this.var = var;
	}

}
