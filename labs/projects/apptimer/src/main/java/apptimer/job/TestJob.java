package apptimer.job;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.timer.QuartzJobTask;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TestJob implements QuartzJobTask {

	private Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	public void execute() {
		logger.info("execte test...");
	}

	public String getCronExpression() {
		return "0/1 * * * * ?";
	}

}
