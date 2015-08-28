package org.onetwo.common.file;

import org.onetwo.common.date.NiceDate;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;


public class SimpleFileStorer implements FileStorer<SimpleFileStoredMeta>{
	
	private StoreFilePathStrategy strategy;
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
		Assert.notNull(strategy, "strategy can not be null");
		String storePath = strategy.getStoreFilePath(storeBaseDir, context);
		return storePath;
	}


	public void setStrategy(StoreFilePathStrategy strategy) {
		this.strategy = strategy;
	}
	
	public void setStoreBaseDir(String storeDir){
		this.storeBaseDir = storeDir;
		this.strategy = (storeBaseDir, ctx)->{
			NiceDate now = NiceDate.New();
			String newfn = FileUtils.getFileNameWithoutExt(ctx.getFileName())+"-"+now.format("HHmmssSSS")+"-"+LangUtils.getRadomString(5);
			newfn += FileUtils.getExtendName(ctx.getFileName(), true);
			
			String baseDir = FileUtils.convertDir(storeBaseDir);
			if(StringUtils.isNotBlank(ctx.getModule())){
				baseDir += FileUtils.convertDir(ctx.getModule());
			}
			return baseDir + now.formatAsDate()+"/"+newfn;
		};
	}

	public void setKeepContextPath(String keepContextPath) {
		this.keepContextPath = keepContextPath;
	}

	public String getKeepContextPath() {
		return keepContextPath;
	}

	
}
