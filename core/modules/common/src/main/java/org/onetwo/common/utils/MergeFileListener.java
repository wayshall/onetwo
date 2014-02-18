package org.onetwo.common.utils;

import java.io.File;
import java.io.Writer;

public interface MergeFileListener {
	void onStart(Writer writer, File file, int fileIndex) throws Exception;
	void writeLine(Writer writer, File file, int fileIndex, String line, int lineIndex) throws Exception;
	void onEnd(Writer writer, File file, int fileIndex) throws Exception;

}
