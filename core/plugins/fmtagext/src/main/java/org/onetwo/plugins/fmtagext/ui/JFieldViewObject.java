package org.onetwo.plugins.fmtagext.ui;

import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtag.JFieldShowable;
import org.onetwo.plugins.fmtagext.annotation.JFieldView;
import org.onetwo.plugins.fmtagext.uitils.UIType;

public class JFieldViewObject implements Comparable<JFieldViewObject>{
	private final JFishProperty field;
	private final JFieldView fieldView;
	
	public JFieldViewObject(JFishProperty field, JFieldView fieldView) {
		super();
		this.field = field;
		this.fieldView = fieldView;
	}
	
	public JFishProperty getField() {
		return field;
	}

	public JFieldView getFieldView() {
		return fieldView;
	}

	public boolean canShowIn(JFieldShowable type){
		return fieldView!=null && ArrayUtils.contains(fieldView.showable(), type);
	}
	
	public int getOrder(){
		int val = 0;
		if(fieldView!=null){
			val = fieldView.order();
		}
		return val;
	}
	
	public String getLabel(){
		String val = null;
		if(fieldView!=null){
			val = fieldView.label();
		}
		if(StringUtils.isBlank(val)){
			val = field.getName();
		}
		return val;
	}

	public String getFormValue(){
		String val = null;
		if(fieldView!=null){
			val = fieldView.formValue();
		}
		if(StringUtils.isBlank(val)){
			val = field.getName();
		}
		return FmUIComponent.asProperty(val);
	}

	public String getShowValue(){
		String val = null;
		if(fieldView!=null){
			val = fieldView.showValue();
		}
		if(StringUtils.isBlank(val)){
			val = field.getName();
		}
		return FmUIComponent.asProperty(val);
	}

	public String getName(){
		return field.getName();
	}
	
	public String getFormName(){
		String formName = null;
		if(fieldView!=null){
			formName = fieldView.formName();
		}
		if(StringUtils.isBlank(formName)){
			formName = field.getName();
		}
		return formName;
	}
	
	public UIType getFormui(){
		return fieldView.formui();
	}
	
	public String getFormat(){
		String format = null;
		if(fieldView!=null){
			format = fieldView.format();
		}
		return format;
	}
	
	public int getShowOrder(){
		return fieldView.order();
	}

	@Override
	public int compareTo(JFieldViewObject o) {
		return this.getShowOrder() - o.getShowOrder();
	}
	
	
}
