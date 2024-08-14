package org.onetwo.ext.poi.excel.generator;

import java.util.Map;
import java.util.stream.Collectors;
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
	
	public static Map<String, TemplateRowTypes> toMap() {
		return Stream.of(values()).collect(Collectors.toMap(e -> e.lowerName(), e -> e));
	}
}
