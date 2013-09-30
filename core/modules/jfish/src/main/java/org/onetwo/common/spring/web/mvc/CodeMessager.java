package org.onetwo.common.spring.web.mvc;


public interface CodeMessager {

	public String getMessage(String code, Object...args);
}
