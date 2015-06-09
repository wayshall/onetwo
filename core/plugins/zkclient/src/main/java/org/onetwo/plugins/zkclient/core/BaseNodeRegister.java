package org.onetwo.plugins.zkclient.core;

import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.plugins.zkclient.ZkclientPluginConfig;
import org.slf4j.Logger;

public class BaseNodeRegister implements ZkEventListener {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private ZkclientPluginConfig zkclientPluginConfig;
	
	
	@Override
	public String getWatchedPath() {
		return zkclientPluginConfig.getBaseNode();
	}

	@Override
	public void doInNotMatchPath(ZkclientEvent event) {
		if(event.getWatchedEvent().getState()==KeeperState.SyncConnected){
			logger.info("zkclient has connected!");
			String basePath = zkclientPluginConfig.getBaseNode();
			Stat stat = event.getSource().exists(basePath, true);
//			Pair<String, Code> result = event.getSource().createPersistentSeq(basePath);
			if(stat==null){
				event.getSource().createPersistent(basePath);
			}
		}
	}

	@Override
	public void doInMatchPath(ZkclientEvent event) {
		if(event.getWatchedEvent().getType()==EventType.NodeCreated){
			logger.info("path has created : {}", event.getWatchedEvent().getPath());
			event.getSource().exists(event.getWatchedEvent().getPath(), true);
		}else{
			logger.info("trigger event: {} -> {}", event.getWatchedEvent().getPath(), event.getWatchedEvent().getType());
		}
	}	

	public void setZkclientPluginConfig(ZkclientPluginConfig zkclientPluginConfig) {
		this.zkclientPluginConfig = zkclientPluginConfig;
	}

}
