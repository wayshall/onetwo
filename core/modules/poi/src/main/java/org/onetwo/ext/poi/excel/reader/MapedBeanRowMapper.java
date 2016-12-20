package org.onetwo.ext.poi.excel.reader;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.ext.poi.excel.exception.ExcelException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Deprecated
public class MapedBeanRowMapper<T> extends BeanRowMapper<T> {

	private Map<String, String> nameMap = Maps.newHashMap();
	
	public MapedBeanRowMapper(Class<T> clazz) {
		super(clazz, WorkbookReaderFactory.convertors);
	}

	public MapedBeanRowMapper<?> map(String name, String propertyName){
		this.nameMap.put(name, propertyName);
		return this;
	}
	public List<String> mapTitleRow(Sheet sheet) {
		List<String> names = super.mapTitleRow(sheet);
		List<String> mappedNames = Lists.newArrayList();
		for(String name : names){
			if(!nameMap.containsKey(name))
				throw new ExcelException("no mapped name found: " + name);
			mappedNames.add(nameMap.get(name));
		}
		return mappedNames;
	}
	
}
