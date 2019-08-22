package org.onetwo.common.reflect;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.collections4.map.MultiValueMap;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;
import org.onetwo.common.utils.ParamUtils;
import org.onetwo.common.utils.RoleEntity;
import org.onetwo.common.utils.UserEntity;

/**
 * @author wayshall
 * <br/>
 */
public class BeanToMapConvertorTest {
	private static final BeanToMapConvertor BEAN_TO_MAP_CONVERTOR = BeanToMapBuilder.newBuilder().build();
	

	BeanToMapConvertor beanToMapConvertor = BeanToMapBuilder.newBuilder()
										 				.enableFieldNameAnnotation()
//										 				.enableUnderLineStyle()
										 				.build();

	@Test
	public void testWithExcludeOnly(){
		UserEntity user = new UserEntity();
		user.setId(11L);
		user.setUserName("testUserName");
		
		Map<String, Object> map = BeanToMapBuilder.newBuilder()
								 				.enableFieldNameAnnotation()
								 				.excludeProperties("roles", "role")
								 				.build()
								 				.toMap(user);
		System.out.println("map: " + map);
		assertThat(map.toString()).isEqualTo("{birthDay=null, id=11, userName=testUserName, age=0, email=null, height=0.0, status=null}");
	}

	@Test
	public void testWithDefaultIgnoreNull(){
		UserEntity user = new UserEntity();
		user.setId(11L);
		user.setUserName("testUserName");
		
		RoleEntity role = new RoleEntity();
		role.setId(20L);
		role.setName("testRoleName");

		RoleEntity role1 = new RoleEntity();
		role1.setId(21L);
		role1.setName("testRoleName1");
		
		user.setRoles(Arrays.asList(role, role1));
		
		MultiValueMap<String, Object> mmap = new MultiValueMap();
		BEAN_TO_MAP_CONVERTOR.toFlatMap(mmap, user);
		String paramString = ParamUtils.comparableKeyMapToParamString(mmap);
		System.out.println("paramString:"+paramString);
		Assert.assertEquals("age=0&height=0.0&id=11&roles[0].id=20&roles[0].name=testRoleName&roles[0].version=0&roles[1].id=21&roles[1].name=testRoleName1&roles[1].version=0&userName=testUserName", 
				paramString);
		
		user.setRole(role1);
		user.setRoles(null);
		Map<String, Object> map = beanToMapConvertor.toFlatMap(user);
		paramString = ParamUtils.comparableKeyMapToParamString(map);
		System.out.println("paramString:"+paramString);
		Assert.assertEquals("age=0&height=0.0&id=11&role.id=21&role.name=testRoleName1&role.version=0&userName=testUserName", 
				paramString);
		
	}
}
