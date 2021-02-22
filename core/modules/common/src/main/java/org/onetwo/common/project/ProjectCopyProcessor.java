package org.onetwo.common.project;

import java.io.File;

import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.StringUtils;

/**
 * @author weishao zeng
 * <br/>
 */

public class ProjectCopyProcessor extends BaseFileProcessor<ProjectCopyProcessor> {
//	private String baseDir;
	private String templateModuleName;
	private String newModuleName;
	private String sourceBaseDir;
	private String targetBaseDir;
	private String charset = FileUtils.UTF8;

	public ProjectCopyProcessor(ProjectRefactor project, String baseDir, String templateModuleName, String newModuleName, String modulePostfix) {
		super(project, FileUtils.convertDir(baseDir) + templateModuleName + modulePostfix);
//		this.baseDir = baseDir;
		this.templateModuleName = templateModuleName;
		this.newModuleName = newModuleName;
		this.sourceBaseDir = FileUtils.convertDir(baseDir) + templateModuleName + modulePostfix;
		this.targetBaseDir = FileUtils.convertDir(baseDir) + newModuleName + modulePostfix;
	}

	protected void beforeProcess(String baseDir) throws Exception {
//		FileUtils.deleteDirectory(targetBaseDir);
	}
	
	@Override
	protected void fileProcess(File file) {
		String relaPath = StringUtils.substringAfter(file.getPath(), sourceBaseDir);
		relaPath = relaPath.replace("/" + templateModuleName + "/", "/" + newModuleName + "/");
		relaPath = relaPath.replace(StringUtils.capitalize(templateModuleName), StringUtils.capitalize(newModuleName));
		String targetPath = targetBaseDir + relaPath;
		File targetFile = new File(targetPath);
		if (targetFile.isFile() && targetFile.exists()) {
			targetFile.delete();
		}
		
		logger.info("copy to target: {}", targetPath);
		if (isJavaFile(file) || isSpringFactoriesFile(file)) {
			String content = FileUtils.readAsString(file);
			content = content.replace("odysseus." + templateModuleName + ".", "odysseus." + newModuleName + ".");
			content = content.replace(StringUtils.capitalize(templateModuleName), StringUtils.capitalize(newModuleName));
			content = content.replace("\"" + templateModuleName + "\"", "\"" + newModuleName + "\"");
			
//			StringBuilder content = new StringBuilder();
//			FileUtils.readFile(file, (line, index) -> {
//				return true;
//			});
			FileUtils.writeStringToFile(targetFile, charset, content);
		} else if (isPomFile(file) || isLogFile(file)) {
			String content = FileUtils.readAsString(file);
			content = content.replace(templateModuleName + "-", newModuleName + "-");
			FileUtils.writeStringToFile(targetFile, charset, content);
		} else if (isYamlFile(file)) {
			String content = FileUtils.readAsString(file);
			content = content.replace(templateModuleName, newModuleName);
			FileUtils.writeStringToFile(targetFile, charset, content);
		} else if (file.isFile()) {
			FileUtils.copyFile(file, targetFile);
		}
	}

//	public ProjectCopyProcessor andPackgeIsNot(String...dirs){
//		return andFileMatcher(FileMatcher.subDirIs(dirs));
//	}
	
	private boolean isJavaFile(File file) {
		return file.getName().toLowerCase().endsWith(".java");
	}
	
	private boolean isPomFile(File file) {
		return file.getName().toLowerCase().equals("pom.xml");
	}
	
	private boolean isLogFile(File file) {
		return file.getName().toLowerCase().equals("logback.xml");
	}
	
	private boolean isYamlFile(File file) {
		return file.getPath().toLowerCase().endsWith(".yaml");
	}
	
	private boolean isSpringFactoriesFile(File file) {
		return file.getName().toLowerCase().equals("spring.factories");
	}
	
	
}
