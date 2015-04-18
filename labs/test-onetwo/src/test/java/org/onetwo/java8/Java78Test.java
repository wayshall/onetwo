package org.onetwo.java8;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Langs;
import org.onetwo.common.utils.list.JFishList;

import test.entity.UserEntity;

import com.google.common.collect.ImmutableMap;


public class Java78Test {

	public UserEntity createUser(String userName, int age){
		UserEntity user = new UserEntity();
		user.setUserName(userName);
		user.setAge(age);
		user.setHeight(Integer.valueOf(age).floatValue());
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
	public void test() {
		/*String path = "";
		try (BufferedReader br = new BufferedReader(new FileReader(path))){
			br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		new Thread(()->{
			System.out.println("test");
		});
		
		JFishList.wrap("aa", "bb").each((String e, int index)->{
			return true;
		});
		
		Stream.of("aa1", "aa2", "bb1", "bb2").filter((String str)->{
			return str.startsWith("aa");
		}).map((String str)->{
			return str+":map";
		}).forEach(System.out::println);
		
	}
	
	@Test
	public void mapToArray(){
		Map<String, Integer> map = ImmutableMap.of("aa", 1, "bb", 2, "cc", 3);
		Object[] arrys = Langs.toArray(map);
		Stream.of(arrys).forEach(System.out::println);
		
		Assert.assertEquals(6, arrys.length);
		Assert.assertEquals("aa", arrys[0]);
		Assert.assertEquals(1, arrys[1]);
	}

	@Test
	public void testSum(){
		List<UserEntity> all = LangUtils.newArrayList();
		List<UserEntity> aa = createUserList("aa", 3);
		List<UserEntity> bb = createUserList("bb", 1);
		List<UserEntity> cc = createUserList("cc", 2);
		all.addAll(aa);
		all.addAll(bb);
		all.addAll(cc);
		
		int total = all.stream().mapToInt(e->e.getAge()).sum();
		
		Assert.assertEquals(JFishList.wrap(all).sum("age").intValue(), total);
	}
	
	@Test
	public void testReduce(){
		List<UserEntity> all = LangUtils.newArrayList();
		List<UserEntity> aa = createUserList("aa", 3);
		List<UserEntity> bb = createUserList("bb", 1);
		List<UserEntity> cc = createUserList("cc", 2);
		all.addAll(aa);
		all.addAll(bb);
		all.addAll(cc);
		
		UserEntity init = new UserEntity();
		System.out.println("init: " + init);
		UserEntity rs = all.stream().reduce(init, (a, b)-> { 
															System.out.println("a:"+a);
															System.out.println("b:"+b);
															Assert.assertEquals(init, a);
															Assert.assertEquals(init, b);
																		a.setAge(a.getAge()+b.getAge()); 
																		a.setHeight(a.getHeight()+b.getHeight()); 
																		return a;
																	});

		System.out.println("rs: " + rs);
		Assert.assertEquals(rs, init);
		Assert.assertEquals(JFishList.wrap(all).sum("age").intValue(), rs.getAge().intValue());
		Assert.assertEquals(JFishList.wrap(all).sum("height").doubleValue(), rs.getHeight().doubleValue());
	}
	
	@Test
	public void testList2Array(){
		List<UserEntity> all = LangUtils.newArrayList();
		List<UserEntity> aa = createUserList("aa", 3);
		List<UserEntity> bb = createUserList("bb", 1);
		List<UserEntity> cc = createUserList("cc", 2);
		all.addAll(aa);
		all.addAll(bb);
		all.addAll(cc);
		
		List<String> list = all.stream().map(UserEntity::getUserName).collect(Collectors.toList());
		System.out.println("list: " + list);
		Assert.assertEquals("[aa, aa, aa, bb, cc, cc]", list.toString());
	}

}
