package org.onetwo.boot.core.web.utils;

import java.util.stream.Stream;

public enum ResponseType {
	PAGE,
	JSON,
	XML;
	
	public static ResponseType of(String ext){
		return Stream.of(values()).filter(e->e.name().equalsIgnoreCase(ext))
							.findAny()
							.orElse(PAGE);
	}
}
