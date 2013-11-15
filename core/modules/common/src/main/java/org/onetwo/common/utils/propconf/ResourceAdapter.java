package org.onetwo.common.utils.propconf;

import java.io.File;

public interface ResourceAdapter {
	public String getName();
//	public String getPath();
	public Object getResource();
	
	public File getFile();
	
	public boolean isSupportedToFile();
	
//	public boolean isURL();

}
