package org.onetwo.common.jfishdbm;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;

public class JFishOrmTestConfig extends AppConfig { 

	protected static final Logger logger = JFishLoggerFactory.getLogger(JFishOrmTestConfig.class);

	private static final JFishOrmTestConfig instance = new JFishOrmTestConfig();
	
	public static JFishOrmTestConfig getInstance() {
		return instance;
	}

	protected JFishOrmTestConfig() {
		super("siteConfig.properties");
	}
	
}
