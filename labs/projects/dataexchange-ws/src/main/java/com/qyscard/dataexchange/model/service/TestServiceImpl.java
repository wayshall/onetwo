package com.qyscard.dataexchange.model.service;

import javax.jws.WebService;

import com.qyscard.dataexchange.wsinterface.TestService;

@WebService(endpointInterface="com.qyscard.dataexchange.wsinterface.TestService")
public class TestServiceImpl implements TestService {

	public String sayTo(String name) {
		return "hello " + name +"!";
	}
	

}
