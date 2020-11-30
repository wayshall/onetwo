package org.onetwo.common.web.userdetails;

import java.security.Principal;

public class UserDetailPrincipal implements Principal {
	
	final private Long id;
	final private String name;
	
	public UserDetailPrincipal(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}


	@Override
	public String getName() {
		return name;
	}
}
