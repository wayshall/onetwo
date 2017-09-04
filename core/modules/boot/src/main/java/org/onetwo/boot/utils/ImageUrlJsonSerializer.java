package org.onetwo.boot.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.ImageServer;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.jackson.UrlJsonSerializer;
import org.onetwo.common.spring.Springs;

/**
 * @author wayshall
 * <br/>
 */
public class ImageUrlJsonSerializer extends UrlJsonSerializer {
	protected List<String> fileTypes = Arrays.asList("jpg", "jpeg", "gif", "png", "bmp");

	protected String getServerFullPath(String subPath){
		if(isHttpPath(subPath)){
			return subPath;
		}
		String ext = FileUtils.getExtendName(subPath).toLowerCase();
		if(!fileTypes.contains(ext)){//非图片类型，直接返回内容 
			return subPath;
		}
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

	public void setFileTypes(List<String> fileTypes) {
		this.fileTypes = fileTypes;
	}
	
}
