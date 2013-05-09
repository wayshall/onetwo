package org.onetwo.common.web.config;

public interface TemplatePathMapper {

	public abstract void clear();

	public abstract void reload();

	public abstract boolean isInited();

	public abstract String parse(String path, boolean isResource);

	public abstract String getParent(String templatePath);

	public abstract boolean isEnable();

}