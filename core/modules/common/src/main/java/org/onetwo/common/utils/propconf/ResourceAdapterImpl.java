package org.onetwo.common.utils.propconf;

import java.io.File;

import org.onetwo.common.utils.FileUtils;

public class ResourceAdapterImpl<T> implements ResourceAdapter {

	protected final T resource;
	
	public ResourceAdapterImpl(T resource) {
		super();
		this.resource = resource;
	}
	

	public Object getResource() {
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
			return getFile();
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
