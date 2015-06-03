package org.onetwo.plugins.zkclient;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;
import org.onetwo.plugins.zkclient.utils.ZkWrapper;

public class ZkclientEvent {

	final private WatchedEvent watchedEvent;
	final private ZkWrapper zkWrapper;
	public ZkclientEvent(WatchedEvent event, ZooKeeper zooKeeper) {
		super();
		this.watchedEvent = event;
		this.zkWrapper = ZkWrapper.wrap(zooKeeper);
	}
	public WatchedEvent getWatchedEvent() {
		return watchedEvent;
	}
	public ZkWrapper getZkWrapper() {
		return zkWrapper;
	}
	
}
