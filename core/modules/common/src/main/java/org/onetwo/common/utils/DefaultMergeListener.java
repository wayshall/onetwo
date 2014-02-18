package org.onetwo.common.utils;

import java.io.File;
import java.io.Writer;

public class DefaultMergeListener implements MergeFileListener {
	@Override
	public void onStart(Writer writer, File file, int fileIndex) throws Exception {
		String fileName = FileUtils.getFileName(file.getPath());
		System.out.println("merege file["+fileIndex+"] : " + fileName);
		writer.write("\n");
	}
	
	@Override
	public void writeLine(Writer writer, File file, int fileIndex, String line, int lineIndex) throws Exception{
		writer.write(line + "\n");
	}

	@Override
	public void onEnd(Writer writer, File file, int fileIndex) throws Exception {
		writer.write("\n\n\n");
	}
}
