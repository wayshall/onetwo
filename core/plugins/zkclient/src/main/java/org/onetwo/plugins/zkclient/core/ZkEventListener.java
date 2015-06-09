package org.onetwo.plugins.zkclient.core;

import org.onetwo.common.utils.Assert;

import com.google.common.eventbus.Subscribe;

public interface ZkEventListener {
	
	String getWatchedPath();
	
	@Subscribe
	default public void onWatchedEvent(ZkclientEvent event){
		String watchedPath = getWatchedPath();
		Assert.hasText(watchedPath, "subscribePath can not be blank!");
		String path = event.getWatchedEvent().getPath();
		if(watchedPath.equals(path)){
			doInMatchPath(event);
		}else{
			doInNotMatchPath(event);
		}
	}

	public void doInMatchPath(ZkclientEvent event);
	default public void doInNotMatchPath(ZkclientEvent event){
	}

}
