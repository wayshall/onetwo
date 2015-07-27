package org.onetwo.boot.core.config;

import java.util.Properties;
import java.util.stream.Stream;

import lombok.Data;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;


/***
 * 某些专属jfish的配置
 * @author way
 *
 */
@ConfigurationProperties(prefix="jfish")
@Data
public class JFishBootConfig {
	
	private String ftlDir = "/jfish/ftl";

	private Properties mediaTypes;
	
	private UploadConfig upload = new UploadConfig();

	@Data
	public static class UploadConfig {
		private StoreType storeType = StoreType.LOCAL;
		private String fileStorePath;
		private int maxUploadSize = 1024*1024*50; //50m
		
		//ftp
		private String ftpEncoding = LangUtils.UTF8;
		private String ftpServer;
		private int ftpPort = 21;
		private String ftpUser;
		private String ftpPassword;
		private String ftpBaseDir;
	}
	
	public static enum StoreType {
		LOCAL,
		FTP;
		
		public static StoreType of(String str){
			if(StringUtils.isBlank(str)){
				return LOCAL;
			}
			return Stream.of(values()).filter(t->t.name().equalsIgnoreCase(str))
								.findFirst().orElse(LOCAL);
		}
	}
	
}
