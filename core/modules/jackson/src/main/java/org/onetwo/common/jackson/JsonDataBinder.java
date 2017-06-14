package org.onetwo.common.jackson;

import java.io.File;

import org.onetwo.common.date.DateUtils;
import org.onetwo.common.file.FileUtils;

public class JsonDataBinder<T> {
	protected Class<T> dataType;
	protected String dataFilePath;

	protected JsonMapper jsonMapper =  JsonMapper.defaultMapper().setDateFormat(DateUtils.DATE_TIME);
	
	public JsonDataBinder(Class<T> dataType) {
		String path = dataType.getName().replace('.', '/')+".data.json";
		this.dataType = dataType;
		this.dataFilePath = path;
	}
	
	public JsonDataBinder(Class<T> dataType, String dataFilePath) {
		super();
		this.dataType = dataType;
		this.dataFilePath = dataFilePath;
	}
	
	public T buildData(){
		String path = FileUtils.getResourcePath("")+dataFilePath;
		File file = new File(path);
		T data = jsonMapper.fromJson(file, dataType);;
		return data;
	}
}
