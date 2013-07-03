package org.onetwo.common;

import org.onetwo.common.spring.web.BaseController;
import org.onetwo.javassist.TestInterface;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/test")
public class TestController extends BaseController<Object> implements TestInterface {

	@Override
	@RequestMapping("/test1")
	public String test1(String value1){
		return "";
	}

	@Override
	@RequestMapping("test2")
	public String test2(String value2){
		return "";
	}

	@Override
	@RequestMapping
	public String test3(String value3){
		return "";
	}
}
