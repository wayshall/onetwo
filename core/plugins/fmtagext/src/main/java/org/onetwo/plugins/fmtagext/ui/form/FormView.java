package org.onetwo.plugins.fmtagext.ui.form;

import org.onetwo.plugins.fmtagext.ui.AbstractDataComponent;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;


public class FormView extends AbstractDataComponent {
	
//	public static final String VAR_NAME = "__FormView__";

//	private final FormUI form;
	private BeanWrapper bw;
	private Object model;
	
	public FormView(FormUI form, Object model) {
//		this.form = form;
		super(form);
		this.model = model;
		if(model!=null)
			this.bw = PropertyAccessorFactory.forBeanPropertyAccess(model);
	}

	public Object getFieldValue(FormFieldUI field){
		return bw.getPropertyValue(field.getName());
	}

	public FormUI getForm() {
		return (FormUI)getComponent();
	}
	
	@Override
	public Object getData() {
		return model;
	}

}
