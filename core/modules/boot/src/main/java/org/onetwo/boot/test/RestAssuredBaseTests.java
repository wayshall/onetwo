package org.onetwo.boot.test;

import java.util.List;

import org.junit.Before;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.Langs;
import org.onetwo.common.utils.func.IndexableClosure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;

import com.jayway.restassured.RestAssured;

@IntegrationTest("server.port:0")
public class RestAssuredBaseTests {

	@Value("${local.server.port}")
	protected int serverPort;
	
	@Before
	public void setupRest(){
		RestAssured.port = serverPort;
	}
	

	protected <T> List<T> createObjects(int size, Class<T> clazz, IndexableClosure<T> mapper){
		List<T> estatelist = Langs.generateList(size, i->{
			T obj = ReflectUtils.newInstance(clazz);
			mapper.execute(obj, i);
			return  obj;
		});
		return estatelist;
	}
}
