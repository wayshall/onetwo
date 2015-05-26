package org.onetwo.plugins.ftl;

import org.onetwo.common.spring.plugin.AbstractLoadingConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

public class FtlPluginConfig extends AbstractLoadingConfig {

	private String templateDir;
	@Override
	protected void initConfig(JFishProperties config) {
		this.templateDir = config.getDir("template.dir", "/template/");
	}
	public String getTemplateDir() {
		return templateDir;
	}

}
