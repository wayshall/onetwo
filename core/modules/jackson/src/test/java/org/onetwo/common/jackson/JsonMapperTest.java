package org.onetwo.common.jackson;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.jackson.UserEntity.SubUserEntity;
import org.onetwo.common.jackson.exception.JsonException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.map.ParamMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

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
	public void testSerialWithTypeList() throws Exception{
		JsonMapper jsonMapper = JsonMapper.ignoreNull();
		ObjectMapper mapper = jsonMapper.getObjectMapper();
		mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		
		List<StoreDetailVo> list = Lists.newArrayList();
		StoreDetailVo storeDetailVo = new StoreDetailVo();
		storeDetailVo.setStoreId(1L);
		list.add(storeDetailVo);
		String s = mapper.writeValueAsString(list);
		System.out.println(s);
		
		byte[] bytes = jsonMapper.toJsonBytes(list);
		List<StoreDetailVo> list2 = jsonMapper.fromJson(bytes, new TypeReference<List<StoreDetailVo>>() {
		});
		
		assertThat(list2).isEqualTo(list2);
		String str1 = LangUtils.newString(bytes);
		System.out.println("json str1: " + str1);
		

		TestJsonData u1 = new TestJsonData();
		u1.setCreate_time(new Timestamp(new Date().getTime()));
		u1.setUser_name("user_name");
		String str = jsonMapper.toJson(u1);
		System.out.println("json str: " + str);
		bytes = jsonMapper.toJsonBytes(u1);
		
		String str2 = LangUtils.newString(bytes);
		System.out.println("json str2: " + str2);
		
		/*Map jsonstr = jsonMapper.fromJson(bytes, Map.class);
		System.out.println("json map: " + jsonstr);*/
	}
	
	@Test
	public void testSerialWithType(){
		JsonMapper json = JsonMapper.ignoreNull().enableTyping();
//		ObjectMapper objectMapper = json.getObjectMapper();
//		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);
//		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

		TestJsonData u1 = new TestJsonData();
		u1.setCreate_time(new Timestamp(new Date().getTime()));
		u1.setUser_name("user_name");
		
		String data = json.toJson(u1);
		System.out.println("data:"+data);
		
		Object u2 = json.fromJson(data, Object.class);
		assertThat(u2.getClass()).isEqualTo(u1.getClass());

		try {
			TestJsonData2 u3 = json.fromJson(data, TestJsonData2.class);
//			assertThat(u3.getUser_name()).isEqualTo(u1.getUser_name());
			Assert.fail();
		} catch (Exception e) {
			assertThat(e.getClass()).isEqualTo(JsonException.class);
		}
		TestJsonData d = new TestJsonData();
		d.setCreate_time(new Date());
		d.setUser_name("user_name");
		data = json.toJson(d);
		System.out.println("data:"+data);
		TestJsonData2 dd = JsonMapper.ignoreNull().fromJson(data, TestJsonData2.class);
		assertThat(dd.getUser_name()).isEqualTo(d.getUser_name());
		
		Map<String, TestJsonData> maps = new HashMap<>();
		maps.put("key1", u1);
		maps.put("key2", d);
		Object mapdata = maps;
		String jsondata = json.toJson(mapdata);
		System.out.println("json: " + jsondata);
	}
	
	@Test
	public void testSimple(){
		
		String jsonTest = JsonMapper.toJsonString("test string");
		System.out.println("jsonTest:"+jsonTest);
		
		Object data = "child1-aa";
		String json = JsonMapper.toJsonString(data);
		Object rs = JsonMapper.fromJsonString(json, String.class);
		System.out.println("json: " + json + ", rs: " + rs);
		Assert.assertEquals(data, rs);
		
		data = Integer.valueOf(12123123);
		json = JsonMapper.toJsonString(data);
		rs = JsonMapper.fromJsonString(json, Integer.class);
		System.out.println("json: " + json + ", rs: " + rs);
		Assert.assertEquals(data, rs);
		
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
		user.setEmail("userNameJsonTest@163.com");
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
		SubUserEntity user = new SubUserEntity();
		user.setId(100l);
		user.setAge(11);
		user.setUserName("勺子");
		user.setEmail("userNameJsonTest@163.com");
		user.setBirthDay2(DateUtils.parse("1984-01-01 11:11:11"));
		
		String json = JsonMapper.defaultMapper().toJson(user);
		Assert.assertTrue(json.contains("birthDay2"));
		Assert.assertTrue(json.contains("1984-01-01"));
		System.out.println("testJson: " + json);
		
		SubUserEntity u2 = JsonMapper.defaultMapper().fromJson(json, SubUserEntity.class);
		Assert.assertEquals(user.getId(), u2.getId());
		Assert.assertEquals(user.getUserName(), u2.getUserName());
		Assert.assertEquals(DateUtils.parse("1984-01-01"), u2.getBirthDay2());
		
		JsonMapper jsonMapper = JsonMapper.defaultMapper();
		jsonMapper.getObjectMapper()
				.setPropertyNamingStrategy(new SplitorPropertyStrategy());
		json = jsonMapper.toJson(user);
		Assert.assertTrue(json.contains("user_name"));
		Assert.assertTrue(json.contains("birth_day2"));
		Assert.assertTrue(json.contains("1984-01-01"));
		System.out.println("testJson2: " + json);
		
		SubUserEntity subUser = jsonMapper.fromJson(json, SubUserEntity.class);
		assertThat(subUser.getBirthDay()).isNull();
		assertThat(DateUtils.formatDate(subUser.getBirthDay2())).isEqualTo("1984-01-01");
		assertThat(subUser.getEmail()).isEqualTo(user.getEmail());
	}
	
	
	@Test
	public void testAsNodeObject() throws Exception{
		UserEntity user = createUser();
		user.setBirthDay(DateUtils.parse("2019-01-22 17:47:00"));
		String json = JsonMapper.defaultMapper().toJson(user);
		System.out.println("testAsNodeObject: " + json);
		ObjectNode objectNode = JsonMapper.defaultMapper().fromJson(json);
		assertThat(objectNode.get("id").longValue()).isEqualTo(user.getId());
		assertThat(objectNode.get("userName").asText()).isEqualTo(user.getUserName());
		
		json = JsonMapper.ignoreNull().toJson(objectNode);
		System.out.println("json: " + json);
		String res = "{\"id\":100,\"userName\":\"勺子\",\"status\":null,\"email\":\"userNameJsonTest@163.com\",\"age\":11,\"birthDay\":\"2019-01-22 09:47:00\",\"height\":0.0}";
		assertThat(json).isEqualTo(res);
	}
	
	@Test
	public void testJsonList() throws Exception{
		List<UserEntity> users = new ArrayList<UserEntity>();
		int size = 5;
		for(int i=0; i<size; i++){
			users.add(createUser());
		}
		String json = JsonMapper.defaultMapper().toJson(users);
		System.out.println("testJsonList: " + json);
		
		List<UserEntity> u2 = JsonMapper.defaultMapper().fromJsonAsList(json);
		System.out.println(u2);
		Assert.assertEquals(size, u2.size());
		
		String jsonListStr = "[{\"attributeId\":\"718797279100964864\",\"fieldType\":\"MSELECT\",\"selectedValue\":[{\"id\":\"719926646329872384\",\"attributeId\":\"718797279100964864\",\"listOrder\":100,\"value\":\"500克\"},{\"id\":\"719926763585835008\",\"attributeId\":\"718797279100964864\",\"listOrder\":100,\"value\":\"1000克\"},{\"id\":\"719926681687855104\",\"attributeId\":\"718797279100964864\",\"listOrder\":90,\"value\":\"250\"}]},{\"attributeId\":\"722194076098510848\",\"fieldType\":\"MSELECT\",\"selectedValue\":[{\"id\":\"722194294009380864\",\"attributeId\":\"722194076098510848\",\"listOrder\":100,\"value\":\"大果\"}]}]";
		List<AttributeValueJsonTestVO> jsonList = JsonMapper.ignoreNull().fromJsonAsList(jsonListStr, AttributeValueJsonTestVO.class);
		System.out.println("jsonList: " + jsonList);
	}
	
	@Test
	public void testJsonList2() throws Exception{
		List<UserEntity> users = new ArrayList<UserEntity>();
		int size = 5;
		for(int i=0; i<size; i++){
			users.add(createUser());
		}
		String json = JsonMapper.defaultMapper().toJson(users);
		System.out.println("testJsonList2: " + json);
		
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

		String json = "{\"id\":200,\"userName\":\"userNameJsonTest\",\"email\":null,\"age\":11,\"birthDay\":\"2012-07-02 15:26:10\", \"aa\":2}";
		System.out.println("testJsonIgnoreEmpty: " + json);
		
		UserEntity u2 = JsonMapper.defaultMapper().update(json, user);
		System.out.println("testJsonUpdate: " + u2.getBirthDay());
		Assert.assertEquals(Long.valueOf(200l), u2.getId());
		Assert.assertEquals("userNameJsonTest", u2.getUserName());
		
	}
	
	@Test
	public void testJson2Map() throws Exception{
		String json = "{id:200,'userName':\"userNameJsonTest\",\"email\":null,\"age\":11,\"birthDay\":\"2012-07-02 15:26:10\"}";
		System.out.println("testJsonIgnoreEmpty: " + json);
		
		Map<String, String> map = JsonMapper.defaultMapper().allowSingleQuotes().fromJson(json, Map.class);
		System.out.println("map: " + map);
		Assert.assertEquals(Integer.valueOf(200), map.get("id"));
		Assert.assertEquals("userNameJsonTest", map.get("userName"));
		
	}
	
	@Test
	public void testJsonIgnoreField() throws Exception{
		TestJsonBean bean = new TestJsonBean();
		bean.setName("test bean");
		bean.setEmail("test@email.com");
		bean.setBirthday(DateUtils.parse("2012-11-23"));

		JsonMapper jm = JsonMapper.ignoreEmpty();
		String json = jm.toJson(bean);
		System.out.println("testJsonIgnoreField: " + json);
		Assert.assertEquals("{\"email\":\"test@email.com\",\"birthday\":\"2012-11-23 00:00:00\"}", json);
	}
	

	@Test
	public void testTestDefaultBeanPropertyFilter() throws Exception{
		TestJsonBean bean = new TestJsonBean();
		bean.setName("test bean");
		bean.setEmail("test@email.com");
		bean.setBirthday(DateUtils.parse("2012-11-23"));

		JsonMapper jm = JsonMapper.ignoreEmpty().defaultFiler(new TestDefaultBeanPropertyFilter());
		String json = jm.toJson(bean);
		System.out.println("testTestDefaultBeanPropertyFilter: " + json);
		Assert.assertEquals("{\"email\":\"test@email.com\",\"birthday\":\"2012-11-23 00:00:00\"}", json);
	}
	
	@Test
	public void testCauseMap(){
		ParamMap map = new ParamMap();
		map.putElement("aa", "aavalue");
		map.putElement("bb", "bbvalue");
		String json = JsonMapper.IGNORE_EMPTY.toJson(map);
		System.out.println("json: " +json);
		Assert.assertEquals("{\"aa\":[\"aavalue\"],\"bb\":[\"bbvalue\"]}", json);
		

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("aa", "aavalue");
		map2.put("bb", "bbvalue");
		json = JsonMapper.IGNORE_EMPTY.toJson(map2);
		System.out.println("json: " +json);
	}
	
	@Test
	public void testMapToJson(){
		String json = "";
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("aa", "aavalue");
		map2.put("bb", "bbvalue");
		json = JsonMapper.IGNORE_EMPTY.toJson(map2);
		System.out.println("json: " +json);
		Assert.assertEquals("{\"aa\":\"aavalue\",\"bb\":\"bbvalue\"}", json);
		
		MapObjectTest mapObj = new MapObjectTest();
		mapObj.setId(11L);
		mapObj.setUserName("testUserName");
		map2.put("aa", "aavalue");
		map2.put("bb", "bbvalue");
		json = JsonMapper.IGNORE_EMPTY.toJson(map2);
		System.out.println("json: " +json);
		Assert.assertEquals("{\"aa\":\"aavalue\",\"bb\":\"bbvalue\"}", json);
		
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
		json = JsonMapper.IGNORE_EMPTY.toJson(mapping);
		System.out.println("json: " +json);
		Map<String, Object> jsonMap = JsonMapper.ignoreEmpty().fromJson(json, Map.class);
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
		
		String json = JsonMapper.IGNORE_EMPTY.toJson(v);
		System.out.println("json: " + json);
		Assert.assertEquals("{\"field\":\"fieldValue\",\"intField\":11,\"type\":\"FINISHED\"}", json);;
	}
	
	@Test
	public void testMixins() {
		JsonMapper printMapper = JsonMapper.defaultMapper();
		
		TestJsonDataWithIOClass data = new TestJsonDataWithIOClass();
		data.setUserName("testUserName");
		String json = printMapper.toJson(data);
		System.out.println("json: " + json);
		assertThat(json).isEqualTo("{\"userName\":\"testUserName\",\"createAt\":1570464000000,\"file\":\"d:\\\\test\\\\test.jpg\"}");
		
		printMapper = JsonMapper.defaultMapper()
				.addMixIns(IgnoreIOClassForTestMixin.class,
						File.class, 
						InputStream.class);
		json = printMapper.toJson(data);
		System.out.println("json: " + json);
		assertThat(json).isEqualTo("{\"userName\":\"testUserName\",\"createAt\":1570464000000}");
		
		Map<String, Object> map = new HashMap<>();
		map.put("userName", "testUserName");
		map.put("test", new File("d:/test/test.jpg"));
		json = printMapper.toJson(map);
		System.out.println("json: " + json);
		assertThat(json).isEqualTo("{\"test\":\"d:\\\\test\\\\test.jpg\",\"userName\":\"testUserName\"}");
	}
	
	@Test
	public void testGenericType() {
		String json = "[{\"url\": \"https://test.com/test.jpg\", \"name\": \"test.jpg\", \"type\": \"PICTURE\", \"accessableUrl\": \"https://test.com/test.jpg\"}]";
		List<ResourceInfo> resList = JsonMapper.IGNORE_NULL.fromJsonAsList(json, ResourceInfo.class);
		System.out.println("Res: " + resList);
		assertThat(resList).isNotEmpty();
		assertThat(resList.get(0).getUrl()).isEqualTo("https://test.com/test.jpg");
	}
	
	@JsonIgnoreType
	static public interface IgnoreIOClassForTestMixin {

	}
	
	@Test
	public void testTypeingData() {
		StoreDetailResponse data = new StoreDetailResponse();
		data.setStoreName("test name");
		data.setSlidePictureUrls(new String[] {"test"});
		data.setErrorWithList(Lists.newArrayList("test list"));
		JsonMapper json = JsonMapper.ignoreNull();
		String str = json.toJson(data);
		System.out.println("str: " + str);
		
		json = JsonMapper.ignoreNull();
		json.getObjectMapper().enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		str = json.toJson(data);
		System.out.println("enableDefaultTyping str: " + str);
	}
	

	public static class TestJsonDataWithIOClass {
		private String userName;
		private Date createAt = DateUtils.parse("2019-10-08");
		private File file = new File("d:/test/test.jpg");
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public Date getCreateAt() {
			return createAt;
		}
		public void setCreateAt(Date createAt) {
			this.createAt = createAt;
		}
		public File getFile() {
			return file;
		}
		public void setFile(File file) {
			this.file = file;
		}
		
	}
	
	static public class AttributeValueJsonTestVO {

	    private Long attributeId;
	    private List<AttributeSelectedValueTestVO> selectedValue;
	    private String textValue;
	    private ProductAttributeFieldTypeTest fieldType;
		public Long getAttributeId() {
			return attributeId;
		}
		public void setAttributeId(Long attributeId) {
			this.attributeId = attributeId;
		}
		public List<AttributeSelectedValueTestVO> getSelectedValue() {
			return selectedValue;
		}
		public void setSelectedValue(List<AttributeSelectedValueTestVO> selectedValue) {
			this.selectedValue = selectedValue;
		}
		public String getTextValue() {
			return textValue;
		}
		public void setTextValue(String textValue) {
			this.textValue = textValue;
		}
		public ProductAttributeFieldTypeTest getFieldType() {
			return fieldType;
		}
		public void setFieldType(ProductAttributeFieldTypeTest fieldType) {
			this.fieldType = fieldType;
		}

	}
	
	static public class AttributeSelectedValueTestVO {

	    private Long id;
	    private String value;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	    
	}
	
	public static enum ProductAttributeFieldTypeTest {
        SELECT("单选下拉框"),
        MSELECT("多选下拉框"),
        TEXT("文本框");

        String label;

		private ProductAttributeFieldTypeTest(String label) {
			this.label = label;
		}
    }
	
	private static class RouteNodeRecordEntity {
		List<ResourceInfo> resourceList;
		public List<ResourceInfo> getResourceList() {
			return resourceList;
		}
		public void setResourceList(List<ResourceInfo> resourceList) {
			this.resourceList = resourceList;
		}
	}
	private static class ResourceInfo implements Serializable {
	    private String name;
	    private String url;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
}
