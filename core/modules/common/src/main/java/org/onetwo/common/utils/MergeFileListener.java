package org.onetwo.common.utils;

/***
 * @author wayshall
 *
 */
public interface MergeFileListener {
	void onStart(MergeFileContext context) throws Exception;
	void onFileStart(MergeFileContext context) throws Exception;
	void writeLine(MergeFileContext context, String line, int lineIndex) throws Exception;
	void onFileEnd(MergeFileContext context) throws Exception;
	void onEnd(MergeFileContext context) throws Exception;

}
