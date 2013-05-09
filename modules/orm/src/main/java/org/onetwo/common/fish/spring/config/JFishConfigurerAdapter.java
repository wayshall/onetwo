package org.onetwo.common.fish.spring.config;

import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.spring.JFishDaoImplementor;

public class JFishConfigurerAdapter implements JFishConfigurer{

	@Override
	public JFishEntityManager jfishEntityManager(JFishDaoImplementor jfishDao) {
		return null;
	}

}
