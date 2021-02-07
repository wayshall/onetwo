package org.onetwo.common.project;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;

import org.onetwo.common.file.FileMatcher;
import org.onetwo.common.file.FileMatcher.PredicateFileAdapter;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.StringUtils;

/**
 * @author weishao zeng
 * <br/>
 */

public abstract class BaseFileProcessor<R extends BaseFileProcessor<R>> {
	final protected String baseDir;
	protected FileMatcher fileMatcher;
//	final protected File baseDirFile;
	private ProjectRefactor project;
	
	public BaseFileProcessor(ProjectRefactor project, String baseDir) {
		super();
		this.baseDir = baseDir;
		this.project = project;
//		this.baseDirFile = new File(baseDir);
	}

	public R fileMatcher(Predicate<File> fileMatcher){
		PredicateFileAdapter adpt = new PredicateFileAdapter(fileMatcher);
		return fileMatcher(adpt);
	}

	@SuppressWarnings("unchecked")
	public R fileMatcher(FileMatcher fileMatcher){
		this.fileMatcher = fileMatcher;
		return (R)this;
	}
	
	public R orFileMatcher(Predicate<File> matcher){
		PredicateFileAdapter adpt = new PredicateFileAdapter(matcher);
		return orFileMatcher(adpt);
	}
	
	public R orFileMatcher(FileMatcher matcher){
		return fileMatcher(this.fileMatcher==null?matcher:this.fileMatcher.or(matcher));
	}
	
	public R andFileMatcher(Predicate<File> matcher){
		PredicateFileAdapter adpt = new PredicateFileAdapter(matcher);
		return andFileMatcher(adpt);
	}
	public R andFileMatcher(FileMatcher matcher){
		return fileMatcher(this.fileMatcher==null?matcher:this.fileMatcher.and(matcher));
	}

	public void process(){
		matchAllFiles().forEach(file->{
					 fileProcess(file);
				 });
	}

	protected List<File> matchAllFiles(){
		File baseDirFile = new File(baseDir);
		return matchAllFiles(baseDirFile);
	}
	protected List<File> matchAllFiles(File baseDirFile){
		File dirFile = new File(baseDir);
		return FileUtils.matchFiles(dirFile, fileMatcher, true);
	}

	public ProjectRefactor end(){
		return project;
	}
	
	/****
	 * 根目录下的子目录名称
	 * @author weishao zeng
	 * @param dirs
	 * @return
	 */
	public R andSubDirIs(String...dirs){
		return andFileMatcher(FileMatcher.subDirIs(dirs));
	}
	public R andSubDirIs(String dir, String dirSeperator){
		String[] dirs = StringUtils.split(dir, dirSeperator);
		return andFileMatcher(FileMatcher.subDirIs(dirs));
	}
	
	public R andPostfixIsAnyOf(String...postfix){
		return andFileMatcher(FileMatcher.fileNameEndWith(postfix));
	}
	
	public R andFilePathContains(String...paths){
		return andFileMatcher(FileMatcher.filePathContains(paths));
	}

	
	public R andIgnoreDirContains(String...ignoreDirs){
		return andFileMatcher((baseDir, file)-> {
			return !FileUtils.relativeDirPathContains(file, baseDir, ignoreDirs);
		});
	}
	
	/***
	 * default implement, just print path
	 * @author weishao zeng
	 * @param file
	 */
	protected void fileProcess(File file) {
		System.out.println("match file: " + file.getPath());
	}

	public String getBaseDir() {
		return baseDir;
	}

	public ProjectRefactor getProject() {
		return project;
	}
	
}
