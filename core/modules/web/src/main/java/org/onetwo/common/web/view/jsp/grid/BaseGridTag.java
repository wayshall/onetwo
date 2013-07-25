package org.onetwo.common.web.view.jsp.grid;

import javax.servlet.jsp.tagext.BodyTagSupport;

@SuppressWarnings("serial")
public class BaseGridTag extends BodyTagSupport {
	
	public static final String VAR_PRIFEX = "__gridtag__";

	protected String getGridVarName(){
		return getTagVarName("gridTagBean__");
	}
	protected String getTagVarName(String name){
		return VAR_PRIFEX + name;
	}
}
