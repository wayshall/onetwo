package org.onetwo.boot.dsrouter;

import java.util.Map;

import lombok.Data;

import org.onetwo.common.propconf.JFishProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(prefix=org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".dsRouter")
@Data
public class DatasourceRouterProperties {
	
	String lookupStrategy;
	DsProperties common = new DsProperties();
	Map<String, DsProperties> targets = Maps.newLinkedHashMap();

	HeaderLookupStrategy headerStrategy = new HeaderLookupStrategy();
	
	@SuppressWarnings("serial")
	public static class DsProperties extends JFishProperties {
		
		public String getUrl(){
			return getProperty("url");
		}
		public String getDriverClassName(){
			return getProperty("driverClassName");
		}
		public String getUsername(){
			return getProperty("username");
		}
		public String getPassword(){
			return getProperty("password");
		}
	}
	
	@Data
	public static class HeaderLookupStrategy {
		String headerName;
	}

}
