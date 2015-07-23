package org.onetwo.common.fs;

import java.io.File;

public class SimpleFileStoredMeta implements FileStoredMeta{

	private String storedPath;
	private File storeFile;
	
	public SimpleFileStoredMeta(String storedPath) {
		super();
		this.storedPath = storedPath;
		this.storeFile = new File(storedPath);
	}

	@Override
	public String getStoredPath() {
		return storedPath;
	}

	@Override
	public File toFile() {
		return storeFile;
	}

}
