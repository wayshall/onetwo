package org.onetwo.common.jackson;

import org.onetwo.common.date.DateUtil;
import org.onetwo.common.file.FileUtils;

public class JsonDataBinder<T> {
	private Class<T> dataType;
	private String dataFilePath;

	private JsonMapper jsonMapper =  JsonMapper.defaultMapper().setDateFormat(DateUtil.DATE_TIME);
	
	public JsonDataBinder(Class<T> dataType, String dataFilePath) {
		super();
		this.dataType = dataType;
		this.dataFilePath = dataFilePath;
	}
	
	public T buildData(){
		String path = FileUtils.getResourcePath("")+dataFilePath;
		T data = jsonMapper.fromJson(path, dataType);;
		return data;
	}
}
