package org.onetwo.common.file;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.md.Hashs;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;


public class SimpleFileStorer implements FileStorer<SimpleFileStoredMeta>{
	
	public static final StoreFilePathStrategy SIMPLE_STORE_STRATEGY = (storeBaseDir, appContextDir, ctx)->{
		NiceDate now = NiceDate.New();
		
		//写入文件名=md5(原文件名+随机字符串)
		String newfn = FileUtils.getFileNameWithoutExt(ctx.getFileName())+"-"+now.format("HHmmssSSS")+"-"+LangUtils.getRadomString(5);
		newfn = Hashs.MD5.hash(newfn).toLowerCase();
		newfn += FileUtils.getExtendName(ctx.getFileName(), true);

		// /appContextDir/moduleDir/yyyy-MM-dd//orginFileName-HHmmssSSS-randomString.ext
		String accessablePath = StoreFilePathStrategy.getAppModulePath(storeBaseDir, appContextDir, ctx);
		accessablePath = accessablePath + now.formatAsDate()+"/"+newfn;
		
		//sotreDir/appContextDir/moduleDir/yyyy-MM-dd//orginFileName-HHmmssSSS-randomString.ext
		String storedServerLocalPath = storeBaseDir + accessablePath;
		
		SimpleFileStoredMeta meta = new SimpleFileStoredMeta(ctx.getFileName(), storedServerLocalPath);
		meta.setSotredFileName(newfn);
		meta.setAccessablePath(accessablePath);
		meta.setBizModule(ctx.getModule());
//		meta.setFullAccessablePath(fullAccessablePath);
		return meta;
	};
	
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
