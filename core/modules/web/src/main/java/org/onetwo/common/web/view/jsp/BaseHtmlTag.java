package org.onetwo.common.web.view.jsp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.HtmlElement;
import org.springframework.beans.BeanWrapper;

@SuppressWarnings("serial")
abstract public class BaseHtmlTag<T extends HtmlElement> extends AbstractBodyTag implements DynamicAttributes {
	
	private static final String TAG_STACK_NAME = "tagStack";
	
	protected String id;
	protected String name;
	protected String title;
	protected String label;
	protected String cssStyle;
	protected String cssClass;
	protected String onclick;
	private Map<String, Object> dynamicAttributes = LangUtils.newHashMap();
	
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
	
	protected void setTagStack(Deque<HtmlElement> tagStack){
		setComponentIntoRequest(TAG_STACK_NAME, tagStack);
	}
	protected Deque<HtmlElement> getTagStack(){
		return (Deque<HtmlElement>)getComponentFromRequest(TAG_STACK_NAME, Deque.class);
	}
	protected void clearTagStackFromRequest(){
		clearComponentFromRequest(TAG_STACK_NAME);
	}
	
	protected <T extends HtmlElement> T getTopComponent(Class<T> clazz){
		Deque<T> stack = (Deque<T>)getTagStack();
		if(stack==null)
			return null;
		for(T b : stack){
			if(b.getClass()==clazz)
				return b;
		}
		return null;
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
		
		//set dynamic attributes
		BeanWrapper bw = SpringUtils.newBeanWrapper(component);
		for(Entry<String, Object> entry : this.dynamicAttributes.entrySet()){
			String prop = StringUtils.toJavaName(entry.getKey(), '-', false);
			if(bw.isWritableProperty(prop)){
				bw.setPropertyValue(prop, entry.getValue());
			}else{
				component.getDynamicAttributes().put(entry.getKey(), entry.getValue());
			}
		}
		
		Deque<HtmlElement> tagStack = getTagStack();
		if(tagStack==null){
			tagStack = new ArrayDeque<HtmlElement>();
			setTagStack(tagStack);
		}
		getTagStack().push(component);
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
			throw new BaseException("render tag error : " + e.getMessage(), e);
		} finally{
			getTagStack().pop();
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
	public T getComponent() {
		return component;
	}
	@Override
	public void setDynamicAttribute(String uri, String localName, Object value)
			throws JspException {
		this.dynamicAttributes.put(localName, value);
	}

	public Object getDynamicAttribute(String attr) {
		if(LangUtils.isEmpty(dynamicAttributes))
			return null;
		return dynamicAttributes.get(attr);
	}
}
