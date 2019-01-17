package org.onetwo.boot.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.onetwo.boot.core.jwt.JwtConfig;
import org.onetwo.common.utils.LangOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

import lombok.Data;


/***
 * 某些专属jfish的配置
 * 一般用于增强配置framework
 * @author way
 *
 */
@ConfigurationProperties(prefix="jfish")
@Data
public class BootJFishConfig {
	public static final String ENABLE_GRACEKILL = "jfish.graceKill.enabled";
	public static final String ENABLE_SWAGGER = "jfish.swagger.enabled";
	
	public static final String ENABLE_CORSFILTER = "jfish.corsfilter.enabled";
	public static final String ENABLE_DYNAMIC_LOGGER_LEVEL = "jfish.dynamic.loggerLevel";
	public static final String ENABLE_DYNAMIC_SETTING = "jfish.dynamic.setting";
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
	private boolean logErrorDetail;
	List<String> notifyThrowables;

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

    public boolean isLogErrorDetail(){
    	if(logErrorDetail){
    		return true;
    	}
    	return !bootSpringConfig.isProduct();
    }

	public void setLogErrorDetail(boolean logErrorDetail) {
		this.logErrorDetail = logErrorDetail;
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
		List<CorsConfig> cors = new ArrayList<>();
//		MvcAsyncProperties async = new MvcAsyncProperties();
		
		/*@Deprecated
		private AutoWrapResultConfig autoWrapResult = new AutoWrapResultConfig();*/

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
					 return !bootSpringConfig.isProduct();
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
}
