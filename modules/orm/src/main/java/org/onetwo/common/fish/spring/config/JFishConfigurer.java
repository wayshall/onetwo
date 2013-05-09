package org.onetwo.common.fish.spring.config;

import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.spring.JFishDaoImplementor;

public interface JFishConfigurer {
	
	public JFishEntityManager jfishEntityManager(JFishDaoImplementor jfishDao);

}
