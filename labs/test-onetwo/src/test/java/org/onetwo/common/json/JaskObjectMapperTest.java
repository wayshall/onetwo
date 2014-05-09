package org.onetwo.common.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.onetwo.common.jackson.JsonMapper;

import test.entity.UserEntity;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JaskObjectMapperTest {

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
	public void testJsonList() throws Exception{
		List<UserEntity> users = new ArrayList<UserEntity>();
		int size = 5;
		for(int i=0; i<size; i++){
			users.add(createUser());
		}

		JsonMapper jsonmapper = new JsonMapper(Include.ALWAYS);
		ObjectMapper objectMapper = jsonmapper.getObjectMapper();
		
		String json = jsonmapper.toJson(users);
		System.out.println("testJsonList: " + json);
	}
	
}
