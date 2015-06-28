package org.onetwo.common.spring.plugin.event;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


/***
 * 改由eventbus实现监听后的适配
 * @author way
 *
 */
public class JFishContextPluginListenerAdapter implements JFishContextPluginListener{
	
	final private AbstractContextPlugin<?> contextPlugin;
	
	public JFishContextPluginListenerAdapter(AbstractContextPlugin<?> contextPlugin) {
		super();
		this.contextPlugin = contextPlugin;
	}

	@Override
	public void listening(ContextConfigRegisterEvent event) {
		contextPlugin.onJFishContextClasses(event.getConfigClasses());
	}

	@Override
	public void listening(ContextRefreshFinishedEvent event) {
	}
	
	
}
