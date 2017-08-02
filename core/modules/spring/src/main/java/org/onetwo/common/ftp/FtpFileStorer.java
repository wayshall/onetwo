package org.onetwo.common.ftp;

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
		Assert.notNull(ftpConfig);
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
