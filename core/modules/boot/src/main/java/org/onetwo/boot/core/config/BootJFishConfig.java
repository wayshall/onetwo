package org.onetwo.boot.core.config;

import java.util.Properties;
import java.util.stream.Stream;

import lombok.Data;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;


/***
 * 某些专属jfish的配置
 * @author way
 *
 */
@ConfigurationProperties(prefix="jfish")
@Data
public class BootJFishConfig {
	
	@Autowired
	private BootSpringConfig bootSpringConfig;
	
	private String ftlDir = "/jfish/ftl";
	private Properties mediaTypes;
	
	private UploadConfig upload = new UploadConfig();
	private JsonConfig json = new JsonConfig();
	
	private MessageSourceConfig messageSource = new MessageSourceConfig();
	
	@Data
	public class MessageSourceConfig {
		private int cacheSeconds = -1;//cache forever
	}

	@Data
	public class JsonConfig {
		private Boolean prettyPrint;
		
		public boolean isPrettyPrint(){
			 if(prettyPrint==null)
				 return !bootSpringConfig.isProduct();
			 else
				 return prettyPrint;
		}
	}
	
	@Data
	public class UploadConfig {
		private StoreType storeType = StoreType.LOCAL;
		private String fileStorePath;
		private String keepContextPath;
		private int maxUploadSize = 1024*1024*50; //50m
		
		//ftp
		private String ftpEncoding = LangUtils.UTF8;
		private String ftpServer;
		private int ftpPort = 21;
		private String ftpUser;
		private String ftpPassword;
//		private String ftpBaseDir;
		
		/*public int getMaxUploadSize(){
			return maxUploadSize;
		}*/
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
