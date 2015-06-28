package org.onetwo.common.utils.propconf;

import java.io.File;

import org.onetwo.common.utils.FileUtils;

public class ResourceAdapterImpl<T> implements ResourceAdapter<T> {

	protected final T resource;
	
	public ResourceAdapterImpl(T resource) {
		super();
		this.resource = resource;
	}
	

	public T getResource() {
		return resource;
	}

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
