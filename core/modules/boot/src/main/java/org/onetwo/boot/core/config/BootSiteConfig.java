package org.onetwo.boot.core.config;

import java.util.stream.Stream;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import lombok.Data;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.filter.DefaultSiteConfig;
import org.onetwo.common.web.filter.SiteConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;


/***
 * 兼容以前siteConfig的配置
 * TODO: 可再包一层读取远程配置
 * 	  name: testName
 * 	  code: testCode
 * @author way
 *
 */
@ConfigurationProperties(prefix="siteConfig")
//@Data
public class BootSiteConfig extends DefaultSiteConfig implements SiteConfigProvider<BootSiteConfig> {
	/*public static final String BASEURL = "baseURL";
	public static final String APP_NAME = "name";
	public static final String APP_CODE = "code";*/

	/*public static final String PATH_JS = "path.js";
	public static final String PATH_RS = "path.resources";
	public static final String PATH_CSS = "path.css";
	public static final String PATH_IMAGE = "path.image";*/
	
	private String contextPath;
	private String contextRealPath;
	private String name;
	private String code;
	private String baseURL;
	
	//static resouce path config
	private String jsPath;
	private String cssPath;
	private String imagePath;
	
	//uploaded file access path
	private String uploadImageAccessPath;

	@Autowired
	private BootSpringConfig bootSpringConfig;
	
	@Getter
	private UploadConfig upload = new UploadConfig();
	

    @Override
    public BootSiteConfig createConfig(FilterConfig config) {
    	ServletContext servletContext = config.getServletContext();
    	this.contextPath = servletContext.getContextPath();
		this.contextRealPath = servletContext.getRealPath("");
		
		if (StringUtils.isBlank(baseURL))
			baseURL = contextPath;
		
	    return this;
    }
	
	public boolean isDev(){
		return bootSpringConfig.isDev();
	}
	
	public boolean isProduct(){
		return bootSpringConfig.isProduct();
	}
	
	public boolean isTest(){
		return bootSpringConfig.isTest();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/***
	 * dir /static/js/file.js
	 * url /js/file.js
	 * @return
	 */
	public String getJsPath(){
//		return getContextPath()+getProperty(PATH_JS, "/js");
		if(StringUtils.isNotBlank(jsPath)){
			return jsPath;
		}
		return getBaseURL() + "/js";
	}
	
	
	public String getCssPath(){
		if(StringUtils.isNotBlank(cssPath))
			return cssPath;
		return getBaseURL()+ "/css";
	}
	
	public String getImagePath(){
		if(StringUtils.isNotBlank(imagePath))
			return imagePath;
		return getBaseURL()+"/images";
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getContextRealPath() {
		return contextRealPath;
	}

	public String getBaseURL() {
		return baseURL;
	}
	

	public String getUploadImageAccessPath() {
		return uploadImageAccessPath;
	}

	public void setUploadImageAccessPath(String uploadImageAccessPath) {
		this.uploadImageAccessPath = uploadImageAccessPath;
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
