package org.onetwo.common.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import test.entity.RoleEntity;
import test.entity.UserEntity;

public class JFishPropertyTest {
	
	@Test
	public void testPropertyParameterType(){
		JFishProperty prop = JFishPropertyFactory.create(UserEntity.class, "roles", true);
		Assert.assertEquals(List.class, prop.getType());
		Assert.assertEquals(RoleEntity.class, prop.getFirstParameterType());
		prop = JFishPropertyFactory.create(UserEntity.class, "roles.id", true);
		Assert.assertEquals(Long.class, prop.getType());
		prop = JFishPropertyFactory.create(UserEntity.class, "roles[0].id", true);
		Assert.assertEquals(Long.class, prop.getType());
	}

}
