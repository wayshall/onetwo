package org.onetwo.boot.core.web.ftl;

import java.io.IOException;

import org.onetwo.common.spring.ftl.FtlUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class JFishFreeMarkerConfigurer extends FreeMarkerConfigurer {

	public static final BeansWrapper INSTANCE = FtlUtils.BEAN_WRAPPER;


	public JFishFreeMarkerConfigurer() {
		super();
	}

	protected void postProcessConfiguration(Configuration config) throws IOException, TemplateException {
		config.setObjectWrapper(INSTANCE);
		config.setSetting("classic_compatible", "true");
		//默认不格式化数字
		config.setNumberFormat("#");
	}

}
