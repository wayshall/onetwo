package org.onetwo.common.spring.mcache;

import java.io.Serializable;

public class CacheModel {
	
	protected Serializable key;
	protected String group;
	protected int expire;

	public CacheModel(){
	}
	
	public CacheModel(Serializable key, String group, int expire) {
		this.key = key;
		this.group = group;
		this.expire = expire;
	}


	public Serializable getKey() {
		return key;
	}

	public void setKey(Serializable key) {
		this.key = key;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

}
