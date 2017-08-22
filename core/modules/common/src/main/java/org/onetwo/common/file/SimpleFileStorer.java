package org.onetwo.common.file;

import org.onetwo.common.date.NiceDate;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;


public class SimpleFileStorer implements FileStorer<SimpleFileStoredMeta>{
	
	public static final StoreFilePathStrategy SIMPLE_STORE_STRATEGY = (storeBaseDir, appContextDir, ctx)->{
		NiceDate now = NiceDate.New();
		String newfn = FileUtils.getFileNameWithoutExt(ctx.getFileName())+"-"+now.format("HHmmssSSS")+"-"+LangUtils.getRadomString(5);
		newfn += FileUtils.getExtendName(ctx.getFileName(), true);

		// /appContextDir/moduleDir/yyyy-MM-dd//orginFileName-HHmmssSSS-randomString.ext
		String moduelPath = StoreFilePathStrategy.getAppModulePath(storeBaseDir, appContextDir, ctx);
		moduelPath = moduelPath + now.formatAsDate()+"/"+newfn;
		
		//sotreDir/appContextDir/moduleDir/yyyy-MM-dd//orginFileName-HHmmssSSS-randomString.ext
		String baseDir = storeBaseDir + moduelPath;
		
		SimpleFileStoredMeta meta = new SimpleFileStoredMeta(ctx.getFileName(), baseDir);
		meta.setSotredFileName(newfn);
		meta.setAccessablePath(moduelPath);
//		meta.setFullAccessablePath(fullAccessablePath);
		return meta;
	};
	
//	private StoreFilePathStrategy strategy = SIMPLE_STORE_STRATEGY;
	private String storeBaseDir;
	private String appContextDir;
	
	@Override
	public SimpleFileStoredMeta write(StoringFileContext context) {
		SimpleFileStoredMeta meta = getStoreDir(context);
		doStoring(meta, context);
		/*if(StringUtils.isNotBlank(appContextDir)){
			returnPath = StringUtils.trimEndWith(appContextDir, "/") + returnPath;
		}*/
		return meta;
	}
	
	protected void doStoring(SimpleFileStoredMeta meta, StoringFileContext context){
		FileUtils.writeInputStreamTo(context.getInputStream(), meta.getStoredServerLocalPath());
	}
	
	protected SimpleFileStoredMeta getStoreDir(StoringFileContext context){
//		Assert.notNull(context.getStoreFilePathStrategy(), "strategy can not be null");
		if(StringUtils.isBlank(storeBaseDir)){
			throw new BaseException("store dir must be config, but blank ");
		}
		StoreFilePathStrategy strategy = context.getStoreFilePathStrategy();
		if(strategy==null){
			strategy = SIMPLE_STORE_STRATEGY;
		}
		SimpleFileStoredMeta meta = (SimpleFileStoredMeta)strategy.getStoreFilePath(storeBaseDir, appContextDir, context);
		return meta;
	}

	public void setStoreBaseDir(String storeDir){
		this.storeBaseDir = storeDir;
	}

	public String getAppContextDir() {
		return appContextDir;
	}

	public void setAppContextDir(String appContextDir) {
		this.appContextDir = appContextDir;
	}

}
