package org.onetwo.boot.module.qlexpress;
/**
 * @author weishao zeng
 * <br/>
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.config.QLExpressRunStrategy;

@ConditionalOnClass(ExpressRunner.class)
@ConditionalOnProperty(value=QLExpressProperties.ENABLE_KEY, matchIfMissing=true)
@Configuration
@EnableConfigurationProperties(QLExpressProperties.class)
public class QLExpressConfigtion {
	
	@Autowired
	QLExpressProperties expressProperties;
	
	@Bean
	public ExpressRunner expressRunner() {
		QLExpressRunStrategy.setForbiddenInvokeSecurityRiskMethods(true);
		ExpressRunner runner = new ExpressRunner(expressProperties.isPrecise(), expressProperties.isTrace());
		runner.setShortCircuit(expressProperties.isShortCircuit());
		return runner;
	}
	
	@Bean
	public ExpressExecutor ExpressExecutor(ExpressRunner expressRunner) {
		return new ExpressExecutor(expressRunner, expressProperties);
	}

}
