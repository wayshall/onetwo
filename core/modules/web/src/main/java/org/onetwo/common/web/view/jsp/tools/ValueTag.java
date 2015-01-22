package org.onetwo.common.web.view.jsp.tools;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.jsp.AbstractTagSupport;

import com.google.common.collect.ImmutableMap;

public class ValueTag extends AbstractTagSupport {

	private String value;
	private String provider;
	private boolean escape = true;
	

//	private boolean ignoreField;
	private final Map<String, ValueTagProvider> typeMap;
	
	public ValueTag(){
		Map<String, ValueTagProvider> temp = LangUtils.newHashMap();
		List<ValueTagProvider> typeList = SpringApplication.getInstance().getBeans(ValueTagProvider.class);
		for(ValueTagProvider type : typeList){
			temp.put(type.getValueProvider(), type);
		}
		typeMap = ImmutableMap.copyOf(temp);
	}
	
    public int doEndTag() throws JspException {
    	String result = "";
    	if(StringUtils.isNotBlank(provider)){
    		ValueTagProvider l = typeMap.get(provider);
    		Object val = l.getValue(this);
    		result = StringUtils.emptyIfNull(val);
    	}else{
    		result = ToolEl.web(value);
    	}
    	if(escape){
    		result = ToolEl.escape(result);
    	}
    	write(result);
    	return EVAL_PAGE;
    }


	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public boolean isEscape() {
		return escape;
	}

	public void setEscape(boolean escape) {
		this.escape = escape;
	}

}
 	