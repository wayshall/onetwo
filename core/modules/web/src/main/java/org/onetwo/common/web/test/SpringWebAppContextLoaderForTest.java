package org.onetwo.common.web.test;


import java.io.File;

import org.onetwo.common.spring.context.SpringProfilesWebApplicationContext;
import org.onetwo.common.test.spring.SpringProfilesWebApplicationContextLoader;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.config.WebProfilesApplicationContext;
import org.springframework.mock.web.MockServletContext;

public class SpringWebAppContextLoaderForTest extends SpringProfilesWebApplicationContextLoader {
	

	@Override
	protected Class<?>[] getClassArray() {
		return null;
	}

	@Override
	protected String getAppEnvironment() {
		return BaseSiteConfig.getInstance().getAppEnvironment();
	}

	public String getWebappDir(){
		String baseDirPath = FileUtils.getResourcePath("");
		File baseDir = new File(baseDirPath);
		baseDir = baseDir.getParentFile().getParentFile();
		String path = "file://" + baseDir.getPath() + "/src/main/webapp";
		return path;
	}

	protected SpringProfilesWebApplicationContext createContext(){
		MockServletContext servletContext = new MockServletContext(getWebappDir());
		WebProfilesApplicationContext context = new WebProfilesApplicationContext();
		context.setServletContext(servletContext);
		context.refresh();
		return context;
	}
	
}
