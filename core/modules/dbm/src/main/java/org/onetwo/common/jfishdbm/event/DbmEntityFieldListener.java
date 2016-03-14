package org.onetwo.common.jfishdbm.event;

import org.onetwo.common.utils.JFishProperty;


public interface DbmEntityFieldListener {
	
	public Object beforeFieldInsert(JFishProperty field, Object fieldValue);
	
	public Object beforeFieldUpdate(JFishProperty field, Object fieldValue);
	
}
