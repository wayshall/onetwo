package org.onetwo.webapp.oauth2.resource.service;

import org.onetwo.common.db.BaseEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl {
	
	/*@Autowired
	private BaseEntityManager baseEntityManager;*/
	
	public void test(){
		System.out.println("test");
	}

}
