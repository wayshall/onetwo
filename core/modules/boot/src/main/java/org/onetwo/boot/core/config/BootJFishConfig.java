package org.onetwo.boot.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.onetwo.boot.core.jwt.JwtConfig;
import org.onetwo.boot.core.web.mvc.exception.ExceptionMapping;
import org.onetwo.boot.core.web.mvc.exception.ExceptionMessageFinderConfig;
import org.onetwo.common.utils.LangOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Data;


/***
 * 某些专属jfish的配置
 * 一般用于增强配置framework
 * @author way
 *
 */
@ConfigurationProperties(prefix=BootJFishConfig.ZIFISH_CONFIG_PREFIX)
@Data
public class BootJFishConfig implements ExceptionMessageFinderConfig {
	public static final String ZIFISH_CONFIG_PREFIX = "jfish";
	
	public static final String ENABLE_GRACEKILL = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".grace-kill.enabled";
	public static final String ENABLE_SWAGGER = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".swagger.enabled";
	
	/***
	 * jfish.profiles.debug: true
	 */
	public static final String PROFILES_DEBUG = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".profiles.debug";
	public static final String ENABLE_CORSFILTER = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".corsfilter.enabled";
	public static final String ENABLE_HOST_FILTER = BootJFishConfig.ZIFISH_CONFIG_PREFIX + ".hostfilter.enabled";
	public static final String ENABLE_MVC_CORSFILTER = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mvc.cors-filter";
	public static final String ENABLE_DYNAMIC_LOGGER_LEVEL = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".dynamic.logger-level";
	public static final String ENABLE_DYNAMIC_SETTING = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".dynamic.setting";
//	public static final String ENABLE_MVC_LOGGER_INTERCEPTOR = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mvc.loggerInterceptor";
//	public static final String ENABLE_CORSFILTER = PREFIX + ".corsfilter.enabled";
//	public static final String ENABLE_MVC_CORSFILTER = PREFIX + ".mvc.corsFilter";
//	public static final String ENABLE_DYNAMIC_LOGGER_LEVEL = PREFIX + ".dynamic.loggerLevel";
//	public static final String ENABLE_DYNAMIC_SETTING = PREFIX + ".dynamic.setting";
//	public static final String ENABLE_MVC_LOGGER_INTERCEPTOR = "jfish.mvc.loggerInterceptor";
//	public static final String VALUE_AUTO_CONFIG_WEB_UI = "web-ui";
//	public static final String VALUE_AUTO_CONFIG_WEB_MS = "web-ms";
//	public static final String VALUE_AUTO_CONFIG_DISABLED = "disabled";
	
//	public static final String ENABLE_NEGOTIATING_VIEW = "negotiating-view";
	
	@Autowired
	private BootSpringConfig bootSpringConfig;
	
//	private String ftlDir = "/jfish/ftl";
	private MessageSourceConfig messageSource = new MessageSourceConfig();
	
//	private DefaultDataBaseConfig dbm = new DefaultDataBaseConfig();
	
	private MvcConfig mvc = new MvcConfig();
//	private JFishProperties plugin = new JFishProperties();
	private Map<String, PluginProperties> plugin = Maps.newHashMap();
	/***
	 * 是否自动增加插件路径
	 */
	boolean appendPluginContextPath = true;
	PluginContextPathModes pluginContextPathModes = PluginContextPathModes.APPEND;
	/***
	 * 
	 */
	private JwtConfig jwt = new JwtConfig();
	/*private JsonConfig json = new JsonConfig();
	private Properties mediaTypes;*/
	
	//security=BootSecurityConfig
	
	private boolean profile;
	private Boolean logErrorDetail;
	List<String> notifyThrowables = Lists.newArrayList("com.mysql.jdbc.MysqlDataTruncation", "SQLException");

	private HostfilterConfig hostfilter = new HostfilterConfig();
	
	private String errorView = "error";
	
	/***
	 * default is web
	 * option: web, ms
	 */
//	private String autoConfig;
	private GraceKillConfig graceKill = new GraceKillConfig();
	
	private FtlConfig ftl = new FtlConfig();
	
	public void setAppendPluginContextPath(boolean appendPluginContextPath){
		if(appendPluginContextPath){
			pluginContextPathModes = PluginContextPathModes.APPEND;
		}else{
			pluginContextPathModes = PluginContextPathModes.NO_APPEND;
		}
	}
	
	public boolean isNotifyThrowable(Throwable t) {
		return this.notifyThrowables.contains(t.getClass().getName()) ||
				this.notifyThrowables.contains(t.getClass().getSimpleName());
	}

    public boolean isLogErrorDetail(){
    	if(logErrorDetail!=null){
    		return logErrorDetail;
    	}
    	return bootSpringConfig!=null && !bootSpringConfig.isProduct();
    }

	public void setLogErrorDetail(Boolean logErrorDetail) {
		this.logErrorDetail = logErrorDetail;
	}
	
	@Override
	public boolean isAlwaysLogErrorDetail() {
		return isLogErrorDetail();
	}

	@Override
	public Map<String, Integer> getExceptionsStatusMapping() {
		return getMvc().getExceptionsStatusMapping();
	}

	@Override
	public Map<String, ExceptionMapping> getExceptionsMapping() {
		return getMvc().getExceptionsMapping();
	}

	@Data
	public class FtlConfig {
		String templateDir;
	}
	
	@Data
	public class MessageSourceConfig {
		private int cacheSeconds = -1;//cache forever
	}
	
	@Data
	public class MvcConfig {
		Properties mediaTypes;
		JsonConfig json = new JsonConfig();
		List<ResourceHandlerConfig> resourceHandlers = new ArrayList<>();
		
		/***
jfish:
    #corsfilter: 
        #enabled: true
    mvc: 
        corsFilter: true
        cors: 
            - mapping: /**
            - allowCredentials: true
		 */
		boolean corsFilter;
		List<CorsConfig> cors = new ArrayList<>();
//		MvcAsyncProperties async = new MvcAsyncProperties();
		
		/*@Deprecated
		private AutoWrapResultConfig autoWrapResult = new AutoWrapResultConfig();*/

		private Map<String, Integer> exceptionsStatusMapping = Maps.newHashMap();
		private Map<String, ExceptionMapping> exceptionsMapping = Maps.newHashMap();
		
		/***
		 * spring mvc 某个版本（5？）之后，默认不再使用后缀匹配模式
		 * 即若controller配置的mapping是"/requestPath"，当请求"/requestPath.json"时，将会匹配不到对应的controller
		 * 这会影响旧版本的运行，这里提供配置激活后缀匹配模式。
		 * 配合mediaTypes（PathExtensionContentNegotiationStrategy）使用更佳, @see {@link org.onetwo.boot.core.web.BootMvcConfigurerAdapter#configureContentNegotiation}
		 * 
		 * spring 默认为false
		 * @see {@link org.onetwo.boot.core.web.BootMvcConfigurerAdapter#configurePathMatch}
		 * @see {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#useSuffixPatternMatch}
		 */
		private boolean useSuffixPatternMatch = true;
		/***
		 * 启用尾部斜杠匹配，例如，/user和/user/将被视为相同的URL。
		 * spring 默认为true
		 * @see {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#useTrailingSlashMatch}
		 */
		private boolean useTrailingSlashMatch = true;

		public MvcConfig() {
			this.mediaTypes = new Properties();
			this.mediaTypes.put("json", "application/json");
			this.mediaTypes.put("xml", "application/xml");
			this.mediaTypes.put("xls", "application/vnd.ms-excel");
			this.mediaTypes.put("jfxls", "application/jfxls");
		}

		@Data
		public class JsonConfig {
			Boolean prettyPrint;
			boolean alwaysWrapDataResult;
			
			public boolean isPrettyPrint(){
				 if(prettyPrint==null)
					 return bootSpringConfig==null || !bootSpringConfig.isProduct();
				 else
					 return prettyPrint;
			}
		}
		
		/*@Data
		public class AutoWrapResultConfig {
			boolean enable;
			List<String> packages;
		}*/
	}
	
	@Data
	static public class CorsConfig {
		String mapping;
		String[] allowedOrigins = new String[] {"*"}; //规范支持*
		String[] allowedMethods = new String[] {"*"};//{"POST", "PUT", "GET", "OPTIONS", "DELETE", "HEAD"}; // spring mvc 支持*
		String[] allowedHeaders = new String[] {"*"}; //{"Content-Type", "Accept", "x-requested-with"}; // // spring mvc 支持*
		String[] exposedHeaders = new String[] {}; // 不支持 *
		/***
		 * true为设置Access-Control-Allow-Credentials: true，允许跨域cookies
		 * 同时需要客户端设置 xhr.withCredentials = true
		 */
		boolean allowCredentials = false;
		String maxAge;
		
		public long getMaxAgeInMillis() {
			return LangOps.timeToMills(maxAge, 1800);
		}
	}
	
	@Data
	static public class ResourceHandlerConfig {
		String[] pathPatterns;
		String[] locations;
		Integer cacheInDays = 30;
	}
	
	@Data
	public class GraceKillConfig {
		Collection<String> signals;
	}

	@Data
	public class HostfilterConfig {
		List<String> allowIps = Lists.newArrayList();
		/***
		 * 不允许的host访问时，返回的状态码
		 * 默认返回403（Forbidden）
		 */
		Integer denyStatus = 403;
	}
	
}
