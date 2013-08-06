package org.onetwo.plugins.permission.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@SuppressWarnings("serial")
@Inheritance(strategy=InheritanceType.JOINED)
abstract public class PermissionEntity implements Serializable {
	
	private Long id;
	private String code;
	
	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
