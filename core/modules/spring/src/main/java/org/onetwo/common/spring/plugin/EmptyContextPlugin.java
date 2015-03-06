package org.onetwo.common.spring.plugin;


public class EmptyContextPlugin extends AbstractContextPlugin<Object> {
	
	public EmptyContextPlugin(){
	}

	@Override
	public void setPluginInstance(Object plugin) {
		
	}

	@Override
	public boolean isEmptyPlugin() {
		return true;
	}

}
