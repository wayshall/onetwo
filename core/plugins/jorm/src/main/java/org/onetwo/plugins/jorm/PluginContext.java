package org.onetwo.plugins.jorm;

import org.onetwo.common.fish.spring.config.JFishAppConfigrator;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=JormPlugin.class)
public class PluginContext  implements ApplicationContextAware{

	private ApplicationContext applicationContex;

	@Value("${jfish.base.packages}")
	private String jfishBasePackges;
	
	@Value("${watch.sql.file}")
	private boolean isWatchSqlFile;
	
	@Bean
	public JFishAppConfigrator jfishAppConfigurator() {
		JFishAppConfigrator jfAppConfigurator = SpringUtils.getBean(applicationContex, JFishAppConfigrator.class);
		if (jfAppConfigurator == null) {
			jfAppConfigurator = BaseSiteConfig.getInstance().getWebAppConfigurator(JFishAppConfigrator.class);
		}
		if(jfAppConfigurator==null){
			jfAppConfigurator = new BaseAppConfigurator() {
				
				@Override
				public String getJFishBasePackage() {
					return jfishBasePackges;
				}
			};
		}
		return jfAppConfigurator;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContex = applicationContext;
	}
	
}
