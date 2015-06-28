package org.onetwo.common.spring.plugin.event;

import java.util.List;

import org.onetwo.common.spring.plugin.ContextPlugin;
import org.onetwo.common.spring.plugin.ContextPluginManager;
import org.onetwo.common.spring.plugin.ContextPluginMeta;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;

import com.google.common.eventbus.EventBus;

public class JFishContextEventBus {
	private EventBus contextEventBus = new EventBus(this.getClass().getSimpleName());
	final private ContextPluginManager<?> contextPluginManager;
	
	public JFishContextEventBus(ContextPluginManager<?> contextPluginManager) {
		super();
		this.contextPluginManager = contextPluginManager;
	}
	

	
	final public void registerListener(JFishContextPluginListener listener){
		contextEventBus.register(listener);
	}
	
	final public <T extends ContextPluginMeta> void registerListenerByPluginManager(ContextPluginManager<?> contextPluginManager){
		JFishList.wrap(contextPluginManager.getContextPlugins()).each(new NoIndexIt<ContextPlugin>() {

			@Override
			protected void doIt(ContextPlugin element) throws Exception {
				registerListener(element.getJFishContextPluginListener());
			}
			
		});
	}

	public <T extends ContextPluginMeta>  void postRegisterJFishContextClasses(final List<Class<?>> annoClasses){
		contextEventBus.post(new ContextConfigRegisterEvent(contextPluginManager, annoClasses));
	}

	public <T extends ContextPluginMeta>  void postContextRefreshFinished(){
		contextEventBus.post(new ContextRefreshFinishedEvent(contextPluginManager));
	}
	
	public void postEvent(Object event){
		contextEventBus.post(event);
	}

}
