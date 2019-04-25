package org.onetwo.common.spring.utils;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.spring.entity.UserEntity;
import org.onetwo.common.utils.CUtils;

public class MapToBeanConvertorTest {
	
	@Test
	public void test() {
		Date birthDay = DateUtils.parse("2019-04-25");
		Map<String, ?> map = CUtils.asMap("id", 123, 
										"user_name", "testUserName", 
										"birthDay", birthDay,
										"roles[0].id", 222,
										"roles[0].name", "testRoleName"
										);
		MapToBeanConvertor map2Bean = MapToBeanConvertor.builder().build();
		UserEntity user = map2Bean.toBean(map, UserEntity.class);
		
		assertThat(user).isNotNull();
		assertThat(user.getId()).isEqualTo(123);
		assertThat(user.getUserName()).isNull();
		assertThat(user.getBirthDay()).isEqualTo(birthDay);
		assertThat(user.getRoles()).isNull();
		
		/*RoleEntity role = user.getRoles().get(0);
		assertThat(role.getId()).isEqualTo(222);
		assertThat(role.getName()).isEqualTo("testRoleName");*/
	}

}

