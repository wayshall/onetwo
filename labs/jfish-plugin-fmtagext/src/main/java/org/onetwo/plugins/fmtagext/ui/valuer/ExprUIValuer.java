package org.onetwo.plugins.fmtagext.ui.valuer;


public interface ExprUIValuer<T> extends UIValuer<T>{
	
	public void setValue(String value);
	
	public String getValue();

}
