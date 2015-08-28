package org.onetwo.common.utils.propconf;

import java.io.File;
import java.util.List;

import org.onetwo.common.file.FileUtils;

public class ResourceAdapterImpl<T> implements ResourceAdapter<T> {

	protected final T resource;
	
	public ResourceAdapterImpl(T resource) {
		super();
		this.resource = resource;
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
