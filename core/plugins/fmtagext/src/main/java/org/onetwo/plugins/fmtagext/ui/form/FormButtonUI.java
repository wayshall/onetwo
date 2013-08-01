package org.onetwo.plugins.fmtagext.ui.form;

import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.plugins.fmtagext.ui.ButtonUI;
import org.onetwo.plugins.fmtagext.ui.valuer.StringUIValuer;
import org.onetwo.plugins.fmtagext.ui.valuer.UIValuer;

public class FormButtonUI extends ButtonUI{

	public static final String HTTP_KEY = "http";
	private boolean locationHref;
	

	public FormButtonUI(String name, String label) {
		super("ui-form-button");
		this.label = label;
		this.name = name;
	}
	
	public boolean isLocationHref() {
		return locationHref;
	}
	public void setLocationHref(boolean locationHref) {
		this.locationHref = locationHref;
	}
	
	public String getCssClass(){
		if(isLocationHref()){
			return "";
		}else{
			return "form-button";
		}
	}

	protected UIValuer<?> createUIValuer(){
		return new StringUIValuer(this, value, valueFormat){
			public String getUIValue(Object viewValue) {
				String val = super.getUIValue(viewValue);
				
				if(val!=null && !val.startsWith(HTTP_KEY)){
					val = BaseSiteConfig.getInstance().getBaseURL()+val;
				}
				return val;
			}
		};
	}
}
