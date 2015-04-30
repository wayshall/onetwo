package org.onetwo.test.jorm;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;

public class JFishOrmConfig extends AppConfig { 

	protected static final Logger logger = JFishLoggerFactory.getLogger(JFishOrmConfig.class);

	private static final JFishOrmConfig instance = new JFishOrmConfig();
	
	public static JFishOrmConfig getInstance() {
		return instance;
	}

	protected JFishOrmConfig() {
		super("siteConfig.properties");
	}
	
}
