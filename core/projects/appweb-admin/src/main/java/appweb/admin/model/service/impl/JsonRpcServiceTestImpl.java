package appweb.admin.model.service.impl;

import org.springframework.stereotype.Service;

import appweb.admin.model.service.JsonRpcServiceTest;

@Service
public class JsonRpcServiceTestImpl implements JsonRpcServiceTest {
	
	@Override
	public String say(String something){
		return "helllo "+ something;
	}

}
