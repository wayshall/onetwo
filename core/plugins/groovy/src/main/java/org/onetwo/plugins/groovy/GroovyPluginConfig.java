package org.onetwo.plugins.groovy;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractLoadingConfig;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.springframework.util.Assert;

public class GroovyPluginConfig extends AbstractLoadingConfig {

//	private JFishProperties wrapper;
	
	private List<String> groovySourcePackages;
	
	@Override
	protected void initConfig(JFishProperties config) {
		groovySourcePackages = config.getStringList("source.packages", ",");
		Assert.notEmpty(groovySourcePackages, "groovy-config[source.packages] can't not be empty!");
	}

	public List<String> getGroovySourcePackages() {
		return groovySourcePackages;
	}

	public void setGroovySourcePackages(List<String> groovySourcePackages) {
		this.groovySourcePackages = groovySourcePackages;
	}

}
