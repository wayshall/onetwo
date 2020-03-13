package org.onetwo.common.project;

import java.io.File;
import java.util.function.Predicate;

import org.onetwo.common.file.FileUtils;

/**
 * @author weishao zeng
 * <br/>
 */

public abstract class BaseFileProcessor<R extends BaseFileProcessor<R>> {
	final protected String baseDir;
	protected Predicate<File> fileMatcher;
	
	public BaseFileProcessor(String baseDir) {
		super();
		this.baseDir = baseDir;
	}

	@SuppressWarnings("unchecked")
	public R fileMatcher(Predicate<File> fileMatcher){
		this.fileMatcher = fileMatcher;
		return (R)this;
	}
	
	public R orFileMatcher(Predicate<File> matcher){
		return fileMatcher(this.fileMatcher==null?matcher:this.fileMatcher.or(matcher));
	}
	
	public R andFileMatcher(Predicate<File> matcher){
		return fileMatcher(this.fileMatcher==null?matcher:this.fileMatcher.and(matcher));
	}

	public void process(){
		File dirFile = new File(baseDir);
		FileUtils.list(dirFile, fileMatcher, true)
				 .forEach(file->{
					 fileProcess(file);
				 });
	}
	
	abstract protected void fileProcess(File file);
	
}
