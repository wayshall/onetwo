package org.onetwo.common.fish.spring;

public interface JFishDaoLifeCycleListener {

	public void onInit(JFishDaoImplementor dao);
	public void onDestroy(JFishDaoImplementor dao);

}
