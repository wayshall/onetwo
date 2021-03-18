package org.onetwo.boot.module.sftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.validation.constraints.NotNull;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.boot.module.sftp.SftpProperties.UserAuthTypes;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.GuavaUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * @author weishao zeng
 * <br/>
 */

public class SshClientManager {
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private SftpProperties sftpProperties;
	private Session sshSession;
	private ChannelSftp sftpChannel;
	private JSch jsch;


	public SshClientManager(SftpProperties sftpProperties) {
		super();
		Assert.hasText(sftpProperties.getHost(), "host can not be blank");
		Assert.hasText(sftpProperties.getUsername(), "username can not be blank");
		this.sftpProperties = sftpProperties;
	}

	public void init() {
		try {
			this.init0();
		} catch (Exception e) {
			this.destroy();
			throw new BaseException("init sftp error: " + e.getMessage(), e);
		}
	}
	
	private void init0() throws Exception {
		if (StringUtils.isBlank(sftpProperties.getBaseDir())) {
			throw new BaseException("baseDir of sftp can not be blank, check config: " + SftpProperties.PREFIX);
		}
		
		jsch = new JSch();
		Session sshSession = null;
		if (sftpProperties.getAuthType()==UserAuthTypes.PUBLIC_KEY) {
			if (StringUtils.isBlank(sftpProperties.getPassphrase())) {
				jsch.addIdentity(sftpProperties.getPrivateKeyPath());
			} else {
				jsch.addIdentity(sftpProperties.getPrivateKeyPath(), sftpProperties.getPassphrase());
			}
			sshSession = jsch.getSession(sftpProperties.getUsername(), sftpProperties.getHost(), sftpProperties.getPort());
			sshSession.setConfig("StrictHostKeyChecking", "no");
		} else {
			sshSession = jsch.getSession(sftpProperties.getUsername(), sftpProperties.getHost(), sftpProperties.getPort());
			sshSession.setPassword(sftpProperties.getPassword());
			sshSession.setConfig("userauth.gssapi-with-mic", "no");
			sshSession.setConfig("StrictHostKeyChecking", "no");
		}
		
		sshSession.setConfig(this.sftpProperties.getConfigs());
		
		this.sshSession = sshSession;
		sshSession.connect();
		
		// 获取sftp通道
        sftpChannel = (ChannelSftp)sshSession.openChannel("sftp");
        sftpChannel.connect();
        logger.info("连接ftp成功!");
        
        this.sftpChannel.cd(this.sftpProperties.getBaseDir());
	}
	

	public void retrieveFile(String storeAccessablePath, final OutputStream output){
		checkClientActive();
		
		InputStream in = null;
		try {
			in = retrieveFileStream(storeAccessablePath);
			IOUtils.copy(in, output);
		} catch (Exception e) {
			throw new BaseException("read file to ftp error: " + e.getMessage(), e);
		} finally{
			IOUtils.closeQuietly(in);
//			IOUtils.closeQuietly(output);
		}
	}
	
	public InputStream retrieveFileStream(String storeAccessablePath){
		checkClientActive();
		
		try {
			String fullPath = FileUtils.convertDir(sftpProperties.getBaseDir()) + StringUtils.trimStartWith(storeAccessablePath, FileUtils.SLASH);
			File file = new File(fullPath);
			String dir = FileUtils.convertDir(file.getParent());
			if (!isDirExist(dir)) {
				throw new IOException("dir not found!");
			}
			this.sftpChannel.cd(dir);
			InputStream in = this.sftpChannel.get(file.getName());
			return in;
		} catch (Exception e) {
			throw new BaseException("read file from  sftp error: " + e.getMessage(), e);
		} finally{
		}
	}

	public boolean isClientActive() {
		return this.sshSession!=null && this.sshSession.isConnected() &&
				this.sftpChannel!=null || !this.sftpChannel.isClosed();
	}

	public void checkClientActive() {
		if (!isClientActive()) {
			throw new BaseException("ssh client has closed!");
		}
	}

	public void upload(String remotePath, @NotNull InputStream local){
		checkClientActive();

		String fullPath = FileUtils.convertDir(sftpProperties.getBaseDir()) + StringUtils.trimStartWith(remotePath, FileUtils.SLASH);
		File file = new File(fullPath);
		String dir = FileUtils.convertDir(file.getParent());
		
		try {
			changeAndMakeDirs(dir);
			this.sftpChannel.put(local, file.getName());
		} catch (SftpException e) {
			throw new BaseException("upload file to ftp error: " + e.getMessage(), e);
		} finally{
			IOUtils.closeQuietly(local);
		}
	}
	
	public void changeAndMakeDirs(String dir){
		try {
			if(!isDirExist(sftpProperties.getBaseDir())){
				this.sftpChannel.mkdir(sftpProperties.getBaseDir());
			}
			this.sftpChannel.cd(sftpProperties.getBaseDir());
			
			String subPath = StringUtils.substringAfter(dir, sftpProperties.getBaseDir());
			String[] subDirs = GuavaUtils.split(subPath, FileUtils.SLASH);
			for (String subdir : subDirs) {
				if (!isDirExist(subdir)) {
					this.sftpChannel.mkdir(subdir);
				}
				this.sftpChannel.cd(subdir);
			}
		} catch (SftpException e) {
			throw new BaseException("create sftp dir error: " + dir, e);
		}
	}
	
	public boolean isDirExist(String dir) {
		boolean exist = false;
		try {
			SftpATTRS attr = this.sftpChannel.lstat(dir);
			exist = true;
			return attr.isDir();
		} catch (SftpException e) {
			if (e.getMessage().toLowerCase().equals("no such file")) {
                exist = false;
            }
		}
		return exist;
	}
	
	public void destroy() {
		if (this.sftpChannel!=null) {
			if (!this.sftpChannel.isClosed()) {
				this.sftpChannel.disconnect();
			}
			this.sftpChannel=null;
		}
		if (this.sshSession!=null) {
			if (this.sshSession.isConnected()) {
				this.sshSession.disconnect();
			}
			this.sshSession = null;
		}
		this.jsch = null;
	}
	
}
