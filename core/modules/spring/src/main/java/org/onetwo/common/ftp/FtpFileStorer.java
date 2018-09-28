package org.onetwo.common.ftp;

import java.io.InputStream;
import java.io.OutputStream;

import org.onetwo.common.file.SimpleFileStoredMeta;
import org.onetwo.common.file.SimpleFileStorer;
import org.onetwo.common.file.StoringFileContext;
import org.onetwo.common.ftp.FtpClientManager.FtpConfig;
import org.onetwo.common.ftp.FtpClientManager.LoginParam;
import org.springframework.util.Assert;

public class FtpFileStorer extends SimpleFileStorer  {
	
//	private FtpClientManager ftpClientManager;
	private LoginParam loginParam;
	private FtpConfig ftpConfig;
	
	public FtpFileStorer(FtpConfig ftpConfig){
		Assert.notNull(ftpConfig, "ftpConfig can not be null");
		this.ftpConfig = ftpConfig;
//		this.ftpClientManager = new FtpClientManager(ftpConfig);
	}
	
	@Override
	protected void doStoring(SimpleFileStoredMeta meta, StoringFileContext context){
		FtpClientManager ftpClientManager = new FtpClientManager(ftpConfig);
		try {
			ftpClientManager.init();
			ftpClientManager.login(loginParam);
			ftpClientManager.upload(meta.getStoredServerLocalPath(), context.getInputStream());
		} finally {
			ftpClientManager.destroy();
		}
	}

	@Override
	public void readFileTo(final String accessablePath, final OutputStream output){
		String fullPath = getStoreBaseDir() + accessablePath;
		FtpClientManager ftpClientManager = new FtpClientManager(ftpConfig);
		try {
			ftpClientManager.init();
			ftpClientManager.login(loginParam);
			ftpClientManager.retrieveFile(fullPath, output);
		} finally {
			ftpClientManager.destroy();
		}
	}

	@Override
	public InputStream readFileStream(final String accessablePath){
		String fullPath = getStoreBaseDir() + accessablePath;
		FtpClientManager ftpClientManager = new FtpClientManager(ftpConfig);
		try {
			ftpClientManager.init();
			ftpClientManager.login(loginParam);
			return ftpClientManager.retrieveFileStream(fullPath);
		} finally {
			ftpClientManager.destroy();
		}
	}

	//TODO
	@Override
	public long getLastModified(String accessablePath) {
		return -1;
	}

	/*@Override
	public void destroy() throws Exception {
		this.ftpClientManager.destroy();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.ftpClientManager.init();
		this.ftpClientManager.login(loginParam);
	}*/

	public void setLoginParam(String user, String password) {
		this.loginParam = new LoginParam(user, password);
	}
	
	
}
