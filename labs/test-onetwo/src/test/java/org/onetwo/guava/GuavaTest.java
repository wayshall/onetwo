package org.onetwo.guava;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

import test.entity.UserEntity;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;

public class GuavaTest {
	
	public UserEntity createUser(String userName, int age){
		UserEntity user = new UserEntity();
		user.setUserName(userName);
		user.setAge(age);
		return user;
	}
	
	public List<UserEntity> createUserList(String userName, int count){
		List<UserEntity> users = LangUtils.newArrayList(count);
		for (int i = 0; i < count; i++) {
			users.add(createUser(userName, i));
		}
		return users;
	}
	
	@Test
	public void testGroupBy(){
		List<UserEntity> all = LangUtils.newArrayList();
		List<UserEntity> aa = createUserList("aa", 3);
		List<UserEntity> bb = createUserList("bb", 1);
		List<UserEntity> cc = createUserList("cc", 2);
		all.addAll(aa);
		all.addAll(bb);
		all.addAll(cc);
		
		ImmutableListMultimap<String, UserEntity> groups = Multimaps.index(all, new Function<UserEntity, String>() {

			@Override
			public String apply(UserEntity input) {
				return input.getUserName();
			}
			
		});
		
		System.out.println("groups:" + groups);
		Assert.assertEquals(3, groups.get("aa").size());
		Assert.assertEquals(1, groups.get("bb").size());
		Assert.assertEquals(2, groups.get("cc").size());
	}

}
