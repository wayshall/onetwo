package org.onetwo.common.utils.file;

import java.io.File;


public interface FileCallback<T> {

	public T doWithCandidate(File file, int count);
	
}
