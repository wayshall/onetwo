package org.onetwo.common.jackson;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserEntity {

	private Long id;
	
	private String userName;

	private String status;
	
	private String email;
	
	private Integer age;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	protected Date birthDay;

	private Float height;
	
	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAge() {
		return age==null?0:age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public Float getHeight() {
		return height==null?0:height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static class SubUserEntity extends UserEntity {

		protected Date birthDay2;
		@JsonSerialize(using=JsonDateOnlySerializer.class)
		public Date getBirthDay2() {
			return birthDay2;
		}

		@JsonDeserialize(using=JsonDateOnlyDerializer.class)
		public void setBirthDay2(Date birthDay) {
			this.birthDay2 = birthDay;
		}
	}
}
