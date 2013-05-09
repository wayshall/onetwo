package org.onetwo.common.utils.bean;

import java.util.List;



public class Bean1 {

	private String name1;
	private String name2;
	private String name3;
	
	private List<TestBean1> list;
	
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
	public String getName3() {
		return name3;
	}
	public void setName3(String name3) {
		this.name3 = name3;
	}
	public List<TestBean1> getList() {
		return list;
	}
	public void setList(List<TestBean1> list) {
		this.list = list;
	}
	
}