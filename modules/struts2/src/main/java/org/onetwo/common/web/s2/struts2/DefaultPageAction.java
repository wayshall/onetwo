package org.onetwo.common.web.s2.struts2;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.s2.BaseAction;

@SuppressWarnings("serial")
public class DefaultPageAction extends BaseAction {
	
	public static String ACTION_NAME = StringUtils.convert2UnderLineName(DefaultPageAction.class.getSimpleName().replace("Action", ""), "-");
	
	public String execute(){
		return SUCCESS;
	}

}
