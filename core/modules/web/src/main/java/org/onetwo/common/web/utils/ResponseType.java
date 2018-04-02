package org.onetwo.common.web.utils;

import java.util.stream.Stream;

public enum ResponseType {
	PAGE,
	JSON,
	XML,
	JFXLS;
	
	public static ResponseType of(String ext){
		return Stream.of(values()).filter(e->e.name().equalsIgnoreCase(ext))
							.findAny()
							.orElse(PAGE);
	}
}
