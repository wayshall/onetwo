package org.onetwo.common.project;

import java.io.File;
import java.util.List;

/**
 * @author weishao zeng
 * <br/>
 */

public class ExecutorFileProcessor extends BaseFileProcessor<ExecutorFileProcessor> {

	private FileProccessExecutor executor;
	
	public ExecutorFileProcessor(ProjectRefactor project, String baseDir, FileProccessExecutor executor) {
		super(project, baseDir);
		this.executor = executor;
	}
	
	public void process(){
		File baseDirFile = new File(baseDir);
		List<File> allFiles = matchAllFiles(baseDirFile);
		boolean continueProccess = true;
		for (File file : allFiles){
			continueProccess = executor.apply(baseDirFile, file);
			if (!continueProccess) {
				break;
			}
		}
	}
	
}
