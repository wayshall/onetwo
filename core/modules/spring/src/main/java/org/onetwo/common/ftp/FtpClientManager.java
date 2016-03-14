package org.onetwo.common.ftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.validation.constraints.NotNull;

import lombok.Data;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Assert;
import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

public class FtpClientManager {
	
	final private Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private FTPClient ftpClient;
	private FtpConfig ftpConfig;
	private LoginParam loginParam;
	private boolean initialized = false;
	
	public FtpClientManager(FtpConfig ftpConfig) {
		super();
		this.ftpConfig = ftpConfig;
	}


	public void init() {
		if(initialized){
			return ;
		}
		
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding(ftpConfig.getEncoding());
		ftpClient.setBufferSize(ftpConfig.getBufferSize());
		ftpClient.setPassiveNatWorkaround(ftpConfig.isPasv());
		
		this.ftpClient = ftpClient;
		this.initialized = true;
	}
	
	public void login(@NotNull LoginParam loginParam){
		this.loginParam = loginParam;
		
		this.init();
		try {
			ftpClient.connect(ftpConfig.getServer(), ftpConfig.getPort());
			ftpClient.login(loginParam.getUser(), loginParam.getPassword());
			ftpClient.changeWorkingDirectory(loginParam.getWorkDir());
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		} catch (Exception e) {
			throw new BaseException("login ftp error: " + e.getMessage(), e);
		} 
	}
	
	public void upload(String remotePath, @NotNull InputStream local){
		Assert.assertTrue("not initialized", initialized);
//		Assert.assertTrue("not connected", ftpClient.isConnected());
		if(!ftpClient.isAvailable()){
			login(loginParam);
		}
		
		if(StringUtils.isBlank(remotePath)){
			remotePath = "/";
		}
		
		try {
			File file = new File(remotePath);
			changeAndMakeDirs(file.getParent());
			boolean rs = ftpClient.storeFile(file.getName(), local);
			if(!rs){
				throw new BaseException("upload error:" + rs);
			}
		} catch (IOException e) {
			throw new BaseException("upload file to ftp error: " + e.getMessage(), e);
		} finally{
			IOUtils.closeQuietly(local);
		}
	}
	
	public void changeAndMakeDirs(String dir){
		String ftpDir = dir;//FileUtils.replaceBackSlashToSlash(dir);
		File dirFile = new File(ftpDir);
		try {
			if(!ftpClient.changeWorkingDirectory(ftpDir)){
				if(ftpClient.getReplyCode()==550){
					changeAndMakeDirs(dirFile.getParent());
				}
				int rcode = ftpClient.mkd(dirFile.getName());
				if(!FTPReply.isPositiveCompletion(rcode)){
					throw new BaseException("cmd[mkd] reply code : "+rcode);
				}
			}
		} catch (IOException e) {
			throw new BaseException("create dir error: " + dir, e);
		}
	}
	
	public void destroy() {
		try {
			if(ftpClient.isConnected())
				this.ftpClient.disconnect();
		} catch (IOException e) {
			logger.error("ftp disconnect error: " + e.getMessage());
		}
		this.initialized = false;
		this.ftpClient = null;
	}

	/*@Override
	public void close() throws IOException {
		destroy();
	}*/


	public FTPClient getFtpClient() {
		return ftpClient;
	}


	@Data
	public static class FtpConfig {
		private String server;
		private int port = 21;
		private String encoding = LangUtils.UTF8;
		private int bufferSize = 1024*8;
		private boolean pasv = true;
	}
	
	@Data(staticConstructor="of")
	public static class LoginParam {
		private String user;
		private String password;
		private String workDir = "/";
		public LoginParam(String user, String password) {
			super();
			this.user = user;
			this.password = password;
		}
		
	}
}
