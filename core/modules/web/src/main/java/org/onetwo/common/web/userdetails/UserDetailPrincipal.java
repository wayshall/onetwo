package org.onetwo.common.web.userdetails;

import java.io.Serializable;
import java.security.Principal;

public class UserDetailPrincipal implements Principal {
	
	final private Serializable id;
	final private String name;
	
	public UserDetailPrincipal(Serializable id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Serializable getId() {
		return id;
	}


	@Override
	public String getName() {
		return name;
	}
}
