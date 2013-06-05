package org.onetwo.common.fish.spring;

public interface JFishDaoLifeCycleListener {

	public void onInit(JFishDao dao);
	public void onDestroy(JFishDao dao);

}
