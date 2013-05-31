package org.onetwo.common.json;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;

import test.entity.UserEntity;

public class JsonMapperTest {

	public static class TestJsonData {
		private String user_name = "test json";
		private Date create_time = new Date();
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public Date getCreate_time() {
			return create_time;
		}
		public void setCreate_time(Date create_time) {
			this.create_time = create_time;
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class TestJsonData2 {
		private String user_name = "test json";
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
	}

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Before
	public void setup(){
//		objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
//		objectMapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	
	@Test
	public void testMapListToJson(){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("name1", "value1");
		map1.put("name2", "value2");
		list.add(map1);
		
		String json = JsonMapper.defaultMapper().toJson(list);
		System.out.println("json: " + json);
	}

	@Test
	public void testTestJsonData() throws Exception{
		TestJsonData u1 = new TestJsonData();
		u1.setCreate_time(new Timestamp(new Date().getTime()));
		u1.setUser_name("user_name");
		
		String json = JsonMapper.defaultMapper().toJson(u1);
		System.out.println("json: " + json);
		TestJsonData2 t2 = objectMapper.readValue(json, TestJsonData2.class);
		System.out.println("t2: " + JsonMapper.defaultMapper().toJson(t2));
	}
	
	protected UserEntity createUser(){
		UserEntity user = new UserEntity();
		user.setId(100l);
		user.setAge(11);
		user.setUserName("勺子");
		user.setBirthDay(new Date());
		user.setEmail("wayshall@163.com");
		return user;
	}
	
	
	@Test
	public void testPageJson() throws Exception{
		Page<TestJsonData> page = Page.create();
		List<TestJsonData> list = LangUtils.newArrayList();
		TestJsonData u1 = new TestJsonData();
		u1.setCreate_time(new Timestamp(new Date().getTime()));
		list.add(u1);
		list.add(new TestJsonData());
		list.add(new TestJsonData());
		page.setResult(list);
		Map<String, Page<TestJsonData>> map = LangUtils.newHashMap();
		map.put("data", page);
		
		String json = JsonMapper.defaultMapper().toJson(map);
		System.out.println("testPageJson: " + json);
	}
	
	@Test
	public void testJson() throws Exception{
		UserEntity user = createUser();
		String json = JsonMapper.defaultMapper().toJson(user);
		System.out.println("testJson: " + json);
		
		UserEntity u2 = JsonMapper.defaultMapper().fromJson(json, UserEntity.class);
		Assert.assertEquals(user.getId(), u2.getId());
		Assert.assertEquals(user.getUserName(), u2.getUserName());
	}
	
	@Test
	public void testJsonList() throws Exception{
		List<UserEntity> users = new ArrayList<UserEntity>();
		int size = 5;
		for(int i=0; i<size; i++){
			users.add(createUser());
		}
		String json = JsonMapper.defaultMapper().toJson(users);
		System.out.println("testJson: " + json);
		
		List<UserEntity> u2 = JsonMapper.defaultMapper().fromJsonAsList(json, UserEntity[].class);
		System.out.println(u2);
		Assert.assertEquals(size, u2.size());
	}
	
	@Test
	public void testJsonList2() throws Exception{
		List<UserEntity> users = new ArrayList<UserEntity>();
		int size = 5;
		for(int i=0; i<size; i++){
			users.add(createUser());
		}
		String json = JsonMapper.defaultMapper().toJson(users);
		System.out.println("testJson: " + json);
		
		List<UserEntity> u2 = JsonMapper.defaultMapper().fromJsonAsList(json);
		System.out.println(u2);
		Assert.assertEquals(size, u2.size());
	}
	
	@Test
	public void testJsonIgnoreNull() throws Exception{
		UserEntity user = createUser();

		String json = JsonMapper.ignoreNull().toJson(user);
		System.out.println("testJsonIgnoreNull: " + json);
		
		UserEntity u2 = JsonMapper.ignoreNull().fromJson(json, UserEntity.class);
		Assert.assertEquals(user.getId(), u2.getId());
		Assert.assertEquals(user.getUserName(), u2.getUserName());
		
	}
	
	@Test
	public void testJsonIgnoreEmpty() throws Exception{
		UserEntity user = createUser();

		String json = JsonMapper.ignoreEmpty().toJson(user);
		System.out.println("testJsonIgnoreEmpty: " + json);
		
		UserEntity u2 = JsonMapper.ignoreEmpty().fromJson(json, UserEntity.class);
		Assert.assertEquals(user.getId(), u2.getId());
		Assert.assertEquals(user.getUserName(), u2.getUserName());
		
	}
	
	@Test
	public void testJsonUpdate() throws Exception{
		UserEntity user = createUser();

		String json = "{\"id\":200,\"userName\":\"wayshall\",\"email\":null,\"age\":11,\"birthDay\":\"2012-07-02 15:26:10\", \"aa\":2}";
		System.out.println("testJsonIgnoreEmpty: " + json);
		
		UserEntity u2 = JsonMapper.defaultMapper().update(json, user);
		System.out.println("testJsonUpdate: " + u2.getBirthDay());
		Assert.assertEquals(Long.valueOf(200l), u2.getId());
		Assert.assertEquals("wayshall", u2.getUserName());
		
	}
	
	@Test
	public void testJson2Map() throws Exception{
		String json = "{id:200,'userName':\"wayshall\",\"email\":null,\"age\":11,\"birthDay\":\"2012-07-02 15:26:10\"}";
		System.out.println("testJsonIgnoreEmpty: " + json);
		
		Map<String, String> map = JsonMapper.defaultMapper().fromJson(json, Map.class);
		System.out.println("map: " + map);
		Assert.assertEquals(Integer.valueOf(200), map.get("id"));
		Assert.assertEquals("wayshall", map.get("userName"));
		
	}
	
	@Test
	public void testJsonIgnoreField() throws Exception{
		TestJsonBean bean = new TestJsonBean();
		bean.setName("test bean");
		bean.setEmail("test@email.com");
		bean.setBirthday(DateUtil.parse("2012-11-23"));

		JsonMapper jm = JsonMapper.ignoreEmpty();
		String json = jm.toJson(bean);
		System.out.println("testJsonIgnoreField: " + json);
		Assert.assertEquals("{\"email\":\"test@email.com\",\"birthday\":\"2012-11-23\"}", json);
	}
	
}
