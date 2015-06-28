package org.onetwo.common.utils.propconf;

import java.io.File;

public interface ResourceAdapter<T> {
	public String getName();
//	public String getPath();
	public T getResource();
	
	public File getFile();
	
	public boolean isSupportedToFile();
	
//	public boolean isURL();

}
