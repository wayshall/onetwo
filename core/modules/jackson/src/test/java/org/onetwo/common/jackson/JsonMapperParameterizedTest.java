package org.onetwo.common.jackson;

import java.lang.reflect.Type;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.date.DateUtil;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.Page;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonMapperParameterizedTest {
	
	private final JsonMapper mapper = JsonMapper.IGNORE_NULL;
	
	public static class JsonParametericData {
		private long id;
		private List<TestJsonBean> datas;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public List<TestJsonBean> getDatas() {
			return datas;
		}
		public void setDatas(List<TestJsonBean> datas) {
			this.datas = datas;
		}
		
	}
	
	protected TestJsonBean createTestJsonBean(int i){
		TestJsonBean bean = new TestJsonBean();
		bean.setName("test bean"+i);
		bean.setEmail("test@email.com"+i);
		bean.setBirthday(DateUtil.parse("2012-11-23"));
		return bean;
	}
	
	public List<TestJsonBean> createBeans(int count){
		return LangOps.generateList(count, (i)->createTestJsonBean(i));
		/*return Stream.iterate(1, i->i+1).limit(count)
										.map((i)->createTestJsonBean(i))
										.collect(Collectors.toList());*/
	}

	@Test
	public void testArray() throws Exception{
		int count = 10;
		TestJsonBean[] list = createBeans(count).toArray(new TestJsonBean[0]);
		String str = mapper.toJson(list);
		System.out.println("json: " + str);
		
		Object[] result = mapper.fromJsonWith(str, TestJsonBean[].class);
		
		System.out.println("result: " + mapper.toJson(result));
		Assert.assertTrue(TestJsonBean[].class.isInstance(result));
		TestJsonBean[] resultList = (TestJsonBean[]) result;
		Assert.assertTrue(resultList.length==count);
		Assert.assertEquals(TestJsonBean.class, resultList[0].getClass());
	}

	@Test
	public void testListParameteric() throws Exception{
		int count = 10;
		List<TestJsonBean> list = createBeans(count);
		String str = mapper.toJson(list);
		System.out.println("json: " + str);
		
		/*TypeFactory tf = mapper.getObjectMapper().getTypeFactory();
		Object result = mapper.getObjectMapper().readValue(str, tf.constructParametricType(List.class, TestJsonBean.class));
		*/
		Object result = mapper.fromJson(str, new TypeReference<List<TestJsonBean>>(){});
		System.out.println("result: " + mapper.toJson(result));
		Assert.assertTrue(List.class.isInstance(result));
		List<?> resultList = (List<?>) result;
		Assert.assertTrue(resultList.size()==count);
		Assert.assertEquals(TestJsonBean.class, resultList.get(0).getClass());
	}

	@Test
	public void testListParameteric2() throws Exception{
		int count = 10;
		List<TestJsonBean> list = createBeans(count);
		String str = mapper.toJson(list);
		System.out.println("json: " + str);
		
		/*TypeFactory tf = mapper.getObjectMapper().getTypeFactory();
		Object result = mapper.getObjectMapper().readValue(str, tf.constructParametricType(List.class, TestJsonBean.class));
		*/
		Type type = ReflectUtils.findMethod(JsonParametericData.class, "getDatas").getGenericReturnType();
		List<TestJsonBean> result = mapper.fromJson(str, type);
		System.out.println("result: " + mapper.toJson(result));
		Assert.assertTrue(List.class.isInstance(result));
		List<?> resultList = (List<?>) result;
		Assert.assertTrue(resultList.size()==count);
		Assert.assertEquals(TestJsonBean.class, resultList.get(0).getClass());
	}
	
	@Test
	public void testPageParameteric() throws Exception{
		int count = 10;
		List<TestJsonBean> list = createBeans(count);
		Page<TestJsonBean> page = new Page<TestJsonBean>();
		page.setResult(list);
		String str = mapper.toJson(page);
		System.out.println("json: " + str);
		
		TypeFactory tf = mapper.getObjectMapper().getTypeFactory();
		Object result = mapper.getObjectMapper().readValue(str, tf.constructParametricType(Page.class, TestJsonBean.class));
		
		Assert.assertTrue(Page.class.isInstance(result));
		Page<?> resultList = (Page<?>) result;
		Assert.assertTrue(resultList.getResult().size()==count);
		Assert.assertEquals(TestJsonBean.class, resultList.getResult().get(0).getClass());
	}
	
	@Test
	public void testJsonParametericData() throws Exception{
		int count = 10;
		List<TestJsonBean> list = createBeans(count);
		JsonParametericData page = new JsonParametericData();
		page.setDatas(list);
		String str = mapper.toJson(page);
		System.out.println("json: " + str);
		
		TypeFactory tf = mapper.getObjectMapper().getTypeFactory();
		TypeBindings binding = new TypeBindings(tf, JsonParametericData.class);
		binding.addBinding("datas", tf.constructParametricType(List.class, TestJsonBean.class));
		Object result = mapper.getObjectMapper().readValue(str, tf.constructType(JsonParametericData.class, binding));
		
		Assert.assertTrue(JsonParametericData.class.isInstance(result));
		JsonParametericData resultList = (JsonParametericData) result;
		Assert.assertTrue(resultList.getDatas().size()==count);
		Assert.assertEquals(TestJsonBean.class, resultList.getDatas().get(0).getClass());
	}
	
	
	/*@Test
	public void testSimple2(){
		Method method = ReflectUtils.findMethod(JsonMapperParameterizedTest.class, "createBeans");
		RpcMethodResolver rmethod = new RpcMethodResolver(method);
		
		int count = 10;
		List<TestJsonBean> list = createBeans(count);
		String str = mapper.toJson(list);
		System.out.println("json: " + str);
		Class<?> clazz = rmethod.getResponseType();
		
		TypeFactory tf = mapper.getObjectMapper().getTypeFactory();
		Object result = mapper.getObjectMapper().readValue(str, tf.constructType(type, context));
		
		System.out.println("result: " + mapper.toJson(result));
		Assert.assertTrue(List.class.isInstance(result));
		List<?> resultList = (List<?>) result;
		Assert.assertTrue(resultList.size()==count);
		Assert.assertEquals(TestJsonBean.class, resultList.get(0).getClass());
	}*/

}
