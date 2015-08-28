package org.onetwo.boot.core.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.utils.BootUtils;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.propconf.AppConfig;
import org.onetwo.common.propconf.Env;
import org.onetwo.common.propconf.JFishProperties;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.filter.AppConfigProvider;
import org.springframework.core.env.PropertySource;

import com.google.common.collect.ImmutableList;

/*@Component
@ConfigurationProperties(prefix="jfish")*/
/***
 * 过度配置 siteConfig
 * @author way
 *
 */
@SuppressWarnings("unchecked")
public class BootSiteConfig extends AppConfig implements AppConfigProvider {

	private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
	private static final String CONFIG_NAME = "siteConfig";
	private static final String CONFIG_POSFIX = ".properties";
	

	public static final String BASEURL = "baseURL";

	public static final String PATH_JS = "path.js";
	public static final String PATH_RS = "path.resources";
	public static final String PATH_CSS = "path.css";
	public static final String PATH_IMAGE = "path.image";
	
	
	private static final BootSiteConfig instance;
	static {
		PropertySource<?> props = BootUtils.loadYaml("application.yaml");
		Object env = props.getProperty(SPRING_PROFILES_ACTIVE);
		
		String[] strs = StringUtils.split(env.toString(), ",");
		instance = new BootSiteConfig(loadApplications(strs), ImmutableList.copyOf(strs));
		UtilTimerStack.active(instance.isDev());
		instance.printConfigs();
	}
	
	final private static JFishProperties loadApplications(String[] envstrs){
		List<String> envs = Stream.of(envstrs)
				.map(envStr->LangUtils.append(CONFIG_NAME, "-", envStr, CONFIG_POSFIX))
				.collect(Collectors.toList());
		String baseConfig = CONFIG_NAME+CONFIG_POSFIX;
		
		List<String> allEnvs = new ArrayList<>();
		allEnvs.add(baseConfig);
		allEnvs.addAll(envs);
		
		JFishProperties config = SpringUtils.loadAsJFishProperties(allEnvs.toArray(new String[0]));
		return config;
	}

	public static BootSiteConfig getInstance() {
		return instance;
	}

	private List<String> profilesActive;

	private String contextPath;
	private String contextRealPath;
	
	private BootSiteConfig(JFishProperties config, List<String> envs) {
	    super(config);
	    this.profilesActive = Optional.ofNullable(profilesActive)
									.orElse(Collections.EMPTY_LIST);
    }
	
	

    @Override
    public AppConfig createAppConfig(FilterConfig config) {
    	ServletContext servletContext = config.getServletContext();
    	this.contextPath = servletContext.getContextPath();
		this.contextRealPath = servletContext.getRealPath("");
		
	    return this;
    }

	public boolean isEnv(Env env){
		return profilesActive.contains(env.name());
	}
	
	public boolean isProduct(){
		return isEnv(Env.PRODUCT);
	}
	
	public String getBaseURL() {
		String baseURL = getVariable(BASEURL);
		if (StringUtils.isBlank(baseURL))
			baseURL = contextPath;
		return baseURL;
	}

	public List<String> getProfilesActive() {
		return profilesActive;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getContextRealPath() {
		return contextRealPath;
	}

	/***
	 * dir /static/js/file.js
	 * url /js/file.js
	 * @return
	 */
	public String getJsPath(){
//		return getContextPath()+getProperty(PATH_JS, "/js");
		String jspath = getProperty(PATH_JS);
		if(StringUtils.isNotBlank(jspath))
			return getContextPath()+jspath;
		return getBaseURL() + "/js";
	}
	
	public String getRsPath(){
		String csspath = getProperty(PATH_RS);
		if(StringUtils.isNotBlank(csspath))
			return getContextPath()+csspath;
		return getBaseURL()+ "/resources";
	}
	
	public String getCssPath(){
		String csspath = getProperty(PATH_CSS);
		if(StringUtils.isNotBlank(csspath))
			return getContextPath()+csspath;
		return getBaseURL()+ "/css";
	}
	
	public String getImagePath(){
		String imgpath = getProperty(PATH_IMAGE);
		if(StringUtils.isNotBlank(imgpath))
			return getContextPath()+imgpath;
		return getBaseURL()+"/images";
	}

//	@Setter @Getter
//	private String templateDir = "/ftl";
	
}
