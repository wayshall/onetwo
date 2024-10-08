package org.onetwo.boot.core.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.filter.DefaultSiteConfig;
import org.onetwo.common.web.filter.SiteConfigProvider;
import org.onetwo.common.web.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


/***
 * 和web相关的配置，这个类的配置可在在页面通过siteConfig获取
 * 
 * 兼容以前siteConfig的配置
 * TODO: 可再包一层读取远程配置
 * 	  name: testName
 * 	  code: testCode
 * @author way
 *
 */
//@ConfigurationProperties(prefix="jfish.siteConfig")
@ConfigurationProperties(prefix=BootSiteConfig.PREFIX)
//@Data
public class BootSiteConfig extends DefaultSiteConfig implements SiteConfigProvider<BootSiteConfig> {
	public static final String PREFIX = "site";
	
	/*public static final String BASEURL = "baseURL";
	public static final String APP_NAME = "name";
	public static final String APP_CODE = "code";*/

	/*public static final String PATH_JS = "path.js";
	public static final String PATH_RS = "path.resources";
	public static final String PATH_CSS = "path.css";
	public static final String PATH_IMAGE = "path.image";*/

	public static final String ENABLE_UPLOAD_VIEW = "site.upload.view";
	public static final String ENABLE_STORETYPE_PROPERTY = "site.upload.store-type";
//	public static final String ENABLE_KINDEDITOR_UPLOADSERVICE = "site.kindeditor.uploadService";
	public static final String ENABLE_COMPRESS_PREFIX = "site.upload.compress-image.enable";
	public static final String ENABLE_UPLOAD_STOREFILEMETATODATABASE = "site.upload.store-file-meta-to-database";
	
	final private Logger logger = LoggerFactory.getLogger(this.getClass());

	final private static String WEBJARS_PATH = "/webjars";
	
	private String contextPath;
	private String contextRealPath;
	private String name;
	private String code;
	private String baseURL = "";
	
	//static resouce path config
	private String jsPath;
	private String cssPath;
	private String imagePath;
	
	private String staticURL;
	
//	List<String> notifyThrowables;
	
	private ImageServer imageServer = new ImageServer();
	
	//uploaded file access path

	@Autowired(required=false)
	private BootSpringConfig bootSpringConfig;
	
	@Getter
	private UploadConfig upload = new UploadConfig();

	@Getter
	EditorConfig kindeditor = new EditorConfig();
	
	

    @Override
    public BootSiteConfig initWebConfig(ServletContext servletContext) {
//    	ServletContext servletContext = config.getServletContext();
    	this.contextPath = servletContext.getContextPath();
		this.contextRealPath = servletContext.getRealPath("");
		
		logger.info("=====>>> contextPath: {}", contextPath);
		if (StringUtils.isBlank(baseURL))
			baseURL = contextPath;
		logger.info("=====>>> baseURL: {}", baseURL);
	    return this;
    }
    
    
    public String getWebjarsStaticPath(){
    	return getWebjarsPath()+"/static";
    }
    
    public String getWebjarsPath(){
    	return getBaseURL()+WEBJARS_PATH;
    }
    
    public String getWebjarsJsPath(){
    	return getWebjarsStaticPath()+"/js";
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
		/*if(UrlUtils.isAbsoluteUrl(baseURL)){
			return baseURL;
		}
		String prefixPath = BootWebUtils.getBasePathPrefixFromHeader();*/
		return baseURL;
	}
	
	public ImageServer getImageServer() {
		return imageServer;
	}

	public void setImageServer(ImageServer imageServer) {
		this.imageServer = imageServer;
	}

	/*public List<String> getNotifyThrowables() {
		return notifyThrowables;
	}

	public void setNotifyThrowables(List<String> notifyThrowables) {
		this.notifyThrowables = notifyThrowables;
	}*/

	public String getStaticURL() {
		if(StringUtils.isBlank(staticURL)){
			return getJsPath();
		}
		return staticURL;
	}

	public void setStaticURL(String staticURL) {
		this.staticURL = staticURL;
	}

	@Data
	public class ImageServer {
		String basePath;
		boolean useLoadBalance;
		int serverCount = 2;
		
		Map<String, String> pathTags = Maps.newHashMap();
		
		WaterMaskConfig watermask = new WaterMaskConfig();
		
		public String getBasePath(){
			if(StringUtils.isBlank(basePath)){
				return "";
			}
			return basePath;
		}
		public void setBasePath(String basePath){
			this.basePath = basePath;
		}
		
		public String getImageFullPath(String subPath){
			if (subPath==null || RequestUtils.isHttpPath(subPath)) {
				return subPath;
			}
			if(!basePath.endsWith("/") && !subPath.startsWith("/")){
				basePath += "/";
			}
			return basePath + subPath;
		}
	}
	
	@Data
	public static class WaterMaskConfig {
		boolean enabled;
		/***
		 * 用urlTemplate重新解释url
		 */
		String urlTemplate;
		List<String> applyPostfixList = Arrays.asList("jpg", "jpeg", "gif", "png", "bmp");
	}

	//move to jfishConfig?
	@Data
	static public class UploadConfig {
		/***
		 * 如果设置了值，则会把上传目录映射到对应的访问路径，如：/upload/**
		 */
		String accessPathPatterns;
		StoreType storeType = StoreType.LOCAL;
		/***
		 * 如果是local存储，可以写绝对路径，如：/data/upload/cms；
		 * 如果是云存储，可以看做是系统模块名称，如：cms
		 */
		String fileStorePath;
//		boolean fileStorePathToResourceHandler = true;
		Integer resourceCacheInDays = 30;
//		String appContextDir;
		//multipartProperties
//		int maxUploadSize = BootStandardServletMultipartResolver.DEFAULT_MAX_UPLOAD_SIZE;
		boolean storeFileMetaToDatabase;
		
		//ftp
		String ftpEncoding = LangUtils.UTF8;
		String ftpServer;
		int ftpPort = 21;
		String ftpUser;
		String ftpPassword;
//		String ftpBaseDir;
		
		CompressConfig compressImage = new CompressConfig();
		
		/****
		 * 单个文件最大上传
		 */
		String maxFileUploadSize;
		
		CleanupProps cleanup = new CleanupProps();
		
		/***
		 * -1为没有配置
		 * @author weishao zeng
		 * @return
		 */
		public int getMaxFileUploadSizeInBytes() {
			if (StringUtils.isBlank(maxFileUploadSize)) {
				return -1;
			}
			return FileUtils.parseSize(maxFileUploadSize);
		}

		public Integer getResourceCacheInDays() {
			return resourceCacheInDays;
		}
		
		/*public String getAppContextDir() {
			return appContextDir;
		}*/

		/*public int getMaxUploadSize(){
			return maxUploadSize;
		}*/
		
	}
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	static public class CompressConfig {
		//超过了配置值就启用自动压缩功能，比如：5KB
		//少于0则一律不压缩
		String thresholdSize;
		/***
		 * 缩放，0到无穷大（不包含)
		 */
		Double scale;
		/***
		 * 压缩质量0到1
		 */
		Double quality;
		Integer width;
		Integer height;
		@Builder.Default
		List<String> fileTypes = Arrays.asList("jpg", "jpeg", "gif", "png", "bmp");
	}
	
	@Data
	public class EditorConfig {
		//kindeditor上传图片时返回路径的前缀
		String imageBasePath;
		
		public String getImageBasePath(){
			if(StringUtils.isBlank(imageBasePath)){
				return imageServer.getBasePath();
			}
			return imageBasePath;
		}
	}
	
	public static enum StoreType {
		LOCAL,
		FTP,
		SFTP,
		ALIOSS,
		COS;
		
		public static StoreType of(String str){
			if(StringUtils.isBlank(str)){
				return LOCAL;
			}
			return Stream.of(values()).filter(t->t.name().equalsIgnoreCase(str))
								.findFirst().orElse(LOCAL);
		}
	}
	
	@Data
	public static class CleanupProps {
		public static final String ENABLED_KEY = PREFIX + ".upload.cleanup.enabled";
		/****
		 * 每天零点零5分执行
		 */
		public static final String CRON = "${" + PREFIX + ".upload.cleanup.cron:0 5 0 * * *}";
		/****
		 * 清理N天之前的文件
		 * 少于0为不清理
		 */
		int expiredInDays = -1;
		
		List<String> subdirs;
	}
}
