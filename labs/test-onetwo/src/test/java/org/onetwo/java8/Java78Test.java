package org.onetwo.java8;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
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
//															Assert.assertEquals(init, b);
																		a.setAge(a.getAge()+b.getAge()); 
																		a.setHeight(a.getHeight()+b.getHeight()); 
																		return a;
																	});

		System.out.println("rs1: " + JFishList.wrap(all).sum("height").doubleValue());
		System.out.println("rs2: " + rs.getHeight().doubleValue());
		Assert.assertEquals(rs, init);
		Assert.assertEquals(JFishList.wrap(all).sum("age").intValue(), rs.getAge().intValue());
		Assert.assertTrue(JFishList.wrap(all).sum("height").doubleValue()==rs.getHeight().doubleValue());
		
		StringBuilder sb = new StringBuilder("[");
		StringBuilder sb2 = all.stream().reduce(sb, (str, user)-> {
														if(str.length()>1) str.append(","); return str.append(user.getUserName());}, 
														(str1, str2)->{
															System.out.println("str1:"+str1);
															System.out.println("str2:"+str2);
															return str1.append(str2);
														});
		Assert.assertEquals(sb, sb2);
		sb2.append("]");
		System.out.println("sb2: " + sb2);
		Assert.assertEquals("[aa,aa,aa,bb,cc,cc]", sb2.toString());
		
	}
	
	@Test
	public void testParallelReduce(){
		List<UserEntity> all = LangUtils.newArrayList();
		int index = 0;
		all.add(createUser("aa"+(index++), 1));
		all.add(createUser("bb"+(index++), 1));
		all.add(createUser("cc"+(index++), 1));
		all.add(createUser("dd"+(index++), 1));
		all.add(createUser("ee"+(index++), 1));
		all.add(createUser("ff"+(index++), 1));
		all.add(createUser("gg"+(index++), 1));
		all.add(createUser("hh"+(index++), 1));
		all.add(createUser("ii"+(index++), 1));
		all.add(createUser("jj"+(index++), 1));
		all.add(createUser("kk"+(index++), 1));
		StringBuilder sb = new StringBuilder("[");
		System.out.println(sb.hashCode()+" sb: " + sb);
		StringBuilder sb2 = all.stream().parallel().reduce(sb, (str, user)-> {
													System.out.println("accumulator thread: " + Thread.currentThread().getId()+", str["+str.hashCode()+"]-["+str+"], userName["+user.getUserName()+"]");
													if(sb.length()>1) 
														sb.append(","); 
													return sb.append(user.getUserName());
												}, 
												(str1, str2)->{
													System.out.println("combiner thread: " + Thread.currentThread().getId()+" "+str1.hashCode()+"->str1:"+str1+" ["+str2.hashCode()+"->str2:"+str2);
													return str1;
												});
		sb.append("]");
		System.out.println(sb.hashCode()+" sb: " + sb);
		System.out.println(sb2.hashCode()+" sb2: " + sb2);
		Assert.assertEquals(46, sb.length());
	}
	
	@Test
	public void testParallelReduce2(){
		List<UserEntity> all = LangUtils.newArrayList();
		int index = 0;
		all.add(createUser("aa"+(index++), 1));
		all.add(createUser("bb"+(index++), 1));
		all.add(createUser("cc"+(index++), 1));
		all.add(createUser("dd"+(index++), 1));
		all.add(createUser("ee"+(index++), 1));
		all.add(createUser("ff"+(index++), 1));
		all.add(createUser("gg"+(index++), 1));
		all.add(createUser("hh"+(index++), 1));
		all.add(createUser("ii"+(index++), 1));
		all.add(createUser("jj"+(index++), 1));
		all.add(createUser("kk"+(index++), 1));
		StringBuilder sb = new StringBuilder("[");
		StringBuilder sb2 = all.stream().parallel().reduce(sb, (str, user)-> {
													System.out.println("accumulator thread: " + Thread.currentThread().getId()+", str["+str.hashCode()+"]-["+str+"], userName["+user.getUserName()+"]");
													if(str.length()>1) 
														str.append(","); 
													return str.append(user.getUserName());
												}, 
												(str1, str2)->{
													System.out.println("combiner thread: " + Thread.currentThread().getId()+" "+str1.hashCode()+" str1:"+str1+" str2:"+str2);
													return str1;
												});
		Assert.assertEquals(sb, sb2);
		sb2.append("]");
		System.out.println(sb2.hashCode()+" sb2: " + sb2);
		Assert.assertEquals(46, sb2.length());
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
	
	@Test
	public void testListAsMap(){
		List<UserEntity> all = LangUtils.newArrayList();
		List<UserEntity> aa = createUserList("aa", 1);
		List<UserEntity> bb = createUserList("bb", 1);
		List<UserEntity> cc = createUserList("cc", 1);
		all.addAll(aa);
		all.addAll(bb);
		all.addAll(cc);
		
		Map<String, UserEntity> userMap = all.stream().sorted((u1, u2)-> -u1.getUserName().compareTo(u2.getUserName())).collect(Collectors.toMap(u->u.getUserName(), u->u));
		System.out.println("list: " + userMap);
		Assert.assertEquals("[aa, bb, cc]", userMap.keySet().toString());
	}
	
	@Test
	public void testMapMerge(){
		Map<String, String> map = new HashMap<>();
		String val = map.merge("test", "default", String::concat);
		Assert.assertEquals("default", val);
		
		val = map.merge("test", "-value1", String::concat);
		Assert.assertEquals("default-value1", val);
		
		val = map.merge("test", "-value", (v1, v2)-> null);
		Assert.assertTrue(!map.containsKey("test"));
	}
	
	@Test
	public void testMapCompute(){
		Map<String, String> map = new HashMap<>();
		String putValue = "putValue";
		String val = map.compute("test", (k, v)-> v==null?putValue:v+putValue);
		Assert.assertEquals(putValue, val);
		
		val = map.put("test", "putValue2");
		Assert.assertEquals("putValue", val);
		
		val = map.compute("test", (k, v)-> v==null?putValue:v+putValue);
		Assert.assertEquals("putValue2"+putValue, val);
		
		val = map.computeIfPresent("test2", (k, v)-> putValue);
		Assert.assertTrue(val==null);
		
		val = map.computeIfAbsent("test2", (v)-> putValue);
		Assert.assertEquals(putValue, val);
		val = map.computeIfPresent("test2", (k, v)-> v+putValue);
		Assert.assertEquals(putValue+putValue, val);
	}

}
