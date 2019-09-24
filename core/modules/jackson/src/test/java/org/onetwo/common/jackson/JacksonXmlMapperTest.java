package org.onetwo.common.jackson;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.jackson.JsonMapperTest.TestJsonData;
import org.onetwo.common.jackson.JsonMapperTest.TestJsonData2;
import org.onetwo.common.jackson.UserEntity.SubUserEntity;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.map.ParamMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Lists;

public class JacksonXmlMapperTest {


	private XmlMapper objectMapper = new XmlMapper();
	
	@Before
	public void setup(){
//		objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
//		objectMapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	

	@Test
	public void testSerialWithTypeList() throws Exception{
		JacksonXmlMapper jacksonXmlMapper = JacksonXmlMapper.ignoreNull();
		ObjectMapper mapper = jacksonXmlMapper.getObjectMapper();
		mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		
		List<StoreDetailVo> list = Lists.newArrayList();
		StoreDetailVo storeDetailVo = new StoreDetailVo();
		storeDetailVo.setStoreId(1L);
		list.add(storeDetailVo);
		String s = mapper.writeValueAsString(list);
		System.out.println(s);
	}
	
	@Test
	public void testWxXml() {
		String xml = FileUtils.readAsString(FileUtils.getResourcePath("wx_test.xml"));
		JacksonXmlMapper jacksonXmlMapper = JacksonXmlMapper.ignoreNull();
		Map<String, String> map = jacksonXmlMapper.fromXml(xml, Map.class);
		System.out.println("map: " + map);
		assertThat(map.get("appid")).isEqualTo("wx2421b1c4370ec43b");
		assertThat(map.get("mch_id")).isEqualTo("10000100");
	}
	
	/*@Test
	public void testSerialWithType(){
		JacksonXmlMapper json = JacksonXmlMapper.ignoreNull().enableTyping();
//		ObjectMapper objectMapper = json.getObjectMapper();
//		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);
//		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

		TestJsonData u1 = new TestJsonData();
		u1.setCreate_time(new Timestamp(new Date().getTime()));
		u1.setUser_name("user_name");
		
		String data = json.toXml(u1);
		System.out.println("data:"+data);
		
		Object u2 = json.fromXml(data, Object.class);
		assertThat(u2.getClass()).isEqualTo(u1.getClass());

		try {
			TestJsonData2 u3 = json.fromXml(data, TestJsonData2.class);
//			assertThat(u3.getUser_name()).isEqualTo(u1.getUser_name());
			Assert.fail();
		} catch (Exception e) {
			assertThat(e.getClass()).isEqualTo(JsonException.class);
		}
		TestJsonData d = new TestJsonData();
		d.setCreate_time(new Date());
		d.setUser_name("user_name");
		data = json.toXml(d);
		System.out.println("data:"+data);
		TestJsonData2 dd = JacksonXmlMapper.ignoreNull().fromXml(data, TestJsonData2.class);
		assertThat(dd.getUser_name()).isEqualTo(d.getUser_name());
		
		Map<String, TestJsonData> maps = new HashMap<>();
		maps.put("key1", u1);
		maps.put("key2", d);
		Object mapdata = maps;
		String jsondata = json.toXml(mapdata);
		System.out.println("json: " + jsondata);
	}*/
	
	/*@Test
	public void testSimple() throws Exception {
		JacksonXmlMapper mapper = JacksonXmlMapper.ignoreNull();
		
		XmlMapper xmlMapper = new XmlMapper();
		
		String jsonTest = mapper.toXml("test string");
		System.out.println("jsonTest:"+jsonTest);
		
		Object data = "child1-aa";
		String json = mapper.toXml(data);
		System.out.println("json:"+json);
		
		Object rs = mapper.fromXml(json, String.class);
		System.out.println("json: " + json + ", rs: " + rs);
		Assert.assertEquals(data, rs);
		
		data = Integer.valueOf(12123123);
		json = mapper.toXml(data);
		rs = mapper.fromXml(json, Integer.class);
		System.out.println("json: " + json + ", rs: " + rs);
		Assert.assertEquals(data, rs);
		
	}*/
	
	
	@Test
	public void testMapListtoXml(){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map1 = new LinkedHashMap<String, Object>();
		map1.put("name1", "value1");
		map1.put("name2", "value2");
		list.add(map1);
		
		String json = JacksonXmlMapper.defaultMapper().toXml(list);
		System.out.println("json: " + json);
		assertThat(json).isEqualTo("<ArrayList><item><name1>value1</name1><name2>value2</name2></item></ArrayList>");
	}

	@Test
	public void testTestJsonData() throws Exception{
		TestJsonData u1 = new TestJsonData();
		u1.setCreate_time(new Timestamp(new Date().getTime()));
		u1.setUser_name("user_name");
		
		String json = JacksonXmlMapper.defaultMapper().toXml(u1);
		System.out.println("json: " + json);
		TestJsonData2 t2 = objectMapper.readValue(json, TestJsonData2.class);
		System.out.println("t2: " + JacksonXmlMapper.defaultMapper().toXml(t2));
	}
	
	protected UserEntity createUser(){
		UserEntity user = new UserEntity();
		user.setId(100l);
		user.setAge(11);
		user.setUserName("勺子");
		user.setBirthDay(new Date());
		user.setEmail("userNameJsonTest@163.com");
		return user;
	}
	
	
	@Test
	public void testPageXml() throws Exception{
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
		
		String json = JacksonXmlMapper.defaultMapper().toXml(map);
		System.out.println("testPageJson: " + json);
	}
	
	
	@Test
	public void testJson() throws Exception{
		SubUserEntity user = new SubUserEntity();
		user.setId(100l);
		user.setAge(11);
		user.setUserName("勺子");
		user.setEmail("userNameJsonTest@163.com");
		user.setBirthDay2(DateUtils.parse("1984-01-01 11:11:11"));
		
		String json = JacksonXmlMapper.defaultMapper().toXml(user);
		Assert.assertTrue(json.contains("birthDay2"));
		Assert.assertTrue(json.contains("1984-01-01"));
		System.out.println("testJson: " + json);
		
		SubUserEntity u2 = JacksonXmlMapper.defaultMapper().fromXml(json, SubUserEntity.class);
		Assert.assertEquals(user.getId(), u2.getId());
		Assert.assertEquals(user.getUserName(), u2.getUserName());
		Assert.assertEquals(DateUtils.parse("1984-01-01"), u2.getBirthDay2());
		
		JacksonXmlMapper jacksonXmlMapper = JacksonXmlMapper.defaultMapper();
		jacksonXmlMapper.getObjectMapper()
				.setPropertyNamingStrategy(new SplitorPropertyStrategy());
		json = jacksonXmlMapper.toXml(user);
		Assert.assertTrue(json.contains("user_name"));
		Assert.assertTrue(json.contains("birth_day2"));
		Assert.assertTrue(json.contains("1984-01-01"));
		System.out.println("testJson2: " + json);
	}
	
	@Test
	public void testJsonList() throws Exception{
		List<UserEntity> users = new ArrayList<UserEntity>();
		int size = 5;
		for(int i=0; i<size; i++){
			users.add(createUser());
		}
		String json = JacksonXmlMapper.defaultMapper().toXml(users);
		System.out.println("testJsonList: " + json);
		
		List<UserEntity> u2 = JacksonXmlMapper.defaultMapper().getJsonMapper().fromJsonAsList(json);
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
		String json = JacksonXmlMapper.defaultMapper().toXml(users);
		System.out.println("testJsonList2: " + json);
		
		List<UserEntity> u2 = JacksonXmlMapper.defaultMapper().getJsonMapper().fromJsonAsList(json);
		System.out.println(u2);
		Assert.assertEquals(size, u2.size());
	}
	
	@Test
	public void testJsonIgnoreNull() throws Exception{
		UserEntity user = createUser();

		String json = JacksonXmlMapper.ignoreNull().toXml(user);
		System.out.println("testJsonIgnoreNull: " + json);
		
		UserEntity u2 = JacksonXmlMapper.ignoreNull().fromXml(json, UserEntity.class);
		Assert.assertEquals(user.getId(), u2.getId());
		Assert.assertEquals(user.getUserName(), u2.getUserName());
		
	}
	
	@Test
	public void testJsonIgnoreEmpty() throws Exception{
		UserEntity user = createUser();

		String json = JacksonXmlMapper.ignoreEmpty().toXml(user);
		System.out.println("testJsonIgnoreEmpty: " + json);
		
		UserEntity u2 = JacksonXmlMapper.ignoreEmpty().fromXml(json, UserEntity.class);
		Assert.assertEquals(user.getId(), u2.getId());
		Assert.assertEquals(user.getUserName(), u2.getUserName());
		
	}
	
	@Test
	public void testJsonUpdate() throws Exception{
		UserEntity user = createUser();

		System.out.println("userData: " + JacksonXmlMapper.defaultMapper().toXml(user));
		String json = "<UserEntity><id>200</id><userName>userNameJsonTest</userName><status/><email>userNameJsonTest@163.com</email><age>11</age><birthDay>2019-04-23 03:35:53</birthDay><height>0.0</height></UserEntity>";
		System.out.println("testJsonIgnoreEmpty: " + json);
		
		UserEntity u2 = JacksonXmlMapper.defaultMapper().getJsonMapper().update(json, user);
		System.out.println("testJsonUpdate: " + u2.getBirthDay());
		Assert.assertEquals(Long.valueOf(200l), u2.getId());
		Assert.assertEquals("userNameJsonTest", u2.getUserName());
		
	}
	
	@Test
	public void testJson2Map() throws Exception{
		Map<String, Object> dataMap = CUtils.asMap("id", 200, "email", null, "age", 11, "birthDay", "2012-07-02 15:26:10");
		String xmlString = JacksonXmlMapper.defaultMapper().toXml(dataMap);
		System.out.println("xml: " + xmlString);
		
		String json = "<HashMap><birthDay>2012-07-02 15:26:10</birthDay><id>200</id><email/><age>11</age></HashMap>";
		
		Map<String, String> map = JacksonXmlMapper.defaultMapper().fromXml(json, Map.class);
		System.out.println("map: " + map);
		Assert.assertEquals("200", map.get("id"));
		
	}
	
	@Test
	public void testJsonIgnoreField() throws Exception{
		TestJsonBean bean = new TestJsonBean();
		bean.setName("test bean");
		bean.setEmail("test@email.com");
		bean.setBirthday(DateUtils.parse("2012-11-23"));

		JacksonXmlMapper jm = JacksonXmlMapper.ignoreEmpty();
		String json = jm.toXml(bean);
		System.out.println("testJsonIgnoreField: " + json);
		Assert.assertEquals("<TestJsonBean><email>test@email.com</email><birthday>2012-11-23 00:00:00</birthday></TestJsonBean>", json);
	}
	

	@Test
	public void testTestDefaultBeanPropertyFilter() throws Exception{
		TestJsonBean bean = new TestJsonBean();
		bean.setName("test bean");
		bean.setEmail("test@email.com");
		bean.setBirthday(DateUtils.parse("2012-11-23"));

		JacksonXmlMapper jm = JacksonXmlMapper.ignoreEmpty();
		jm.getJsonMapper().defaultFiler(new TestDefaultBeanPropertyFilter());
		String json = jm.toXml(bean);
		System.out.println("testTestDefaultBeanPropertyFilter: " + json);
		Assert.assertEquals("<TestJsonBean><email>test@email.com</email><birthday>2012-11-23 00:00:00</birthday></TestJsonBean>", json);
	}
	
	@Test
	public void testCauseMap(){
		ParamMap map = new ParamMap();
		map.putElement("aa", "aavalue");
		map.putElement("bb", "bbvalue");
		String json = JacksonXmlMapper.ignoreEmpty().toXml(map);
		System.out.println("json: " +json);
		Assert.assertEquals("<ParamMap><aa>aavalue</aa><bb>bbvalue</bb></ParamMap>", json);
		

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("aa", "aavalue");
		map2.put("bb", "bbvalue");
		json = JacksonXmlMapper.ignoreEmpty().toXml(map2);
		System.out.println("json: " +json);
	}
	
	@Test
	public void testMaptoXml(){
		String json = "";
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("aa", "aavalue");
		map2.put("bb", "bbvalue");
		json = JacksonXmlMapper.ignoreEmpty().toXml(map2);
		System.out.println("json: " +json);
		Assert.assertEquals("<HashMap><aa>aavalue</aa><bb>bbvalue</bb></HashMap>", json);
		
		MapObjectTest mapObj = new MapObjectTest();
		mapObj.setId(11L);
		mapObj.setUserName("testUserName");
		map2.put("aa", "aavalue");
		map2.put("bb", "bbvalue");
		json = JacksonXmlMapper.ignoreEmpty().toXml(map2);
		System.out.println("json: " +json);
		Assert.assertEquals("<HashMap><aa>aavalue</aa><bb>bbvalue</bb></HashMap>", json);
		
		UserEntity user = new UserEntity();
		user.setId(11L);
		user.setUserName("testUser");
		MappingObjectTest mapping = new MappingObjectTest();
		mapping.put("aa", "aavalue");
		mapping.put("bb", "bbvalue");
		mapping.mapFrom(user)
				.mapping("id", "userId")
				.mapping("userName", "targetUserName")
				.bindValues();
		json = JacksonXmlMapper.ignoreEmpty().toXml(mapping);
		System.out.println("json: " +json);
		Map<String, Object> jsonMap = JacksonXmlMapper.ignoreEmpty().fromXml(json, Map.class);
		Assert.assertTrue(jsonMap.containsKey("aa"));
		Assert.assertTrue(jsonMap.containsKey("bb"));
		Assert.assertTrue(jsonMap.containsKey("userId"));
		Assert.assertTrue(jsonMap.containsKey("targetUserName"));
	}
	
	public static class EnumVo {
		private String field;
		private int intField;
		private EnumTest type;
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public int getIntField() {
			return intField;
		}
		public void setIntField(int intField) {
			this.intField = intField;
		}
		public EnumTest getType() {
			return type;
		}
		public void setType(EnumTest type) {
			this.type = type;
		}
		
	}
	@Test
	public void testEnum(){
		EnumVo v = new EnumVo();
		v.setField("fieldValue");
		v.setIntField(11);
		v.setType(EnumTest.FINISHED);
		
		String json = JacksonXmlMapper.ignoreNull().toXml(v);
		System.out.println("json: " + json);
		Assert.assertEquals("<EnumVo><field>fieldValue</field><intField>11</intField><type>FINISHED</type></EnumVo>", json);;
	}
}
