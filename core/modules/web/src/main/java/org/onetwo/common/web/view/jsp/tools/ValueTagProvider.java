package org.onetwo.common.web.view.jsp.tools;


public interface ValueTagProvider {

	public String getValueProvider();
	public Object getValue(ValueTag tag);

}
