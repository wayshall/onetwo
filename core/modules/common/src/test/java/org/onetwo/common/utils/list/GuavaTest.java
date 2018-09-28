package org.onetwo.common.utils.list;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.UserEntity;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

public class GuavaTest {
	
	@Test
	public void testSplit(){
		String line = "testa:testb：testc";
		List<String> datas = CUtils.iterableToList(Splitter.on(Pattern.compile(":|：")).trimResults().omitEmptyStrings().split(line));
		assertThat(datas.size()).isEqualTo(3);
		assertThat(datas.get(2)).isEqualTo("testc");
	}
	
	public UserEntity createUser(String userName, int age){
		UserEntity user = new UserEntity();
		user.setUserName(userName);
		user.setAge(age);
		return user;
	}
	
	public List<UserEntity> createUserList(String userName, int count){
		List<UserEntity> users = LangUtils.newArrayList(count);
		for (int i = 0; i < count; i++) {
			users.add(createUser(userName+i, i));
		}
		return users;
	}
	
	public List<UserEntity> createSameNameUserList(String userName, int count){
		List<UserEntity> users = LangUtils.newArrayList(count);
		for (int i = 0; i < count; i++) {
			users.add(createUser(userName, i));
		}
		return users;
	}
	

	@Test
	public void test(){
		Collection<String> list = Collections2.filter(Arrays.asList("testa", "testb"), new Predicate<String>(){

			@Override
			public boolean apply(String input) {
				return input.contains("a");
			}
			
		});
		org.junit.Assert.assertEquals(list.toArray()[0], "testa");
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
		List<UserEntity> aa = createSameNameUserList("aa", 3);
		List<UserEntity> bb = createSameNameUserList("bb", 1);
		List<UserEntity> cc = createSameNameUserList("cc", 2);
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
		
		Map<String, List<UserEntity>> userGroup = all.stream().collect(Collectors.groupingBy(u->u.getUserName()));
		System.out.println("userGroup:" + userGroup);
		Assert.assertEquals(3, userGroup.get("aa").size());
		Assert.assertEquals(1, userGroup.get("bb").size());
		Assert.assertEquals(2, userGroup.get("cc").size());
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
