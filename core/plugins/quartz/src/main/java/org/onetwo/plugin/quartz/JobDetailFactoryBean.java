package org.onetwo.plugin.quartz;

import java.util.concurrent.atomic.AtomicInteger;

import org.onetwo.common.spring.SpringUtils;
import org.quartz.JobDetail;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.util.Assert;

public class JobDetailFactoryBean extends MethodInvokingJobDetailFactoryBean implements ApplicationContextAware {

	private static AtomicInteger counter = new AtomicInteger(0);
	private ApplicationContext applicationContext;
	private QuartzJobTask quartzJobTask;

	protected void postProcessJobDetail(JobDetail jobDetail) {
		SpringUtils.registerBean(applicationContext, "cronTriggerFactoryBean#"+counter.getAndIncrement(), CronTriggerFactoryBean.class, "jobDetail", jobDetail, "cronExpression", quartzJobTask.getCronExpression());
	}

	@Override
	public String getTargetMethod() {
		return "execute";
	}


	@Override
	public QuartzJobTask getTargetObject() {
		return quartzJobTask;
	}


	@Override
	public void setTargetObject(Object targetObject) {
		Assert.isInstanceOf(QuartzJobTask.class, targetObject);
		this.quartzJobTask = (QuartzJobTask) targetObject;
		super.setTargetObject(targetObject);
		super.setConcurrent(quartzJobTask.isConcurrent());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
