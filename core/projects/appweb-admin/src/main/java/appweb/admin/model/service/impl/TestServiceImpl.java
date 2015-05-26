package appweb.admin.model.service.impl;

import org.onetwo.common.exception.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl {
	
	public String invokeError(){
		if(true)
			throw new ServiceException("error");
		return "yes";
	}
	
}
