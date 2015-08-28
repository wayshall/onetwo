package org.onetwo.common.file;

import java.io.File;
import java.io.Writer;

public class MergeFileContext {
	final private Writer writer;
	private File file;
	private int fileIndex;
	private int totalLineIndex = 0;
	
	public MergeFileContext(Writer writer) {
		super();
		this.writer = writer;
	}
	public Writer getWriter() {
		return writer;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public int getFileIndex() {
		return fileIndex;
	}
	public void setFileIndex(int fileIndex) {
		this.fileIndex = fileIndex;
	}
	public int getTotalLineIndex() {
		return totalLineIndex;
	}
	public void setTotalLineIndex(int totalLineIndex) {
		this.totalLineIndex = totalLineIndex;
	}
	
	
}
