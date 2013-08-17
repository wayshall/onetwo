package org.onetwo.plugins.permission.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
//@DiscriminatorColumn(name="PTYPE", discriminatorType=DiscriminatorType.STRING)
@Table(name="ADMIN_PERMISSION")
public class PermissionEntity implements Serializable {

	private Long id;
	private String code;
	private String name;
	private PermissionType ptype;
	private String url;
	private String method;
	
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
	@Enumerated(EnumType.STRING)
	public PermissionType getPtype() {
		return ptype;
	}
	public void setPtype(PermissionType ptype) {
		this.ptype = ptype;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

	public static enum PermissionType {
		MENU,
		FUNCTION
	}
	
}
