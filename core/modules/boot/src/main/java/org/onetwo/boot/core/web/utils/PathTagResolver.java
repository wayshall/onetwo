package org.onetwo.boot.core.web.utils;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.Optional;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.ImageServer;
import org.onetwo.boot.core.config.BootSiteConfig.WaterMaskConfig;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class PathTagResolver implements InitializingBean {
	private String start = "{";
	private String end = "}";

	@Autowired
	private BootSiteConfig siteConfig;
	
	public PathTagResolver() {
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
	}
	
	public String trimPathTag(String path) {
		Optional<String> tag = findPathTag(path, true);
		if (tag.isPresent()) {
			return path.replace(tag.get(), "");
		} else {
			return path;
		}
	}

	/***
	 * {sftp}/aa/bb.jpg -> sftp
	 * @author weishao zeng
	 * @param path
	 * @return
	 */
	public Optional<String> findPathTag(String path, boolean withTag) {
		int minLength = end.length() + start.length() +1;
		if (StringUtils.isBlank(path) || path.length()<minLength) {
			return Optional.empty();
		}
		int startIndex = path.indexOf(start);
		if (startIndex==-1) {
			return Optional.empty();
		}
		int endIndex = path.indexOf(end, startIndex);
		if (endIndex==-1) {
			return Optional.empty();
		}
		if (withTag) {
			return Optional.of(path.substring(startIndex, endIndex+1));
		} else {
			return Optional.of(path.substring(startIndex+1, endIndex));
		}
	}
	
	/***
	 * 解释特殊的路径标记，如：{sftp}/aa/bb/cc.jpg -> /sftp_real_path/aa/bb/cc.jpg
	 * @author weishao zeng
	 * @param subPath
	 * @return
	 */
	public String parsePathTag(String subPath) {
		if(siteConfig==null || siteConfig.getImageServer()==null){
			return subPath;
		}
		ImageServer server = siteConfig.getImageServer();
		Optional<String> pathTagOpt = findPathTag(subPath, false);
		String path = null;
		if (!pathTagOpt.isPresent()) {
			if(StringUtils.isBlank(server.getBasePath())){
				return subPath;
			}
			path = server.getBasePath();
		} else {
			String pathTag = pathTagOpt.get();
			if (!server.getPathTags().containsKey(pathTag)) {
				throw new BaseException("pathTag baseUrl not found: " + pathTag);
			}
			path = server.getPathTags().get(pathTag);
			subPath = trimPathTag(subPath);
		}
		path = fixPath(path, subPath);
		
		path = parseUrlTemplate(server.getWatermask(), path);
		
		return path;
	}
	
	private String parseUrlTemplate(WaterMaskConfig watermask, String url) {
		if (!watermask.isEnabled()) {
			return url;
		}
		String postfix = FileUtils.getExtendName(url);
		if (!watermask.getApplyPostfixList().contains(postfix)) {
			return url;
		}
		String urlTemplate = watermask.getUrlTemplate();
		if (StringUtils.isBlank(urlTemplate)) {
			return url;
		}
		String newUrl = ExpressionFacotry.BRACE.parse(urlTemplate, "url", url);
		return newUrl;
	}

	static public String fixPath(String basePath, String subPath){
		if (StringUtils.isBlank(basePath)) {
			JFishLoggerFactory.getCommonLogger().warn("basepath is blank!");
			return subPath;
		}
		if(!basePath.endsWith("/") && !subPath.startsWith("/")){
			basePath += "/";
		}
		return basePath + subPath;
	}
	
	public void checkPathTag(String pathTag) {
		if (!pathTag.startsWith(start) || !pathTag.endsWith(end)) {
			throw new BaseException("error pathTag: " + pathTag);
		}
	}
}
