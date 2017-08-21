package org.onetwo.boot.utils;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.ImageServer;
import org.onetwo.common.jackson.UrlJsonSerializer;
import org.onetwo.common.spring.Springs;

/**
 * @author wayshall
 * <br/>
 */
public class ImageUrlJsonSerializer extends UrlJsonSerializer {

	protected String getServerFullPath(String subPath){
		BootSiteConfig config = Springs.getInstance().getBean(BootSiteConfig.class);
		if(config==null || config.getImageServer()==null){
			return subPath;
		}
		ImageServer server = config.getImageServer();
		if(StringUtils.isBlank(server.getBasePath())){
			return subPath;
		}
		String path = server.getBasePath();
		if(server.isUseLoadBalance()){
			path = path.replace("{}", getServerIndex(subPath, server.getServerCount())+"");
		}
		path = fixPath(path, subPath);
		return path;
	}
	
}
