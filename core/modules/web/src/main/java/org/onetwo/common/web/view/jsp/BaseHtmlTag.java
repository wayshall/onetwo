package org.onetwo.common.web.view.jsp;

import javax.servlet.jsp.JspException;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.web.view.HtmlElement;

@SuppressWarnings("serial")
abstract public class BaseHtmlTag<T extends HtmlElement> extends AbstractBodyTag {
	

	protected String id;
	protected String name;
	protected String title;
	protected String label;
	protected String cssStyle;
	protected String cssClass;
	protected String onclick;
	
	protected T component;


	protected String permission;
	protected boolean showable = true;
	protected boolean ignoreTag;

	protected boolean checkIgnoreField(){
		if(!showable)
			return true;
		return !checkPermission(permission);
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public void setShowable(boolean showable) {
		this.showable = showable;
	}
	
	
	abstract public T createComponent();
	
	
	
	@Override
	public int doStartTag() throws JspException {
		this.ignoreTag = this.checkIgnoreField();
		if(ignoreTag)
			return SKIP_BODY;
		
//		return EVAL_BODY_BUFFERED;
		component = createComponent();
		this.populateComponent();
		int rs = startTag();
		return rs;
	}
	
	protected int startTag()throws JspException {
		/*component = createComponent();
		this.populateComponent();*/
		return EVAL_BODY_BUFFERED;
	}
	
	protected int endTag()throws Exception {
		return EVAL_PAGE;
	}
	
	protected void populateComponent() throws JspException{
		component.setId(id);
		component.setName(name);
		component.setTitle(title);
		component.setLabel(label);
		component.setCssClass(cssClass);
		component.setCssStyle(cssStyle);
		component.setOnclick(onclick);
	}

	@Override
	public int doEndTag() throws JspException {
		if(ignoreTag)
			return EVAL_PAGE;
		
		try {
			return endTag();
		} catch (JspException e) {
			throw e;
		}catch (Exception e) {
			throw new BaseException("render tag error : " + e.getMessage());
		}
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	
	
}
