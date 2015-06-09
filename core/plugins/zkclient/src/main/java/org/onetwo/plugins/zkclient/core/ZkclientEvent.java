package org.onetwo.plugins.zkclient.core;

import org.apache.zookeeper.WatchedEvent;

public class ZkclientEvent {

	final private WatchedEvent watchedEvent;
	final private Zkclienter source;
	public ZkclientEvent(WatchedEvent event, Zkclienter zooKeeper) {
		super();
		this.watchedEvent = event;
		this.source = zooKeeper;
	}
	public WatchedEvent getWatchedEvent() {
		return watchedEvent;
	}
	public Zkclienter getSource() {
		return source;
	}
	
}
