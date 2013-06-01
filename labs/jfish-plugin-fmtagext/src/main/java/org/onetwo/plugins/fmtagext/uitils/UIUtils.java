package org.onetwo.plugins.fmtagext.uitils;

import org.onetwo.plugins.fmtagext.ui.JFieldViewObject;
import org.onetwo.plugins.fmtagext.ui.form.FormFieldUI;



public final class UIUtils {
	
	public static void setFormFieldProperties(FormFieldUI formField, JFieldViewObject viewObj){
		formField.setShowOrder(viewObj.getOrder());
		formField.setValueFormat(viewObj.getFormat());
	}
	
	private UIUtils(){}

}
