package org.onetwo.common.utils;

import java.util.Date;


public class User {
	public static class Address {
		private String detail;
		private String phone;
		private String fax;
		public String getDetail() {
			return detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getFax() {
			return fax;
		}
		public void setFax(String fax) {
			this.fax = fax;
		}
		
		
	}
	private String userName;
	private String desc;
	private int age;
	private Integer height;
	private Integer bust;
	private Date birthDate;
	private boolean avaiable;
	
	private Address address;
	  
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Address getAddress() {
		return address;
	}
	public Address getAddress2() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getBust() {
		return bust;
	}
	public void setBust(Integer bust) {
		this.bust = bust;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean isAvaiable() {
		return avaiable;
	}
	public void setAvaiable(boolean avaiable) {
		this.avaiable = avaiable;
	}
}
