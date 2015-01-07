package org.onetwo.common.web.view.jsp.tools;


public interface ValueTagProvider {

	public String getValueType();
	public Object getValue(ValueTag tag);

}
