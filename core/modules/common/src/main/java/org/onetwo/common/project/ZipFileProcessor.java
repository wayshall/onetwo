package org.onetwo.common.project;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import org.onetwo.common.utils.ZipUtils;

/**
 * @author weishao zeng
 * <br/>
 */

public class ZipFileProcessor extends BaseFileProcessor<ZipFileProcessor> {
	private String targetZipFilePath;

	public ZipFileProcessor(ProjectRefactor project, String baseDir, String targetZipFilePath) {
		super(project, baseDir);
		this.targetZipFilePath = targetZipFilePath;
	}
	
	public ZipFileProcessor fileNameEndWith(String...postfix){
		return orFileMatcher(file->{
			return Stream.of(postfix).anyMatch(suffix->{
				return file.getName().endsWith(suffix);
			});
		});
	}
	
	@Override
	public void process() {
		List<File> allFiles = matchAllFiles();
		ZipUtils.zipfileList(targetZipFilePath, allFiles, this.baseDir);
	}
	
}
