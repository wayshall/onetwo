package org.onetwo.boot.module.oauth2.bearer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties;
import org.onetwo.boot.utils.PathMatcher;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@ConfigurationProperties(BearerTokenProperties.BEARER_TOKEN_KEY)
@Data
public class BearerTokenProperties {
	
	public static final String BEARER_TOKEN_KEY = JFishOauth2Properties.CONFIG_PREFIX + ".bearerToken";

	Map<String, BearerHeadersConfig> headers = Maps.newHashMap();
	boolean debug;
	

	@Data
	static public class BearerHeadersConfig {
		List<String> pathPatterns;
		String value;
		PathMatcher matcher = PathMatcher.ANT;
		Map<String, Pattern> patterns = null;
		
		public Map<String, Pattern> getPatterns(){
			if (matcher!=PathMatcher.REGEX) {
				return Collections.emptyMap();
			}
			Map<String, Pattern> patterns = this.patterns;
			if(patterns==null || patterns.isEmpty()){
				patterns = pathPatterns.stream().collect(Collectors.toMap(path->path, path->Pattern.compile(path)));
			}
			return patterns;
		}
	}

}

