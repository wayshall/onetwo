package org.onetwo.ext.security.base;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.ext.security.exception.AccessDeniedCodeException;
import org.onetwo.ext.security.exception.SecurityErrors;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;

/****
 * 任意一个投票者（voter）赞成即可
 * @author way
 *
 */
public class ExtAffirmativeBased extends AffirmativeBased {
	
	private final Logger logger = JFishLoggerFactory.getLogger(getClass());

	public ExtAffirmativeBased(List<AccessDecisionVoter<?>> decisionVoters) {
		super(decisionVoters);
	}
	
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
		try {
			super.decide(authentication, object, configAttributes);
		} catch (IllegalArgumentException e) {
			logger.error("error security expression: " + e.getMessage(), e);
			// 统一被当作未登录处理
			throw new AccessDeniedCodeException(SecurityErrors.CM_NOT_LOGIN);
		}
	}

}
