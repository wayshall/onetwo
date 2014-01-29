package org.onetwo.project.batch;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;

public class BatchConfig extends AppConfig { 

	protected static final Logger logger = MyLoggerFactory.getLogger(BatchConfig.class);

	private static final BatchConfig instance = new BatchConfig();
	
	public static BatchConfig getInstance() {
		return instance;
	}

	protected BatchConfig() {
		super("siteConfig.properties");
	}
	
}
