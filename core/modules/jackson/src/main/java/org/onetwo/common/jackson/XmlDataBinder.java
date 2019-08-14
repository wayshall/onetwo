package org.onetwo.common.jackson;

import java.io.File;

import org.onetwo.common.file.FileUtils;

public class XmlDataBinder<T> {
	protected Class<T> dataType;
	protected String dataFilePath;

	protected JacksonXmlMapper jsonMapper =  JacksonXmlMapper.defaultMapper();
	
	public XmlDataBinder(Class<T> dataType) {
		String path = dataType.getName().replace('.', '/')+".data.xml";
		this.dataType = dataType;
		this.dataFilePath = path;
	}
	
	public XmlDataBinder(Class<T> dataType, String dataFilePath) {
		super();
		this.dataType = dataType;
		this.dataFilePath = dataFilePath;
	}
	
	public T buildData(){
		String path = FileUtils.getResourcePath("")+dataFilePath;
		File file = new File(path);
		T data = jsonMapper.fromXml(file, dataType);;
		return data;
	}
}
