package org.onetwo.boot.core.config;

import java.util.Properties;

import lombok.Data;

import org.onetwo.common.jfishdbm.mapping.DefaultDataBaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;


/***
 * 某些专属jfish的配置
 * 一般用于增强配置framework
 * @author way
 *
 */
@ConfigurationProperties(prefix="jfish")
@Data
public class BootJFishConfig {
	
	@Autowired
	private BootSpringConfig bootSpringConfig;
	
//	private String ftlDir = "/jfish/ftl";
	private MessageSourceConfig messageSource = new MessageSourceConfig();
	
	private DefaultDataBaseConfig dbm = new DefaultDataBaseConfig();
	
	private MvcConfig mvc = new MvcConfig();
	/*private JsonConfig json = new JsonConfig();
	private Properties mediaTypes;*/
	
	//security=BootSecurityConfig
	
	@Data
	public class MessageSourceConfig {
		private int cacheSeconds = -1;//cache forever
	}
	
	@Data
	public class MvcConfig {
		private Properties mediaTypes;
		private JsonConfig json = new JsonConfig();

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
		
	}
	
	
	
}
