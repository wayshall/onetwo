package org.onetwo.common.project;

import java.io.File;

/**
 * @author weishao zeng
 * <br/>
 */

public interface FileProccessExecutor {
	
	/***
	 * 
	 * @author weishao zeng
	 * @param baseDirFile
	 * @param file
	 * @return 返回false，中止文件处理
	 */
	boolean apply(File baseDirFile, File file);

}
