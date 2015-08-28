package org.onetwo.common.file;


public interface StoreFilePathStrategy {

	String getStoreFilePath(String storeBaseDir, StoringFileContext context);
}
