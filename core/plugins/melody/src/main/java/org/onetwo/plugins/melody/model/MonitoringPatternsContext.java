package org.onetwo.plugins.melody.model;

import javax.annotation.Resource;

import net.bull.javamelody.MonitoringSpringAdvisor;

import org.onetwo.plugins.melody.MelodyConfig;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration
//@ComponentScan(basePackageClasses=PluginModelContext.class)
@ImportResource("classpath:net/bull/javamelody/monitoring-spring-aspectj.xml")
public class MonitoringPatternsContext {
	
	@Resource
	private MelodyConfig melodyConfig;
	
	@Bean
	public MonitoringSpringAdvisor facadeMonitoringAdvisor(){
		JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
		pointcut.setPatterns(melodyConfig.getMonitoringPatterns());
		MonitoringSpringAdvisor m = new MonitoringSpringAdvisor();
		m.setPointcut(pointcut);
		return m;
	}
	
}
