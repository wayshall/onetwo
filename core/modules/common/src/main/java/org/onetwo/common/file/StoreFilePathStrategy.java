package org.onetwo.common.file;

import org.onetwo.common.utils.StringUtils;


public interface StoreFilePathStrategy {

	static public String getModuleDir(String storeBaseDir, String appContextDir, StoringFileContext ctx){
		String baseDir = FileUtils.convertDir(storeBaseDir) + FileUtils.convertDir(appContextDir);
		if(StringUtils.isNotBlank(ctx.getModule())){
			baseDir += FileUtils.convertDir(ctx.getModule());
		}
		return baseDir;
	}
	
	String getStoreFilePath(String storeBaseDir, String appContextDir, StoringFileContext context);
}
