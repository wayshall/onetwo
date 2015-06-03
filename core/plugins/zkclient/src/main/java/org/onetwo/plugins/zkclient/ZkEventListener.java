package org.onetwo.plugins.zkclient;

import com.google.common.eventbus.Subscribe;

public interface ZkEventListener {
	
	@Subscribe
	public void onWatchedEvent(ZkclientEvent event);

}
