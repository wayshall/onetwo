package org.onetwo.common.file;

import java.io.InputStream;
import java.io.OutputStream;

import org.onetwo.common.utils.StringUtils;


public interface FileStorer {
	
	String getStoreType();
	
	default String defaultStoreKey(StoringFileContext context) {
		String dir = StringUtils.emptyIfNull(context.getFileStoreBaseDir());
		if (StringUtils.isNotBlank(context.getModule())) {
			dir += "/" + context.getModule();
		}
		dir = FileUtils.convertDir(dir);
		// oss的key 不能以 / 开头
		dir = StringUtils.trimStartWith(dir, FileUtils.SLASH);
		String key = context.getKey();
		if (StringUtils.isBlank(context.getKey())) {
			key = dir + FileUtils.randomUUIDFileName(context.getFileName(), context.isKeepOriginFileName());
		} else {
			key = dir + key;
		}
		return key;
	}
	
	FileStoredMeta write(StoringFileContext context);
	
	default void delete(String key) {
		throw new UnsupportedOperationException();
	}

	void readFileTo(final String accessablePath, final OutputStream output);
	InputStream readFileStream(final String accessablePath);
	long getLastModified(final String accessablePath);
}
