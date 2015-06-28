package org.onetwo.common.web.view.jsp.grid;

import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.web.view.HtmlElement;
import org.onetwo.common.web.view.ThemeSetting;
import org.onetwo.common.web.view.jsp.BaseHtmlTag;

@SuppressWarnings("serial")
abstract public class BaseGridTag<T extends HtmlElement> extends BaseHtmlTag<T> {


	private ThemeSetting themeSetting;
	
	public BaseGridTag(){
		this.themeSetting = SpringApplication.getInstance().getBean(ThemeSetting.class, false);
	}
	
	protected String getGridVarName(){
		return GridTagBean.class.getSimpleName();
	}
	
	protected String getRowVarName(){
		return RowTagBean.class.getSimpleName();
	}

	public ThemeSetting getThemeSetting() {
		return themeSetting;
	}	
	
}
