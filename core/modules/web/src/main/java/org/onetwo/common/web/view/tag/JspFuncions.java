package org.onetwo.common.web.view.tag;

import java.util.Collection;

public final class JspFuncions {
	
	public static Boolean contains(Collection<?> collection, Object element){
		if(collection==null)
			return false;
		return collection.contains(element);
	}
	
	private JspFuncions(){
	}

}
