package org.onetwo.cloud.env.rmq;

import java.util.Optional;

import org.onetwo.boot.module.oauth2.clientdetails.ClientDetails;
import org.onetwo.boot.module.oauth2.util.OAuth2Utils;
import org.onetwo.cloud.env.AuthEnvs;
import org.onetwo.cloud.env.AuthEnvs.AuthEnv;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.alimq.ConsumerListener;
import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.consumer.ConsumerMeta;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author weishao zeng
 * <br/>
 */
public class AuthEnvRmqConsumerListener implements ConsumerListener {

	private final Logger logger = ONSUtils.getONSLogger();
	
	@Autowired
	private AuthEnvs authEnvs;

	@Override
	public void beforeConsumeMessage(ConsumerMeta consumerMeta, ConsumContext context) {
		AuthEnv env = authEnvs.create(header -> {
			return context.getMessage().getUserProperty(header);
		});

		if(logger.isInfoEnabled()){
			logger.info("consume group[{}] message[{}] AuthEnvs header: {}", consumerMeta.getConsumerId(), context.getMessage().getKeys(), env);
		}
		AuthEnvs.setCurrent(env);
	}

	@Override
	public void afterConsumeMessage(ConsumerMeta consumerMeta, ConsumContext context) {
		AuthEnvs.removeCurrent();
		
	}

	@Override
	public void onConsumeMessageError(ConsumContext context, Throwable e) {
		AuthEnvs.removeCurrent();
	}

}
