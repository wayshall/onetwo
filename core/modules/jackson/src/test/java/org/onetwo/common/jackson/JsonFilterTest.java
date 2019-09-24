package org.onetwo.common.jackson;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Test;
import org.onetwo.common.jackson.exception.JsonException;

import com.fasterxml.jackson.annotation.JsonFilter;

public class JsonFilterTest {
	
	@Test
	public void test() throws Exception {
		UserEntityWithJsonFilter user = new UserEntityWithJsonFilter();
		user.setId(1L);
		user.setUserName("testUserName");
		user.setStatus("normal");
		
		JsonMapper jsonMapper = JsonMapper.ignoreNull()
								.filter(UserEntityWithJsonFilter.WITHOUT_STATUS, "status");
		String json = jsonMapper.toJson(user);
		System.out.println("json: " + json);
		assertThat(json).isEqualTo("{\"id\":1,\"userName\":\"testUserName\"}");
		
		JsonMapper jsonMapper2 = JsonMapper.ignoreNull();
		json = jsonMapper2.writer(JsonMapper.exceptFilter(UserEntityWithJsonFilter.WITHOUT_STATUS, "status"))
						.writeValueAsString(user);
		System.out.println("json: " + json);
		assertThat(json).isEqualTo("{\"id\":1,\"userName\":\"testUserName\"}");
		
		assertThatExceptionOfType(JsonException.class).isThrownBy(() -> {
			jsonMapper2.toJson(user);
		});
	}

	
	@JsonFilter(UserEntityWithJsonFilter.WITHOUT_STATUS)
	public static class UserEntityWithJsonFilter {
		
		public static final String WITHOUT_STATUS = "WITHOUT_STATUS";

		private Long id;
		
		private String userName;

		private String status;
		
		private String email;
		
		private Integer age;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}
		
	}
}

