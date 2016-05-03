package org.onetwo.common.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.exception.ExcelException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MapedBeanRowMapper<T> extends BeanRowMapper<T> {

	private Map<String, String> nameMap = Maps.newHashMap();
	
	public MapedBeanRowMapper(Class<T> clazz) {
		super(clazz, WorkbookReaderFactory.convertors);
	}

	public MapedBeanRowMapper<?> map(String name, String propertyName){
		this.nameMap.put(name, propertyName);
		return this;
	}
	public List<String> mapTitleRow(int sheetIndex, Sheet sheet) {
		List<String> names = super.mapTitleRow(sheetIndex, sheet);
		List<String> mappedNames = Lists.newArrayList();
		for(String name : names){
			if(!nameMap.containsKey(name))
				throw new ExcelException("no mapped name found: " + name);
			mappedNames.add(nameMap.get(name));
		}
		return mappedNames;
	}
	
}
