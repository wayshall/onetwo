package org.onetwo.common.fs;


public interface FileStorer<R extends FileStoredMeta> {
	
	R write(StoringFileContext context);
	
}
