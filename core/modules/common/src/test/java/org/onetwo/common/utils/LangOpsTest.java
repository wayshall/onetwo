package org.onetwo.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class LangOpsTest {
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
	public void testArrayToMap(){
		Object[] arrays = new Object[]{"key1", 1, "key2", "value2"};
		Map<String, Object> map = LangOps.arrayToMap(arrays);
		System.out.println("map:"+map);
		Assert.assertNotNull(map);
		Assert.assertEquals(Integer.valueOf(1), map.get("key1"));
		Assert.assertEquals("value2", map.get("key2"));
		
		arrays = new Object[]{"key1", 1, "key2", "value2", "key3"};
		map = LangOps.arrayToMap(arrays);
		System.out.println("map:"+map);
		Assert.assertTrue(map.size()==2);
		Assert.assertNotNull(map);
		Assert.assertEquals(Integer.valueOf(1), map.get("key1"));
		Assert.assertEquals("value2", map.get("key2"));
		Assert.assertFalse(map.containsKey("key3"));
		
		String[] keys = new String[]{"test1", "test3"};
		Map<String, Integer> arrayMap = LangOps.kvArrayToMap(keys, 
															new Integer[]{111, 333});
		assertThat(arrayMap.size()).isEqualTo(keys.length);
		assertThat(arrayMap).containsKeys(keys[0], keys[1]);
	}
	
	@Test
	public void testSum(){
		BigDecimal total = LangOps.sumBigDecimal(null, BigDecimalBox::getValue);
		Assert.assertEquals(BigDecimal.valueOf(0.0), total);
		
		List<BigDecimalBox> datas = Lists.newArrayList();
		total = LangOps.sumBigDecimal(datas, BigDecimalBox::getValue);
		Assert.assertEquals(BigDecimal.valueOf(0.0), total);
		
		datas.add(new BigDecimalBox(1.5));
		datas.add(new BigDecimalBox(3.5));
		datas.add(new BigDecimalBox(5.0));
		datas.add(new BigDecimalBox(5.0));
		total = LangOps.sumBigDecimal(datas, BigDecimalBox::getValue);
		Assert.assertEquals(BigDecimal.valueOf(15.0), total);
	}
	
	@Test
	public void testParseTimeUnit(){
		Pair<Integer, TimeUnit> tu = LangOps.parseTimeUnit("11mis");
		assertThat(tu.getLeft()).isEqualTo(11);
		assertThat(tu.getRight()).isEqualTo(TimeUnit.MILLISECONDS);
		
		tu = LangOps.parseTimeUnit("11s");
		assertThat(tu.getLeft()).isEqualTo(11);
		assertThat(tu.getRight()).isEqualTo(TimeUnit.SECONDS);
		
		tu = LangOps.parseTimeUnit("11m");
		assertThat(tu.getLeft()).isEqualTo(11);
		assertThat(tu.getRight()).isEqualTo(TimeUnit.MINUTES);
		
		tu = LangOps.parseTimeUnit("101h");
		assertThat(tu.getLeft()).isEqualTo(101);
		assertThat(tu.getRight()).isEqualTo(TimeUnit.HOURS);
		
		tu = LangOps.parseTimeUnit("11d");
		assertThat(tu.getLeft()).isEqualTo(11);
		assertThat(tu.getRight()).isEqualTo(TimeUnit.DAYS);
	}

	class BigDecimalBox {
		private BigDecimal value;

		public BigDecimalBox(double val) {
			super();
			this.value = BigDecimal.valueOf(val);
		}

		public BigDecimal getValue() {
			return value;
		}
		
	}
}
