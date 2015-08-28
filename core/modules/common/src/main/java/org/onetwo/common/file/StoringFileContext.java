package org.onetwo.common.file;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class StoringFileContext {
	
	public static StoringFileContext create(InputStream inputStream, String fileName){
		return new StoringFileContext(null, inputStream, fileName);
	}
	
	public static StoringFileContext create(String module, InputStream inputStream, String fileName){
		return new StoringFileContext(module, inputStream, fileName);
	}
	
	private InputStream inputStream;
	private String fileName;
	private Map<String, Object> context;
	private String module;
	
	public StoringFileContext(String module, InputStream inputStream, String fileName) {
		super();
		this.inputStream = inputStream;
		this.fileName = fileName;
		this.module = module;
	}

	public StoringFileContext put(String name, Object value){
		if(context==null){
			context = Maps.newHashMap();
		}
		context.put(name, value);
		return this;
	}

	public Map<String, Object> getContext() {
		return context==null?Collections.EMPTY_MAP:ImmutableMap.copyOf(context);
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

}
