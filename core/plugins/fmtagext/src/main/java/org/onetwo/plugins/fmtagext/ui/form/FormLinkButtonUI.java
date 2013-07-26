package org.onetwo.plugins.fmtagext.ui.form;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtagext.ui.ButtonUI;
import org.onetwo.plugins.fmtagext.ui.UI.CssClassKeys;

public class FormLinkButtonUI extends ButtonUI {

	public FormLinkButtonUI(String name, String label) {
		super("ui-link-button");
		this.label = label;
		this.setName(name);
	}

	public String getHref(){
		return getDataAction();
	}
	
	public void setHref(String href){
		this.setDataAction(href);
	}

	public String getCssClass(){
		return CssClassKeys.DG_TOOLBAR_BUTTON_BATCH + " " + StringUtils.trimToEmpty(super.getCssClass());
	}
}
