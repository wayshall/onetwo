package org.onetwo.plugins.fmtagext.ui.valuer;

import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.UIComponent;

public class StringUIValuer extends AbstractComponentUIValuer<String> {
	
	private Expression exp = FmUIComponent.VALUE_EXPR;
	private String format;

	public StringUIValuer(UIComponent ui){
		super(ui);
	}
	
	public StringUIValuer(UIComponent ui, String value) {
		this(ui, value, "");
	}
	
	public StringUIValuer(UIComponent ui, String value, String format) {
		super(ui, value);
		this.format = format;
	}
	
	protected boolean isExpresstion(){
		return exp.isExpresstion(value);
	}

	
	public DefaultUIValueProvider createUIValueProvider(Object viewObj){
		return new DefaultUIValueProvider(viewObj, format);
	}

	@Override
	public String getUIValue(Object viewValue) {
		if(value==null)
			return "";
		
		if(isExpresstion()){
			String val = exp.parseByProvider(value, createUIValueProvider(viewValue));
			return val==null?"":val;
		}else if(isProperty()){
			String propertyName = FmUIComponent.trimPropertyMark(value);
			DefaultUIValueProvider vp = createUIValueProvider(viewValue);
			return vp.findString(propertyName);
			
		}else{
			return value;
		}
	}


}
