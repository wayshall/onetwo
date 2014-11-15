package apptimer;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;

public class TimerConfig extends AppConfig { 

	protected static final Logger logger = MyLoggerFactory.getLogger(TimerConfig.class);

	private static final TimerConfig instance = new TimerConfig();
	
	public static TimerConfig getInstance() {
		return instance;
	}

	protected TimerConfig() {
		super("siteConfig.properties");
	}
	
}
