package org.onetwo.plugins.permission.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseModel implements Serializable {
	
	private String id;

}
