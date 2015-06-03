package org.onetwo.plugins.zkclient.provider;

import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.javatuples.Pair;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.plugins.zkclient.ZkEventListener;
import org.onetwo.plugins.zkclient.ZkclientEvent;
import org.onetwo.plugins.zkclient.ZkclientPluginConfig;
import org.slf4j.Logger;

public class ModuleRegister implements ZkEventListener {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private ZkclientPluginConfig zkclientPluginConfig;
	
	@Override
	public void onWatchedEvent(ZkclientEvent event) {
		if(event.getWatchedEvent().getState()==KeeperState.SyncConnected){
			logger.info("zkclient has connected!");
			Pair<String, Code> result = event.getZkWrapper().createPersistentSeq(zkclientPluginConfig.getBaseNode());
			if(Code.NODEEXISTS!=result.getValue1()){
				throw new BaseException("create node["+zkclientPluginConfig.getBaseNode()+"] error : " + result.getValue1());
			}
		}
	}

	public void setZkclientPluginConfig(ZkclientPluginConfig zkclientPluginConfig) {
		this.zkclientPluginConfig = zkclientPluginConfig;
	}

}
