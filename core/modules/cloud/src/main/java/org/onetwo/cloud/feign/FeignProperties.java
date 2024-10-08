package org.onetwo.cloud.feign;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;
import org.onetwo.boot.module.oauth2.util.OAuth2Utils;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.StringUtils;
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

	public static final String PROPERTIES_PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".cloud.feign";
	
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
	
	OAuth2Props oauth2 = new OAuth2Props();
	
	@Data
	public static class LocalProps {
		public static final String ENABLE_KEY = PROPERTIES_PREFIX + ".local.enabled";
		boolean enabled;
		LocalTransactionModes transactonMode = LocalTransactionModes.NONE;
	}
	
	public static enum LocalTransactionModes {
		/***
		 * 无，直接调用
		 */
		NONE,
		/****
		 * 使用spring的required_new事务模拟
		 */
		REQUIRED_NEW
	}

	
	@Data
	public static class ServiceProps {
		String basePath;
//		String url;
	}

	@Data
	public static class OAuth2Props {
		String authorization;
		
		public String getBearerHeader() {
			if (StringUtils.isBlank(authorization)) {
				return authorization;
			} else {
				return StringUtils.appendStartWith(authorization, OAuth2Utils.BEARER_TYPE + " ");
			}
		}
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
