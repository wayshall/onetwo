package org.onetwo.common.hibernate.event;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;

public class HibernatePluginEventListenerAdapter extends JFishContextPluginListenerAdapter implements HibernatePluginEventListener {

	public HibernatePluginEventListenerAdapter(AbstractContextPlugin<?> contextPlugin) {
		super(contextPlugin);
	}

	@Override
	public void listening(EntityPackageRegisterEvent event) {
		registerEntityPackage(event.getPackages());
	}
	
	protected void registerEntityPackage(List<String> packages){
	}

}
