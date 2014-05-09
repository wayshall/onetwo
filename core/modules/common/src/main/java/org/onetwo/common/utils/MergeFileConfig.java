package org.onetwo.common.utils;

import java.io.File;

public class MergeFileConfig {
	
	public static MergeFileConfig build(String charset, String mergedFileName, String dir, String postfix, MergeFileListener listener){
		File[] files = FileUtils.listFiles(dir, postfix);
		MergeFileConfig config = build(charset, mergedFileName, files);
		config.setListener(listener);
		return config;
	}
	
	public static MergeFileConfig build(String charset, String mergedFileName, File...files){
		return new MergeFileConfig(charset, mergedFileName, files);
	}
	
	public static final MergeFileListener DEFAULT_MERGE_LISTENER = new DefaultMergeListener();
	private String charset = LangUtils.UTF8;
	private String mergedFileName;
	private File[] files;
	private MergeFileListener listener = DEFAULT_MERGE_LISTENER;
	

	public MergeFileConfig(String charset, String mergedFileName, File... files) {
		this(charset, mergedFileName, files, null);
	}
	
	public MergeFileConfig(String charset, String mergedFileName, File[] files, MergeFileListener listener) {
		super();
		this.charset = charset;
		this.mergedFileName = mergedFileName;
		this.files = files;
		this.listener = listener;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getMergedFileName() {
		return mergedFileName;
	}
	public void setMergedFileName(String mergedFileName) {
		this.mergedFileName = mergedFileName;
	}
	public File[] getFiles() {
		return files;
	}
	public void setFiles(File[] files) {
		this.files = files;
	}
	
	public MergeFileListener getListener() {
		return listener;
	}
	public void setListener(MergeFileListener listener) {
		this.listener = listener;
	}
	
}
