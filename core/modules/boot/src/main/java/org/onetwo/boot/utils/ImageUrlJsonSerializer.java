package org.onetwo.boot.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.web.utils.PathTagResolver;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.jackson.serializer.UrlJsonSerializer;
import org.onetwo.common.spring.Springs;

/**
 * @author wayshall
 * <br/>
 */
public class ImageUrlJsonSerializer extends UrlJsonSerializer {
	protected List<String> fileTypes = Arrays.asList("jpg", "jpeg", "gif", "png", "bmp");

	protected String getServerFullPath(String subPath){
		if (StringUtils.isBlank(subPath)) {
			return subPath;
		}
		if(isHttpPath(subPath)){
			return subPath;
		}
		String ext = FileUtils.getExtendName(subPath).toLowerCase();
		if(!fileTypes.contains("*") && !fileTypes.contains(ext)){//非图片类型，直接返回内容 
			return subPath;
		}

		
//		BootSiteConfig config = Springs.getInstance().getBean(BootSiteConfig.class);
//		if(config==null || config.getImageServer()==null){
//			return subPath;
//		}

//		ImageServer server = config.getImageServer();
		
		PathTagResolver pathTagResolver = Springs.getInstance().getBean(PathTagResolver.class);
//		Optional<String> pathTag = pathTagResolver.findPathTag(subPath, false);
//		String path = null;
//		if (pathTag.isPresent()) {
//			if (!server.getPathTags().containsKey(pathTag.get())) {
//				throw new BaseException("pathTag baseUrl not found: " + pathTag.get());
//			}
//			path = server.getPathTags().get(pathTag.get());
//			subPath = pathTagResolver.trimPathTag(subPath);
//		} else {
//			if(StringUtils.isBlank(server.getBasePath())){
//				return subPath;
//			}
//			path = server.getBasePath();
//		}
//		
//		if(server.isUseLoadBalance()){
//			path = path.replace("[serverIndex]", getServerIndex(subPath, server.getServerCount())+"");
//		}
//		path = fixPath(path, subPath);
		String path = pathTagResolver.parsePathTag(subPath);
		return path;
	}

	public void setFileTypes(List<String> fileTypes) {
		this.fileTypes = fileTypes;
	}
	
}
