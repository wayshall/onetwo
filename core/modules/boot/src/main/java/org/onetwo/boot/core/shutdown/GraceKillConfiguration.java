package org.onetwo.boot.core.shutdown;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnProperty(value=BootJFishConfig.ENABLE_GRACEKILL, matchIfMissing=true, havingValue="true")
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
