package org.onetwo.guava;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;

import test.entity.UserEntity;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

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
	
	/***
	 * 差集
	 */
	@Test
	public void testDiffence(){
		HashSet<Integer> alist = Sets.newHashSet(1, 2, 3, 5);
		HashSet<Integer> blist = Sets.newHashSet(1, 2, 6, 7);
		Set<Integer> dlist = Sets.difference(alist, blist);
		System.out.println("dlist:" + dlist);
		Assert.assertEquals(dlist.size(), 2);
		Assert.assertTrue(ArrayUtils.contains(dlist.toArray(), 3));
		Assert.assertTrue(ArrayUtils.contains(dlist.toArray(), 5));
		dlist = Sets.difference(blist, alist);
		System.out.println("dlist:" + dlist);
		Assert.assertTrue(ArrayUtils.contains(dlist.toArray(), 6));
		Assert.assertTrue(ArrayUtils.contains(dlist.toArray(), 7));
	}
	

	@Test
	public void testIntersection(){
		HashSet<Integer> alist = Sets.newHashSet(1, 2, 3, 5);
		HashSet<Integer> blist = Sets.newHashSet(1, 2, 6, 7);
		Set<Integer> interSet = Sets.intersection(alist, blist);
		System.out.println("interSet:" + interSet);
		Assert.assertEquals(interSet.size(), 2);
		Assert.assertTrue(ArrayUtils.contains(interSet.toArray(), 1));
		Assert.assertTrue(ArrayUtils.contains(interSet.toArray(), 2));
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
	
	@Test
	public void testTransformAndSum(){
		List<UserEntity> all = LangUtils.newArrayList();
		List<UserEntity> aa = createUserList("aa", 3);
		List<UserEntity> bb = createUserList("bb", 1);
		List<UserEntity> cc = createUserList("cc", 2);
		all.addAll(aa);
		all.addAll(bb);
		all.addAll(cc);
		
		Function<UserEntity, Integer> ageMapper = new Function<UserEntity, Integer>() {

			@Override
			public Integer apply(UserEntity input) {
				return input.getAge();
			}
			
		};
		int total = 0;
		for(Integer a : Iterables.transform(all, ageMapper)){
			total += a;
		}
		Assert.assertEquals(total, JFishList.wrap(all).sum("age").intValue());
	}
	
	@Test
	public void testCache() throws ExecutionException{
		LoadingCache<Long, UserEntity> userCaches = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS).maximumSize(400).build(new CacheLoader<Long, UserEntity>(){

			@Override
			public UserEntity load(Long key) throws Exception {
				UserEntity user = new UserEntity();
				System.out.println("new user: " + key);
				user.setId(key);
				return user;
			}
			
		});

		UserEntity user1 = userCaches.get(1L);
		UserEntity user2 = userCaches.get(1L);
		Assert.assertNotNull(user1);
		Assert.assertNotNull(user2);
		Assert.assertTrue(user1==user2);

		LangUtils.await(3);
		user2 = userCaches.get(1L);
		Assert.assertNotNull(user1);
		Assert.assertNotNull(user2);
		Assert.assertTrue(user1!=user2);
		Assert.assertEquals(user1.getId(), user2.getId());
	}
	
	@Test
	public void joinObject(){
		Object value1 = 1;
		String value2 = "test2";
		Object[] value3 = new Object[]{3, "test3"};
		List<?> value4 = LangUtils.newArrayList("test4", "test44");
		
		List<?> list = JFishList.newList().addObject(value1).addObject(value2).addObject(value3).addObject(value4);
		System.out.println("list: " + list);
		Assert.assertEquals("[1, test2, 3, test3, test4, test44]", list.toString());
		
		List<Object> list2 = new ArrayList<Object>();
		list2.add(value1);
		list2.add(value2);
		list2.add(value3);
		list2.add(value4);
		
		list = JFishList.newList().flatAddObject(list2);
		System.out.println("list: " + list);
		Assert.assertEquals("[1, test2, 3, test3, test4, test44]", list.toString());
		
	}

}
