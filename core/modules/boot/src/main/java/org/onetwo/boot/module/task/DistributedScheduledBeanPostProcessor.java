package org.onetwo.boot.module.task;

import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

/**
 * TODO : 简单的分布式定时器
 * 自定义注解 @DistributedTask
 * 标注了这个注解的任务，延时启动定时器，实现postProcessAfterInitialization进行过滤
 * 
 * @author weishao zeng
 * <br/>
 */
public class DistributedScheduledBeanPostProcessor extends ScheduledAnnotationBeanPostProcessor {

}
