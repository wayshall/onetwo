package org.onetwo.plugins.fmtagext.ui.valuer;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.UIComponent;


public class SimpleComponentUIValuer extends AbstractComponentUIValuer<Object> {

	public SimpleComponentUIValuer(UIComponent ui) {
		super(ui);
	}

	public SimpleComponentUIValuer(UIComponent ui, String value) {
		super(ui, value);
	}

	public Object getUIValue(Object viewValue) {
		String value = getValue();
		if(StringUtils.isBlank(value)){
			return viewValue;
		}
		if(viewValue==null)
			return LangUtils.EMPTY_STRING;
		
		if(isProperty()){
			String propertyName = FmUIComponent.trimPropertyMark(value);
			return createUIValueProvider(viewValue).getValue(propertyName);
			
		}else{
			return value;
		}
	}
	
}
