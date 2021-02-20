package org.onetwo.common.project;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.file.FileMatcher;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
public class ProjectRefactor {
	final protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	final private String baseDir;
	private String charset = FileUtils.DEFAULT_CHARSET;
	private List<BaseFileProcessor<?>> fileRefactors = Lists.newArrayList();

	public ProjectRefactor(String baseDir) {
		super();
		this.baseDir = baseDir;
	}
	
	public ProjectRefactor charset(String charset) {
		this.charset = charset;
		return this;
	}
	
	public <T extends BaseFileProcessor<T>> T newFileProcessor(T fileProcessor){
		fileRefactors.add(fileProcessor);
		return fileProcessor;
	}
	
	public ZipFileProcessor newZipFile(String targetZipFilePath){
		return newFileProcessor(new ZipFileProcessor(this, baseDir, targetZipFilePath));
	}
	
	public DirCopyProcessor newDirCopy(String sourceSubDir, String targetSubDir){
		return newFileProcessor(new DirCopyProcessor(this, baseDir, sourceSubDir, targetSubDir));
	}
	
	public ProjectCopyProcessor newProjectCopy(String templateModuleName, String newModuleName, String modulePostfix){
		return newFileProcessor(new ProjectCopyProcessor(this, baseDir, templateModuleName, newModuleName, modulePostfix));
	}
	
	public ExecutorFileProcessor newExecutor(FileProccessExecutor callback){
		return newFileProcessor(new ExecutorFileProcessor(this, baseDir, callback));
	}
	
	public FileReplacementsRefactor newFileReplacement(){
		FileReplacementsRefactor r = new FileReplacementsRefactor(this, baseDir);
		r.charset(charset);
		fileRefactors.add(r);
		return r;
	}
	
	public FileDeleteRefactor newFileDelete(){
		FileDeleteRefactor r = new FileDeleteRefactor(this, baseDir);
		fileRefactors.add(r);
		return r;
	}
	
	public void execute(){
		fileRefactors.forEach(r->{
			r.process();
		});
	}
	
	public class FileReplacementsRefactor extends BaseFileProcessor<FileReplacementsRefactor> {

		private Map<String, String> textReplacements = Maps.newLinkedHashMap();
		private String charset = FileUtils.DEFAULT_CHARSET;
		
		public FileReplacementsRefactor(ProjectRefactor project, String baseDir) {
			super(project, baseDir);
		}
		public FileReplacementsRefactor charset(String charset) {
			this.charset = charset;
			return this;
		}
		
		public FileReplacementsRefactor textReplace(String source, String target){
			textReplacements.put(source, target);
			return this;
		}

		@Override
		protected void fileProcess(File file) {
			if(file.isDirectory()){
				return ;
			}
			logger.info("doTextReplacement for : " + file.getPath());
			String text = FileUtils.readAsString(file, charset);
			for(Entry<String, String> entry : this.textReplacements.entrySet()){
				text = text.replace(entry.getKey(), entry.getValue());
			}
			FileUtils.writeStringToFile(file, charset, text);
		}
		
		public ProjectRefactor end(){
			return ProjectRefactor.this;
		}
		
	}
	

	public class FileDeleteRefactor extends BaseFileProcessor<FileDeleteRefactor> {
		
		public FileDeleteRefactor(ProjectRefactor project, String baseDir) {
			super(project, baseDir);
		}

		@Override
		protected void fileProcess(File file) {
			if(FileUtils.delete(file, true)){
				logger.info("delete succeed: {}", file.getPath());
			}else{
				logger.info("delete failed : {}", file.getPath());
			}
		}
		

		public FileDeleteRefactor orFileNameEndWith(String...postfix){
			return orFileMatcher(FileMatcher.fileNameEndWith(postfix));
		}
		
		public FileDeleteRefactor orDirNameIs(String...dirNames){
			return orFileMatcher(FileMatcher.dirNameIs(dirNames));
		}
		
		public FileDeleteRefactor orFileNameIs(String...fileNames){
			return orFileMatcher(FileMatcher.fileNameIs(fileNames));
		}
		
	}

}
