package org.onetwo.common.propconf;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.file.FileUtils;

public class ResourceAdapterImpl<T> implements ResourceAdapter<T> {

	protected final T resource;
	protected String postfix;
	
	public ResourceAdapterImpl(T resource) {
		super();
		this.resource = resource;
	}
	
	public ResourceAdapterImpl(T resource, String postfix) {
		super();
		this.resource = resource;
		this.postfix = postfix;
	}

	@Override
	public boolean exists() {
		if(resource instanceof File){
			File file = (File) resource;
			return file.exists();
		}
		return false;
	}

	public String getPostfix() {
		if(StringUtils.isBlank(postfix)){
			return FileUtils.getExtendName(getName());
		}
		return postfix;
	}

	@Override
	public List<String> readAsList(){
		if(isSupportedToFile())
			return FileUtils.readAsList(getFile());
		else
			throw new UnsupportedOperationException();
	}

	public T getResource() {
		return resource;
	}

	
	@Override
	public boolean isSupportedToFile() {
		if(resource instanceof File){
			return true;
		}
		return false;
	}

	@Override
	public File getFile() {
		if(resource instanceof File)
			return (File)resource;
		return null;
	}


	@Override
	public String getName() {
		if(getFile()!=null){
			return getFile().getName();
		}else{
			String path = resource.toString();
			return FileUtils.getFileName(path);
		}
	}

	
}
