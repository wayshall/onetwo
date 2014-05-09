package org.onetwo.project.batch.test.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserVo implements Serializable {

	private String userName;
	private Integer age;
	private String email;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
