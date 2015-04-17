package org.onetwo.common.spring.plugin;

import java.util.List;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;
import org.slf4j.Logger;

abstract public class AbstractContextPlugin<T> implements ContextPlugin{
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	protected ContextPluginMeta pluginMeta;
	protected String appEnvironment;

	/*public void init(ContextPluginMeta pluginMeta) {
		this.pluginMeta = pluginMeta;
	}*/
	
	public String getPluginName(){
		return getPluginMeta().getPluginInfo().getName();
	}
	
	
	protected void initWithEnv(ContextPluginMeta pluginMeta, String appEnv) {
	}
	
	/****
	 * 改由eventbus实现监听后的适配
	 */
	@Override
	public JFishContextPluginListener getJFishContextPluginListener() {
		return new JFishContextPluginListenerAdapter(this);
	}

	@Override
	public boolean isEmptyPlugin() {
		return false;
	}

	@Override
	public void init(ContextPluginMeta pluginMeta, String appEnv) {
		this.pluginMeta = pluginMeta;
		this.appEnvironment = appEnv;
//		this.init(pluginMeta);
		this.initWithEnv(pluginMeta, appEnv);
		this.setPluginInstance((T)this);
	}



	abstract public void setPluginInstance(T plugin);


	public ContextPluginMeta getPluginMeta() {
		return pluginMeta;
	}


	/****
	 * @see JFishContextPluginListener
	 * @param annoClasses
	 */
	@Deprecated
//	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
	}

//	@Override
	/*public void registerEntityPackage(List<String> packages) {
	}*/

	/*@Override
	public <T> T getExtComponent(Class<T> extClasss) {
		return null;
	}*/
}
