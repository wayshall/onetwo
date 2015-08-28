package org.onetwo.common.file;


public interface FileStorer<R extends FileStoredMeta> {
	
	R write(StoringFileContext context);
	
}
