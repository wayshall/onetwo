package org.onetwo.common.utils.bean;

import java.util.ArrayList;
import java.util.List;


public class Bean2 {

	private String name1;
	private String name2;
	private String name4;
	private List<TestBean2> list;
	private List<TestBean2> list2;
	
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
	public List<TestBean2> getList() {
		return list;
	}
	public void setList(List<TestBean2> list) {
		this.list = list;
	}
	
	public void addTestBean2(TestBean2 bean2){
		System.out.println("addTestBean2");
		if(list==null){
			list = new ArrayList<TestBean2>();
		}
		this.list.add(bean2);
		bean2.setBean2(this);
	}
	public List<TestBean2> getList2() {
		return list2;
	}
	public void setList2(List<TestBean2> list2) {
		this.list2 = list2;
	}
}

