package org.onetwo.common.hibernate.event;

import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;

import com.google.common.eventbus.Subscribe;

public interface HibernatePluginEventListener extends JFishContextPluginListener {

	@Subscribe
	void listening(EntityPackageRegisterEvent event);
}
