package org.onetwo.plugins.jsonrpc;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class DispatcherController extends PluginSupportedController {
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public Object dipatcher(){
		return null;
	}

}
