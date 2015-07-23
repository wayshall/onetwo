package org.onetwo.common.fs;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class StoringFileContext {
	
	public static StoringFileContext create(InputStream inputStream, String fileName){
		return new StoringFileContext(inputStream, fileName);
	}
	
	private InputStream inputStream;
	private String fileName;
	private Map<String, Object> context;
	
	public StoringFileContext(InputStream inputStream, String fileName) {
		super();
		this.inputStream = inputStream;
		this.fileName = fileName;
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

}
