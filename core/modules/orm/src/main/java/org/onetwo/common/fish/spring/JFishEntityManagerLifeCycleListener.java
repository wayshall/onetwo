package org.onetwo.common.fish.spring;

import org.onetwo.common.fish.JFishEntityManager;

public interface JFishEntityManagerLifeCycleListener {

	public void onInit(JFishEntityManager dao);
	public void onDestroy(JFishEntityManager dao);

}
