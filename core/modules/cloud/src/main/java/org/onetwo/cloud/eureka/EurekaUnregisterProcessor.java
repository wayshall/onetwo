package org.onetwo.cloud.eureka;

import java.util.Map;

import org.onetwo.boot.core.shutdown.GraceKillSignalHandler.GraceKillProcessor;
import org.onetwo.boot.core.shutdown.GraceKillSignalHandler.SignalInfo;
import org.onetwo.boot.webmgr.WebManagementCommand;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.netflix.discovery.EurekaClient;

/**
 * @author weishao zeng
 * <br/>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EurekaUnregisterProcessor implements GraceKillProcessor, WebManagementCommand {

	private final Logger logger = JFishLoggerFactory.getCommonLogger();
	
	@Autowired
	private EurekaClient eurekaClient;
	@Value("${eureka.client.gracekill.waitSecondsAfterUnregister:3}")
	private int waitSecondsAfterUnregister;
	
	@Override
	public void handle(SignalInfo singal) {
		if (logger.isInfoEnabled()) {
			logger.info("eureka client will be unregistered from eureka and wait {} seconds...", waitSecondsAfterUnregister);
		}
		eurekaClient.shutdown();
		if (waitSecondsAfterUnregister>0) {
			// 后面可能还跟随着关闭整个应用的GraceKillProcessor，这里可以暂停一下，尽量让当前服务处理完请求
			LangUtils.await(waitSecondsAfterUnregister);
		}
	}

	@Override
	public String getName() {
		return "eurekaUnregister";
	}

	@Override
	public Object invoke(Map<String, Object> data) {
		eurekaClient.shutdown();
		return "SUCCESS";
	}
	
}

