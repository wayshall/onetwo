package org.onetwo.boot.module.sftp;

import java.util.Properties;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.common.exception.BaseException;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(SftpProperties.PREFIX)
@Data
public class SftpProperties {
	
	public static final String PREFIX = BootJFishConfig.PREFIX + ".sftp";
	public static final String ENABLED_KEY = PREFIX + ".username";

	String host;
	int port = 22;
	String username;

	UserAuthTypes authType = UserAuthTypes.PUBLIC_KEY;
	
	String password;
	
	String privateKeyPath;
	String passphrase;
	
	String baseDir;

    boolean alwaysStoreFullPath;
	String pathTag;
	String endPoint;
	
	Properties configs = new Properties();
	
	public String getDownloadUrl(String fileName){
		StringBuilder url = new StringBuilder(endPoint);
		url.append(fileName);
		return url.toString();
	}
	

	public static enum UserAuthTypes {
		PASSWORD,
		PUBLIC_KEY;
	}
}
