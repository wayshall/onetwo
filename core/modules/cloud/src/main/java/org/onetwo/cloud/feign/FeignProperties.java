package org.onetwo.cloud.feign;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;
import org.onetwo.common.utils.LangOps;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

import feign.Logger;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(FeignProperties.PROPERTIES_PREFIX)
@Data
public class FeignProperties {

	public static final String PROPERTIES_PREFIX = "jfish.cloud.feign";
	/***
	 * jfish.cloud.feign.base.contextPath
	 */
//	public static final String FEIGN_BASE_PATH_KEY = FeignProperties.PROPERTIES_PREFIX+".base.";
	public static final String FEIGN_CONTEXT_PATH_KEY = PROPERTIES_PREFIX+".base.contextPath";
	
	public static final String ENABLE_KEY = PROPERTIES_PREFIX + ".enabled";
	
	public static final String RETRYER_KEY = PROPERTIES_PREFIX + ".retryer";
	public static final String RETRYER_NEVER_RETRY = "never";
	public static final String RETRYER_FEIGN_DEFAULT = "feign_default";
	
//	public static final String LOCAL_ENABLE_KEY = PROPERTIES_PREFIX + ".local.enabled";
	
	/***
	 * 拒绝插件路径
	 */
	boolean rejectPluginContextPath = true;
	LoggerProps logger = new LoggerProps();
	OkHttpClientProps okHttpClient = new OkHttpClientProps();
	
	String[] keepHeaders;
	
	LocalProps local = new LocalProps();
	Map<String, ServiceProps> services = Maps.newHashMap();
	
	@Data
	public static class LocalProps {
		public static final String ENABLE_KEY = PROPERTIES_PREFIX + ".local.enabled";
		boolean enabled;
	}

	
	@Data
	public static class ServiceProps {
		String basePath;
		String url;
	}
	/***
	 * 
jfish: 
    cloud: 
        feign: 
            logger: 
                level: BASIC
                autoChangeLevel: true
	 * @author wayshall
	 *
	 */
	@Data
	public class LoggerProps {
		Logger.Level level;
		/***
		 * feign的日志使用debug打印，设置是否自动修改相关的client logger的级别为debug
		 */
		boolean autoChangeLevel = true;
	}
	
	@Data
	public class OkHttpClientProps {
		String readTimeout = "50s";
		String connectTimeout = "10s";
		String writeTimeout = "50s";
		
		public Pair<Integer, TimeUnit> getReadTimeoutTime() {
			Pair<Integer, TimeUnit> tu = LangOps.parseTimeUnit(readTimeout);
			return tu;
		}
		public Pair<Integer, TimeUnit> getConnectTimeoutTime() {
			return LangOps.parseTimeUnit(connectTimeout);
		};
		public Pair<Integer, TimeUnit> getWriteTimeoutTime() {
			return LangOps.parseTimeUnit(writeTimeout);
		}
		
	}

}
