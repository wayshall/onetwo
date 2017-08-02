package org.onetwo.common.file;

import java.io.File;

public class SimpleFileStoredMeta implements FileStoredMeta{

	private String accessablePath;
	private String fullAccessablePath;
	private String storedServerLocalPath;
	private String sotredFileName;
	
	public SimpleFileStoredMeta(String storedServerLocalPath) {
		super();
		this.storedServerLocalPath = storedServerLocalPath;
	}

	@Override
	public String getAccessablePath() {
		return accessablePath;
	}

	public File toFile() {
		return new File(accessablePath);
	}

	public String getFullAccessablePath() {
		return fullAccessablePath;
	}

	public void setFullAccessablePath(String fullAccessablePath) {
		this.fullAccessablePath = fullAccessablePath;
	}

	public String getStoredServerLocalPath() {
		return storedServerLocalPath;
	}

	public void setStoredServerLocalPath(String storedServerLocalPath) {
		this.storedServerLocalPath = storedServerLocalPath;
	}

	public String getSotredFileName() {
		return sotredFileName;
	}

	public void setSotredFileName(String sotredFileName) {
		this.sotredFileName = sotredFileName;
	}

	public void setAccessablePath(String accessablePath) {
		this.accessablePath = accessablePath;
	}

}
