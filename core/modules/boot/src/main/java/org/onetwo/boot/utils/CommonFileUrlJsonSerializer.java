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
public class CommonFileUrlJsonSerializer extends UrlJsonSerializer {
	protected List<String> fileTypes = Arrays.asList(
											// 图片
											"jpg", "jpeg", "gif", "png", "bmp",
											// 视频
											"rmvb", "mp4", "avi", "mov",
											// 音频
											"mp3","aac","flac","m4a",
											// 一般电子文档
											"txt", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
											// cad
											"dwg"
										);
	
	public CommonFileUrlJsonSerializer() {
	}

	public String getServerFullPath(String subPath){
		if (StringUtils.isBlank(subPath)) {
			return subPath;
		}
		if(isHttpPath(subPath)){
			return subPath;
		}
		String ext = FileUtils.getExtendName(subPath).toLowerCase();
		if(!fileTypes.contains(ext)){//非配置文件类型，直接返回内容 
			return subPath;
		}
		
		PathTagResolver pathTagResolver = Springs.getInstance().getBean(PathTagResolver.class);
		String path = pathTagResolver.parsePathTag(subPath);
		return path;
	}

	public void setFileTypes(List<String> fileTypes) {
		this.fileTypes = fileTypes;
	}
	
}
