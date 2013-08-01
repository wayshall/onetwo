package org.onetwo.common.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;

public class MapedBeanRowMapper<T> extends BeanRowMapper<T> {

	private Map<String, String> nameMap = LangUtils.newHashMap();
	
	public MapedBeanRowMapper(Class<T> clazz) {
		super(clazz, WorkbookReaderFactory.convertors);
	}

	public MapedBeanRowMapper<?> map(String name, String propertyName){
		this.nameMap.put(name, propertyName);
		return this;
	}
	public List<String> mapTitleRow(Sheet sheet) {
		List<String> names = super.mapTitleRow(sheet);
		List<String> mappedNames = LangUtils.newArrayList();
		for(String name : names){
			if(!nameMap.containsKey(name))
				throw new BaseException("no mapped name found: " + name);
			mappedNames.add(nameMap.get(name));
		}
		return mappedNames;
	}
	
}
