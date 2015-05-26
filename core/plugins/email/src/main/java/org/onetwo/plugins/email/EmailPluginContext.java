package org.onetwo.plugins.email;

import javax.annotation.Resource;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.propconf.AppConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailPluginContext implements InitializingBean {

	public static final String FTL_DIR = "classpath:/plugins/email/ftl/";
	
//	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	@Resource
	private AppConfig appConfig;
	
//	@Resource
//	private Properties mailConfig;
	private EmailConfig emailConfig = EmailPlugin.getInstance().getConfig();
	
//	@Resource
//	private freemarker.template.Configuration mailFreemarkerConfiguration;
	
//	private StringFtlTemplateLoader stringFtlTemplateLoader = new StringFtlTemplateLoader();

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(appConfig, "appConfig must not be null!");
	}

	@Bean
	public EmailConfig emailConfig() {
		/*String envLocation = "/email/mailconfig-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring("/email/mailconfig.properties", envLocation);*/
		return emailConfig;
	}
	
	/*@Bean
	public FreeMarkerConfigurationFactoryBean mailFreemarkerConfiguration(){
		FreeMarkerConfigurationFactoryBean fcfb = new FreeMarkerConfigurationFactoryBean();
		fcfb.setTemplateLoaderPath(FTL_DIR);
		fcfb.setPreTemplateLoaders(stringFtlTemplateLoader);
		return fcfb;
	}*/
	
	/*@Bean
	public StringFtlTemplateLoader stringFtlTemplateLoader(){
		return this.stringFtlTemplateLoader;
	}*/
	
/*	@Bean
	public MailTextContextParser mailTextContextParser(){
		MailTextContextParser parser = new MailTextContextParser();
		return parser;
	}*/

}
