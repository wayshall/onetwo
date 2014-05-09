package org.onetwo.plugin.quartz;

abstract public class AbstractQuartzJobTask implements QuartzJobTask {

	@Override
	public boolean isConcurrent() {
		return true;
	}

}
