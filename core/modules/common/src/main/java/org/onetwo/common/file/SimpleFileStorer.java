package org.onetwo.common.file;

import org.onetwo.common.date.NiceDate;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;


public class SimpleFileStorer implements FileStorer<SimpleFileStoredMeta>{
	
	public static final StoreFilePathStrategy SIMPLE_STORE_STRATEGY = (storeBaseDir, ctx)->{
		NiceDate now = NiceDate.New();
		String newfn = FileUtils.getFileNameWithoutExt(ctx.getFileName())+"-"+now.format("HHmmssSSS")+"-"+LangUtils.getRadomString(5);
		newfn += FileUtils.getExtendName(ctx.getFileName(), true);

		//sotreDir/moduleDir/yyyy-MM-dd//orginFileName-HHmmssSSS-randomString.ext
		String baseDir = StoreFilePathStrategy.getModuleDir(storeBaseDir, ctx);
		return baseDir + now.formatAsDate()+"/"+newfn;
	};
	
//	private StoreFilePathStrategy strategy = SIMPLE_STORE_STRATEGY;
	private String storeBaseDir;
	private String keepContextPath;
	
	@Override
	public SimpleFileStoredMeta write(StoringFileContext context) {
		String storePath = getStoreDir(context);
		doStoring(storePath, context);
		String returnPath = StringUtils.substringAfter(storePath, storeBaseDir);
		if(StringUtils.isNotBlank(keepContextPath)){
			returnPath = StringUtils.trimEndWith(keepContextPath, "/") + returnPath;
		}
		return new SimpleFileStoredMeta(returnPath);
	}
	
	protected void doStoring(String storePath, StoringFileContext context){
		FileUtils.writeInputStreamTo(context.getInputStream(), storePath);
	}
	
	protected String getStoreDir(StoringFileContext context){
//		Assert.notNull(context.getStoreFilePathStrategy(), "strategy can not be null");
		if(StringUtils.isBlank(storeBaseDir)){
			throw new BaseException("store dir must be config, but : " + storeBaseDir);
		}
		StoreFilePathStrategy strategy = context.getStoreFilePathStrategy();
		if(strategy==null){
			strategy = SIMPLE_STORE_STRATEGY;
		}
		String storePath = strategy.getStoreFilePath(storeBaseDir, context);
		return storePath;
	}

	public void setStoreBaseDir(String storeDir){
		this.storeBaseDir = storeDir;
	}

	public void setKeepContextPath(String keepContextPath) {
		this.keepContextPath = keepContextPath;
	}

	public String getKeepContextPath() {
		return keepContextPath;
	}

	
}
