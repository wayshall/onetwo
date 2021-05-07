package org.onetwo.common.project;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileMatcher;
import org.onetwo.common.file.FileMatcher.PredicateFileAdapter;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

/**
 * @author weishao zeng
 * <br/>
 */

public abstract class BaseFileProcessor<R extends BaseFileProcessor<R>> {
	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());
	
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

	public R file(Predicate<File> fileMatcher){
		PredicateFileAdapter adpt = new PredicateFileAdapter(fileMatcher);
		return file(adpt);
	}

	@SuppressWarnings("unchecked")
	public R file(FileMatcher fileMatcher){
		this.fileMatcher = fileMatcher;
		return (R)this;
	}
	
	public R orFile(Predicate<File> matcher){
		PredicateFileAdapter adpt = new PredicateFileAdapter(matcher);
		return orFile(adpt);
	}
	
	public R orFile(FileMatcher matcher){
		return file(this.fileMatcher==null?matcher:this.fileMatcher.or(matcher));
	}
	
	public R andFile(Predicate<File> matcher){
		PredicateFileAdapter adpt = new PredicateFileAdapter(matcher);
		return andFile(adpt);
	}
	public R andFile(FileMatcher matcher){
		return file(this.fileMatcher==null?matcher:this.fileMatcher.and(matcher));
	}

	protected List<File> matchAllFiles(){
		File baseDirFile = new File(baseDir);
		return matchAllFiles(baseDirFile);
	}
	protected List<File> matchAllFiles(File baseDirFile){
		return FileUtils.matchFiles(baseDirFile, fileMatcher, true);
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
		return andFile(FileMatcher.subDirIs(dirs));
	}
	public R andSubDirIsNot(String...dirs){
		return andFile(FileMatcher.subDirIsNot(dirs));
	}
	public R andSubDirIs(String dir, String dirSeperator){
		String[] dirs = StringUtils.split(dir, dirSeperator);
		return andFile(FileMatcher.subDirIs(dirs));
	}
	
	public R andPostfixIsAnyOf(String...postfix){
		return andFile(FileMatcher.fileNameEndWith(postfix));
	}
	
	public R andFilePathContains(String...paths){
		return andFile(FileMatcher.filePathContains(paths));
	}
	
	public R andFilePathNotContains(String path){
		return andFile((baseDir, file)->{
			return !FileMatcher.filePathContains(path).match(baseDir, file);
		});
	}

	
	public R andIgnoreDirContains(String...ignoreDirs){
		return andRelativePathIgnore(ignoreDirs);
	}

	public R andRelativePathIgnore(String...relativeDirs){
		return andFile((baseDir, file)-> {
			return !FileUtils.relativeDirPathContains(file, baseDir, relativeDirs);
		});
	}
	
	public R andRelativePathContains(String...relativeDirs){
		return andFile((baseDir, file)-> {
			return FileUtils.relativeDirPathContains(file, baseDir, relativeDirs);
		});
	}

	protected void beforeProcess(String baseDir) throws Exception {
	}
	protected void afterProcess(String baseDir) throws Exception {
	}

	public void process(){
		try {
			beforeProcess(baseDir);
		} catch (Exception e) {
			throw new BaseException("execute beforeProcess error: " + e.getMessage(), e);
		}
		matchAllFiles().forEach(file->{
			fileProcess(file);
		});
		try {
			afterProcess(baseDir);
		} catch (Exception e) {
			throw new BaseException("execute afterProcess error: " + e.getMessage(), e);
		}
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
