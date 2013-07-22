package org.onetwo.common.jfish;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.example.app.model.member.entity.UserEntity;
import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.JFishQueryBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JFishQueryBuilderTest extends JFishBaseNGTest {
	
	@Resource
	private JFishEntityManager jfishEntityManager;
	
	private int insertCount=100;
	
	@Test(priority=-1)
	public void saveDatas(){
		List<UserEntity> users = new ArrayList<UserEntity>();
		for (int i = 0; i < insertCount; i++) {
			UserEntity user = createUserEntity(i, "test_query_builder");
			users.add(user);
		}
		jfishEntityManager.save(users);
	}
	
	@Test
	public void testQuery(){
		JFishQueryBuilder jq = JFishQueryBuilder.from(jfishEntityManager, UserEntity.class);
		List<UserEntity> dbusers = jq.list();
		Assert.assertTrue(dbusers.size()==insertCount);
	}
	
	@Test(priority=Integer.MAX_VALUE)
	public void removeAll(){
		jfishEntityManager.removeAll(UserEntity.class);
	}

}
