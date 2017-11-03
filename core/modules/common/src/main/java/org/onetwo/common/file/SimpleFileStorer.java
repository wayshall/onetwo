package org.onetwo.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;


public class SimpleFileStorer implements FileStorer<SimpleFileStoredMeta>{
	
	public static final StoreFilePathStrategy SIMPLE_STORE_STRATEGY = (storeBaseDir, appContextDir, ctx)->{
//		NiceDate now = NiceDate.New();
		
		String newfn = ctx.getKey();
		String accessablePath = StoreFilePathStrategy.getAppModulePath(storeBaseDir, appContextDir, ctx);
		if(StringUtils.isBlank(newfn)){
			newfn = StringUtils.emptyIfNull(ctx.getModule())+"-"+UUID.randomUUID().toString()+FileUtils.getExtendName(ctx.getFileName(), true);
		}
		newfn += FileUtils.getExtendName(ctx.getFileName(), true);
		// /appContextDir/moduleDir/yyyy-MM-dd//uuid.ext
		accessablePath = accessablePath + newfn;

		
		//sotreDir/appContextDir/moduleDir/yyyy-MM-dd//orginFileName-HHmmssSSS-randomString.ext
		String storedServerLocalPath = storeBaseDir + accessablePath;
		
		SimpleFileStoredMeta meta = new SimpleFileStoredMeta(ctx.getFileName(), storedServerLocalPath);
		meta.setSotredFileName(newfn);
		meta.setAccessablePath(accessablePath);
		meta.setBizModule(ctx.getModule());
//		meta.setFullAccessablePath(fullAccessablePath);
		return meta;
	};
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
//	private StoreFilePathStrategy strategy = SIMPLE_STORE_STRATEGY;
	protected String storeBaseDir;
	protected String appContextDir;
	
	@Override
	public SimpleFileStoredMeta write(StoringFileContext context) {
		SimpleFileStoredMeta meta = getStoreDir(context);
		doStoring(meta, context);
		/*if(StringUtils.isNotBlank(appContextDir)){
			returnPath = StringUtils.trimEndWith(appContextDir, "/") + returnPath;
		}*/
		return meta;
	}
	
	/***
	 * 保存文件
	 * @author wayshall
	 * @param meta
	 * @param context
	 */
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

	
	public void readFileTo(final String accessablePath, final OutputStream output){
		String fullPath = storeBaseDir + accessablePath;
		InputStream in = null;
		try {
			in = new FileInputStream(fullPath);
			IOUtils.copy(in, output);
		} catch (Exception e) {
			throw new BaseException("read file error!", e);
		}
	}
	

	public InputStream readFileStream(final String accessablePath){
		String fullPath = storeBaseDir + accessablePath;
		InputStream in = null;
		try {
			in = new FileInputStream(fullPath);
			return in;
		} catch (Exception e) {
			throw new BaseException("read file error!", e);
		}
	}

	@Override
	public long getLastModified(String accessablePath) {
		String fullPath = storeBaseDir + accessablePath;
		File file = new File(fullPath);
		return file.exists()?file.lastModified():-1;
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
