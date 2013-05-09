package org.onetwo.common.utils.bean;

import java.util.Date;

import org.onetwo.common.spring.dozer.DozerMapping;

@DozerMapping(classb=TestBean2.class)
public class TestBean1 {
	private String name1;
	private String name2;
	private String name4;
	
	private String userName;
	private Date birthDay;
	
	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getName4() {
		return name4;
	}

	public void setName4(String name4) {
		this.name4 = name4;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}
}

