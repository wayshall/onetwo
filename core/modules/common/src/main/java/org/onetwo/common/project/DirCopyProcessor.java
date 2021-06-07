package org.onetwo.common.project;

import java.io.File;

import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.StringUtils;

/**
 * @author weishao zeng
 * <br/>
 */

public class DirCopyProcessor extends BaseFileProcessor<DirCopyProcessor> {
//	private String baseDir;
	private String sourceBaseDir;
	private String targetBaseDir;

	public DirCopyProcessor(ProjectRefactor project, String baseDir, String sourceSubDir, String targetSubDir) {
		super(project, FileUtils.convertDir(baseDir) + sourceSubDir);
//		this.baseDir = baseDir;
		this.sourceBaseDir = FileUtils.convertDir(baseDir) + sourceSubDir;
		this.targetBaseDir = FileUtils.convertDir(baseDir) + targetSubDir;
	}
	
	@Override
	protected void fileProcess(File file) {
		String targetPath = targetBaseDir + StringUtils.substringAfter(file.getPath(), sourceBaseDir);
		FileUtils.copyFile(file, new File(targetPath));
	}
	
	
}
