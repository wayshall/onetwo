package org.onetwo.boot.core.shutdown;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring boot 2.3后内置实现了优雅关机，
 * 会等待30（spring.lifecycle.timeout-per-shutdown-phase）秒，才会关闭，参考：LifecycleProperties
 * 因此本配置修改为，当spring boot 配置了 server.shutdown=IMMEDIATE时，才启用
 * 
 * @author wayshall
 * <br/>
 */
@Configuration
//@ConditionalOnProperty(value=BootJFishConfig.ENABLE_GRACEKILL, matchIfMissing=true, havingValue="true")
@ConditionalOnProperty(value="server.shutdown", matchIfMissing=false, havingValue="immediate")
public class GraceKillConfiguration {
	
	@Bean
	public GraceKillSignalHandler graceKillSignalHandler(){
		return new GraceKillSignalHandler();
	}
	
	@Bean
	@ConditionalOnMissingBean(BootGraceKillProcessor.class)
	public BootGraceKillProcessor bootGraceKillProcessor(){
		return new BootGraceKillProcessor();
	}

}
