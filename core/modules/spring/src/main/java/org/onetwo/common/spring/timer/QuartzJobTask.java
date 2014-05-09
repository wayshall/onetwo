package org.onetwo.common.spring.timer;

public interface QuartzJobTask extends JobTask {
	

	public String getCronExpression();
}
