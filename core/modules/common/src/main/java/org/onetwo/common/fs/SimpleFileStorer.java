package org.onetwo.common.fs;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.NiceDate;
import org.onetwo.common.utils.file.FileUtils;


public class SimpleFileStorer implements FileStorer<SimpleFileStoredMeta>{
	
	private StoreFilePathStrategy strategy;
	
	@Override
	public SimpleFileStoredMeta write(StoringFileContext context) {
		String storePath = getStoreDir(context);
		doStoring(storePath, context);
		return new SimpleFileStoredMeta(storePath);
	}
	
	protected void doStoring(String storePath, StoringFileContext context){
		FileUtils.writeInputStreamTo(context.getInputStream(), storePath);
	}
	
	protected String getStoreDir(StoringFileContext context){
		Assert.notNull(strategy, "strategy can not be null");
		String storePath = strategy.getStoreFilePath(context);
		return storePath;
	}


	public void setStrategy(StoreFilePathStrategy strategy) {
		this.strategy = strategy;
	}
	
	public void setStoreDir(String storeDir){
		this.strategy = ctx->{
			NiceDate now = NiceDate.New();
			String newfn = FileUtils.getFileNameWithoutExt(ctx.getFileName())+"-"+now.format("HHmmssSSS")+"-"+LangUtils.getRadomString(5);
			newfn += FileUtils.getExtendName(ctx.getFileName(), true);
			return FileUtils.convertDir(storeDir) + now.formatAsDate()+"/"+newfn;
		};
	}
	
}
