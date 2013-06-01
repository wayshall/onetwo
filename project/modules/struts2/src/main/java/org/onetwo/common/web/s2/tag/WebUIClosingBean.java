package org.onetwo.common.web.s2.tag;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.components.ClosingUIBean;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.params.TagParamsMap;
import org.onetwo.common.web.utils.RequestUtils;

import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;

@SuppressWarnings("unchecked")
abstract public class WebUIClosingBean extends ClosingUIBean {
	public static final String OPEN = "-open";
	public static final String CLOSE = "-close";

	public static final String DEFAULT_UI_THEME = "webtag";

	protected Logger logger = Logger.getLogger(this.getClass());

	protected Map params;
	
	public WebUIClosingBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		this.parameters = new TagParamsMap();
	}
	
	protected Object getParameter(Object key, Object def){
		return getParameters().get(key, def);
	}
	
	protected Object getConstantValue(String key){
		return getParameters().getConstantValue(key);
	}

	public String getTemplate(){
		if(StringUtils.isBlank(template))
			return template;
		if(template.startsWith("/"))
			return template;
		else if(template.startsWith("@")){
			return getTemplateDir()+"/"+getTheme()+"/"+template.substring(1);
		}
		String path = this.request.getRequestURI();
		path = RequestUtils.noContextPath(request, path);
		path = FileUtils.getParentpath(path);
		template = path + "/" + template;
		return template;
	}
	
	public TagParamsMap getParameters(){
		return (TagParamsMap)this.parameters;
	}

	public void afterPropertySet() {
		if (this.params != null) {
			for (Map.Entry<String, String> entry : (Collection<Map.Entry<String, String>>) params.entrySet()) {
				Object value = entry.getValue();// this.findValue(entry.getValue());
				if (value == null)
					continue;
				if(String.class.isAssignableFrom(value.getClass()))
					value = findValue(entry.getValue());
				this.getParameters().put(entry.getKey(), value == null ? entry.getValue() : value);
			}
		}
	}

	@Override
	protected void evaluateExtraParams() {
		this.setDefaultUITheme(DEFAULT_UI_THEME);
		super.evaluateExtraParams();
	}

	public <T> T findAncestorByType(Class<T> clazz) {
		T t = (T) super.findAncestor(clazz);
		return t;
	}

	public <T> T findValue(String exp, T def, Class<T> clazz) {
		T rs = null;
		try {
			rs = (T) this.findValue(exp, clazz);
		} catch (Exception e) {
			rs = def;
		}
		return rs;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	@Override
	public String getDefaultOpenTemplate() {
		return org.onetwo.common.utils.StringUtils.convert2UnderLineName(this.getClass().getSimpleName().toLowerCase()) + OPEN;
	}

	@Override
	protected String getDefaultTemplate() {
		return org.onetwo.common.utils.StringUtils.convert2UnderLineName(this.getClass().getSimpleName().toLowerCase()) + CLOSE;
	}
	
	protected String defaultProfileName(String name){
		if(StringUtils.isBlank(name))
			name = this.name==null?this.id:this.name;
		return name;
	}

	public void push(String name){
		UtilTimerStack.push(defaultProfileName(name));
	}
	
	public void pop(String name){
		UtilTimerStack.pop(defaultProfileName(name));
	}

}
