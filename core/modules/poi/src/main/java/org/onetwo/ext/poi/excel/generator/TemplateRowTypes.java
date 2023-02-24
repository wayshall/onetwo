package org.onetwo.ext.poi.excel.generator;

import java.util.stream.Stream;

import org.onetwo.common.exception.ServiceException;

/**
 * @author weishao zeng
 * <br/>
 */
public enum TemplateRowTypes {

	TITLE,
	HEADER,
	ROW,
	ITERATOR;
	
	public String lowerName() {
		return name().toLowerCase();
	}
	
	public static TemplateRowTypes of(String name) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(name)).findAny().orElseThrow(()-> {
			return new ServiceException("error row type: " + name);
		});
	}
}
