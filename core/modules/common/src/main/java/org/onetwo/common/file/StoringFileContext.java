package org.onetwo.common.file;

import java.io.InputStream;

public class StoringFileContext {
	
	public static StoringFileContext create(InputStream inputStream, String fileName){
		return new StoringFileContext(null, inputStream, fileName);
	}
	
	public static StoringFileContext create(String module, InputStream inputStream, String fileName){
		return new StoringFileContext(module, inputStream, fileName);
	}
	
	private InputStream inputStream;
	private String fileName;
//	private Map<String, Object> context;
	private String module;
//	private StoreFilePathStrategy storeFilePathStrategy;// = SimpleFileStorer.SIMPLE_STORE_STRATEGY;
	private String key;
	private boolean keepOriginFileName = false;
	private String fileStoreBaseDir;
	
	
	public StoringFileContext(String module, InputStream inputStream, String fileName) {
		super();
		this.inputStream = inputStream;
		this.fileName = fileName;
		this.module = module;
	}

	/*public StoringFileContext put(String name, Object value){
		if(context==null){
			context = Maps.newHashMap();
		}
		context.put(name, value);
		return this;
	}

	public Map<String, Object> getContext() {
		return context==null?ImmutableMap.of():ImmutableMap.copyOf(context);
	}*/

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getFileName() {
		return fileName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	/*public StoreFilePathStrategy getStoreFilePathStrategy() {
		return storeFilePathStrategy;
	}

	public void setStoreFilePathStrategy(StoreFilePathStrategy storeFilePathStrategy) {
		this.storeFilePathStrategy = storeFilePathStrategy;
	}*/

	public boolean isKeepOriginFileName() {
		return keepOriginFileName;
	}

	public void setKeepOriginFileName(boolean keepOriginFileName) {
		this.keepOriginFileName = keepOriginFileName;
	}

	public String getFileStoreBaseDir() {
		return fileStoreBaseDir;
	}

	public void setFileStoreBaseDir(String fileStoreBaseDir) {
		this.fileStoreBaseDir = fileStoreBaseDir;
	}

}
