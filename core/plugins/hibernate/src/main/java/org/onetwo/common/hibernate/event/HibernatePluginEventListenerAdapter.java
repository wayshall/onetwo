package org.onetwo.common.hibernate.event;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;

abstract public class HibernatePluginEventListenerAdapter extends JFishContextPluginListenerAdapter implements HibernatePluginEventListener {

	public HibernatePluginEventListenerAdapter(AbstractContextPlugin<?> contextPlugin) {
		super(contextPlugin);
	}

	@Override
	abstract  public void listening(EntityPackageRegisterEvent event);
	
}
