package org.onetwo.plugins.jdoc.utils;

import java.io.File;

import org.onetwo.common.utils.FileUtils;

public class JDocPluginUtils {

	public static File getProjectDir(){
		String baseDirPath = FileUtils.getResourcePath("");
		File baseDir = new File(baseDirPath);
		baseDir = baseDir.getParentFile().getParentFile();
		return baseDir;
	}
	
	public static String getProejctSrcDir(){
		String sourceDir = JDocPluginUtils.getProjectDir().getPath()+"/src/main/java";
		return sourceDir;
	}
	
	public static String getProejctSrcTestDir(){
		String sourceDir = JDocPluginUtils.getProjectDir().getPath()+"/src/test/java";
		return sourceDir;
	}
	
	private JDocPluginUtils(){};
	
}
