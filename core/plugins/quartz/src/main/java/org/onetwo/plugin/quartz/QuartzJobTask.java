package org.onetwo.plugin.quartz;

import org.onetwo.common.spring.timer.JobTask;

public interface QuartzJobTask extends JobTask {

	public String getCronExpression();
	
	public boolean isConcurrent();
	
}
