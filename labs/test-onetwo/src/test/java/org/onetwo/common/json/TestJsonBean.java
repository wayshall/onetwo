package org.onetwo.common.json;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.onetwo.common.jackson.JsonDateOnlySerializer;

//@JsonIgnoreProperties({"name"})
public class TestJsonBean {
	
	@JsonIgnore
	private String name;
	private String email;
	private Date birthday;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@JsonSerialize(using=JsonDateOnlySerializer.class)
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
}
