package org.onetwo.plugins.fmtagext.ui.valuer;

public interface UIValueProvider {

	public Object getValue(String expr);

	public String findString(String var);

}