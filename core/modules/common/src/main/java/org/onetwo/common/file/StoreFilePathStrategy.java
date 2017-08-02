package org.onetwo.common.file;

import org.onetwo.common.utils.StringUtils;


public interface StoreFilePathStrategy {

	static public String getAppModulePath(String storeBaseDir, String appContextDir, StoringFileContext ctx){
		String baseDir = FileUtils.convertDir(appContextDir);
		baseDir = StringUtils.appendStartWith(baseDir, FileUtils.SLASH);
		if(StringUtils.isNotBlank(ctx.getModule())){
			baseDir += FileUtils.convertDir(ctx.getModule());
		}
		return baseDir;
	}
	
	/****
	 * local store path
	 * @author wayshall
	 * @param storeBaseDir
	 * @param appContextDir
	 * @param context
	 * @return
	 */
	FileStoredMeta getStoreFilePath(String storeBaseDir, String appContextDir, StoringFileContext context);
}
