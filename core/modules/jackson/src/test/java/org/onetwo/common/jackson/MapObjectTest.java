package org.onetwo.common.jackson;


import java.util.HashMap;

public class MapObjectTest extends HashMap<String, Object>{
	
	private Long id;
	private String userName;
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
	

}
