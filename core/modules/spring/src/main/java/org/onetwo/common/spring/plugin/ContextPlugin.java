package org.onetwo.common.spring.plugin;

import java.util.List;

public interface ContextPlugin<T extends ContextPluginMeta<? extends ContextPlugin<?>>> extends JFishPluginLifeCycleListener<T> {
	void onJFishContextClasses(List<Class<?>> annoClasses);

}
