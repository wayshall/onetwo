package org.onetwo.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.list.JFishList;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;



public class Java78Test {

	public UserEntity createUser(String userName, int age){
		UserEntity user = new UserEntity();
		user.setUserName(userName);
		user.setAge(age);
		user.setHeight(Integer.valueOf(age).floatValue());
		user.setId(Long.valueOf(age));
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
		Object[] arrys = LangOps.toArray(map);
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
		
		List<BigDecimal> datas = Lists.newArrayList();
		datas.add(BigDecimal.valueOf(2.3));
		datas.add(BigDecimal.valueOf(1.7));
		datas.add(BigDecimal.valueOf(5));
		datas.add(BigDecimal.valueOf(1));
		BigDecimal res = datas.stream()
								.reduce(BigDecimal.valueOf(0), (t, m)->t.add(m));
		Assert.assertFalse(BigDecimal.valueOf(10).equals(res));
		Assert.assertEquals(BigDecimal.valueOf(10).setScale(1), res);
		Assert.assertEquals(BigDecimal.valueOf(10.0), res);
		
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
	
	@Test
	public void testMapToInt(){
		List<UserEntity> all = LangUtils.newArrayList();
		List<UserEntity> aa = createUserList("aa", 1);
		List<UserEntity> bb = createUserList("bb", 1);
		List<UserEntity> cc = createUserList("cc", 1);
		all.addAll(aa);
		all.addAll(bb);
		all.addAll(cc);
		
		all.stream().mapToLong(u->u.getId())
					.collect(()->new LongCollectorTest(), (c, v)->{
						c.ids.add(v);
					}, (left, right)->{
						left.ids.addAll(right.ids);
					});
	}
	
	static class LongCollectorTest {
		private List<Long> ids = new ArrayList<>();
		
		public LongCollectorTest(){
			System.out.println("LongCollectorTest");
		}

	}
	
	@Test
	public void testStreamMatch(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);
		boolean res = list.stream()
							.allMatch(i->{
								System.out.println("allMatch:"+i);
								return i>0;
							});
		System.out.println("res:"+res);
		org.junit.Assert.assertFalse(res);
		
		res = list.stream()
						.anyMatch(i->{
							System.out.println("anyMatch:"+i);
							return i>0;
						});
		System.out.println("res:"+res);
		org.junit.Assert.assertTrue(res);
	}
	

	@Test
	public void testStreamMatchException(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);
		try {
			boolean res = list.stream()
								.parallel()
								.allMatch(i->{
									System.out.println("allMatch:"+i);
									if(true){
										throw new RuntimeException("error");
									}
									return true;
								});
			System.out.println("res:"+res);
			org.junit.Assert.fail("can be run here!");
		} catch (Exception e) {
			System.out.println("throw RuntimeException");
			org.junit.Assert.assertTrue(RuntimeException.class.isInstance(e));
		}
	}
	
	@Test
	public void testStreamEmptyMatch(){
		List<Integer> empty = new ArrayList<Integer>();
		boolean res = empty.stream()
							.allMatch(i->i>0);
		System.out.println("res:"+res);
		org.junit.Assert.assertTrue(res);
		
		res = empty.stream()
						.anyMatch(i->i>0);
		System.out.println("res:"+res);
		org.junit.Assert.assertFalse(res);
	}
	
	@Test
	public void testCompare(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(2);
		list.add(0);
		list.add(3);
		list.add(1);
		
		Collections.sort(list, Comparator.comparingInt(o->o));
		System.out.println("list:"+list);
		Assert.assertEquals("[0, 1, 2, 3]", list.toString());
		
		Collections.sort(list, Comparator.comparingInt(o->-o));
		System.out.println("list:"+list);
		Assert.assertEquals("[3, 2, 1, 0]", list.toString());
	}

}
