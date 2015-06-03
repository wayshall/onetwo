package org.onetwo.plugins.zkclient;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.eventbus.EventBus;

public class Zkclienter implements InitializingBean, Watcher{
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

//	@Resource
	private ZkclientPluginConfig zkclientPluginConfig;
	private ZooKeeper zooKeeper;

	private EventBus zkeventBus = new EventBus("zkeventBus");
//	private Zkclienter zkclienter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		zooKeeper = new ZooKeeper(zkclientPluginConfig.getServers(), zkclientPluginConfig.getSessionTimeout(),  this);
	}
	
	@Override
	public void process(WatchedEvent event) {
//		logger.info("zk event[{}] has trigged!", event.getType());
		if(event.getState()==KeeperState.SyncConnected){
			logger.info("zkclient has connected!");
		}
		zkeventBus.post(new ZkclientEvent(event, zooKeeper));
		logger.info("zkclient receive event[{}] from server, and zkeventBus has post it!", event.getType());
	}
	
	public Zkclienter register(ZkEventListener zkEventListener){
		this.zkeventBus.register(zkEventListener);
		return this;
	}

	public ZooKeeper getZooKeeper() {
		return zooKeeper;
	}

	public void setZkclientPluginConfig(ZkclientPluginConfig zkclientPluginConfig) {
		this.zkclientPluginConfig = zkclientPluginConfig;
	}


}
