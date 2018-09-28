package org.onetwo.common.file;

import java.io.InputStream;
import java.io.OutputStream;


public interface FileStorer {
	
	FileStoredMeta write(StoringFileContext context);

	void readFileTo(final String accessablePath, final OutputStream output);
	InputStream readFileStream(final String accessablePath);
	long getLastModified(final String accessablePath);
}
