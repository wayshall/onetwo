package org.onetwo.common.spring.web.mvc.view;

import java.util.Map;

public interface ControllerJsonFilter {
	
	void filterModel(Map<String, Object> model);

}
