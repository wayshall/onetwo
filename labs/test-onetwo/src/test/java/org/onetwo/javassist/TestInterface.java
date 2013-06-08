package org.onetwo.javassist;

import org.springframework.web.bind.annotation.RequestMapping;

public interface TestInterface {

	@RequestMapping("/test1")
	public String test1(String value1);

	@RequestMapping("test2")
	public String test2(String value2);

	@RequestMapping
	public String test3(String value3);

}