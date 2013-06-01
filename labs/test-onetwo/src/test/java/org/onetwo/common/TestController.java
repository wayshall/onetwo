package org.onetwo.common;

import org.onetwo.common.spring.web.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/test")
public class TestController extends BaseController<Object> {

	@RequestMapping("/test1")
	public String test1(String value){
		return "";
	}

	@RequestMapping("test2")
	public String test2(String value){
		return "";
	}

	@RequestMapping
	public String test3(String value){
		return "";
	}
}
