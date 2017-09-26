package org.onetwo.cloud.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(prefix=BootJfishCloudConfig.CONFIG_PREFIX)
@Data
public class BootJfishCloudConfig {
	
	public static final String CONFIG_PREFIX = "jfish.cloud"; 
	public static final String ZUUL_FIXHEADERS_ENABLED = CONFIG_PREFIX + ".zuul.endabledFixHeader"; 
	
	ZuulConfig zuul = new ZuulConfig();
	
	@Data
	public class ZuulConfig {
		List<FixHeadersConfig> fixHeaders = new ArrayList<>();
	}

	@Data
	static public class FixHeadersConfig {
		List<String> pathPatterns;
		String header;
		String value;
		PathMatcher matcher = PathMatcher.ANT;
		Map<String, Pattern> patterns = null;
		
		public Map<String, Pattern> getPatterns(){
			Map<String, Pattern> patterns = this.patterns;
			if(patterns==null || patterns.isEmpty()){
				patterns = pathPatterns.stream().collect(Collectors.toMap(path->path, path->Pattern.compile(path)));
			}
			return patterns;
		}
	}
	
	public static enum PathMatcher {
		ANT,
		REGEX
	}
}
