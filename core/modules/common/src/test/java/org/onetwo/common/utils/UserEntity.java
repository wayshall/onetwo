package org.onetwo.common.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class UserEntity {

	private Long id;
	
	private String userName;

	private String status;
	
	private String email;
	
	private Integer age;

	private Date birthDay;

	private Float height;
	
	private List<RoleEntity> roles;
	
	private RoleEntity role;
	
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
	
	public void addRole(RoleEntity role){
		if(this.roles==null)
			this.roles = new ArrayList<RoleEntity>();
		this.roles.add(role);
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", userName=" + userName + ", status=" + status + "]";
	}


}
