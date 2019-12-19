package org.onetwo.cloud.env.rmq;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptorChain;
import org.onetwo.cloud.env.AuthEnvs;
import org.onetwo.cloud.env.AuthEnvs.AuthEnv;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.ons.ONSUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author weishao zeng
 * <br/>
 */
public class AuthEnvRmqMessageInterceptor implements SendMessageInterceptor {
	
	private final Logger logger = ONSUtils.getONSLogger();
	
	@Autowired
	private AuthEnvs authEnvs;
	@Autowired(required=false)
	private BootJFishConfig config;

	@Override
	public Object intercept(SendMessageInterceptorChain chain) {
		AuthEnv env = AuthEnvs.getCurrent();
		if (env!=null) {
			fillMessageAuthEnvs(env, chain);
			return chain.invoke();
		}
		env = authEnvs.createWebAuthEnv(false);
		if (env!=null) {
			fillMessageAuthEnvs(env, chain);
			return AuthEnvs.runInCurrent(env, () -> {
				return chain.invoke();
			});
		}
		return chain.invoke();
		/*AuthEnv env = AuthEnvs.getCurrent();
		if (env==null) {
			env = authEnvs.createWebAuthEnv(false);
		}
		Message message = (Message)chain.getSendMessageContext().getMessage();
		if(logger.isDebugEnabled()){
			logger.debug("message[{}] AuthEnvs header: {}", chain.getSendMessageContext().getKey(), env);
		}
		if (env!=null) {
			env.getHeaders().forEach(header -> {
				// 排除auth
				if (config!=null && header.getName().equals(config.getJwt().getAuthHeader())) {
					return ;
				}
				String value = header.getValue();
				if (StringUtils.isNotBlank(value)){
					message.putUserProperties(header.getName(), value);
				}
			});
			
			return AuthEnvs.runInCurrent(env, () -> {
				return chain.invoke();
			});
			return chain.invoke();
		} else {
			return chain.invoke();
		}*/
	}
	
	private void fillMessageAuthEnvs(AuthEnv env, SendMessageInterceptorChain chain) {
		if(logger.isInfoEnabled()){
			logger.info("send message[{}] with AuthEnvs header: {}", chain.getSendMessageContext().getKey(), env);
		}
		Message message = (Message)chain.getSendMessageContext().getMessage();
		env.getHeaders().forEach(header -> {
			// 排除auth
			if (config!=null && header.getName().equals(config.getJwt().getAuthHeader())) {
				return ;
			}
			String value = header.getValue();
			if (StringUtils.isNotBlank(value)){
				message.putUserProperties(header.getName(), value);
			}
		});
	}

}
