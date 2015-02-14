package org.onetwo.common.spring.plugin.event;

import com.google.common.eventbus.Subscribe;


public interface JFishContextPluginListener {

	@Subscribe
	void listening(ContextConfigRegisterEvent event);

//	@Subscribe
//	void listening(EntityPackageRegisterEvent event);
}
