package org.onetwo.common.file;

import org.onetwo.common.utils.StringUtils;


public interface StoreFilePathStrategy {

	static public String getModuleDir(String storeBaseDir, StoringFileContext ctx){
		String baseDir = FileUtils.convertDir(storeBaseDir);
		if(StringUtils.isNotBlank(ctx.getModule())){
			baseDir += FileUtils.convertDir(ctx.getModule());
		}
		return baseDir;
	}
	
	String getStoreFilePath(String storeBaseDir, StoringFileContext context);
}
