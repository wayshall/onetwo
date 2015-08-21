package org.onetwo.common.fs;


public interface StoreFilePathStrategy {

	String getStoreFilePath(String storeBaseDir, StoringFileContext context);
}
