package org.onetwo.boot.core.web.utils;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.Optional;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;

public class PathTagResolver implements InitializingBean {
	private String start = "{";
	private String end = "}";

	
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
	
	public void checkPathTag(String pathTag) {
		if (!pathTag.startsWith(start) || !pathTag.endsWith(end)) {
			throw new BaseException("error pathTag: " + pathTag);
		}
	}
}
