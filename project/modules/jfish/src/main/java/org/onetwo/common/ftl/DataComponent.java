package org.onetwo.common.ftl;

import org.onetwo.common.utils.map.BaseMap;

@SuppressWarnings("rawtypes")
public interface DataComponent {
	
	public Object fetchData(BaseMap params); 

}
