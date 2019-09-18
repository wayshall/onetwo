package org.onetwo.cloud.canary;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.limiter.Matcher;
import org.onetwo.cloud.canary.CanaryContext.DefaultCanaryContext;
import org.onetwo.common.utils.GuavaUtils;
import org.onetwo.common.web.utils.RequestUtils;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

/**
eureka:
    instance:
        metadata-map:
            canary.filters.client-tag: v-2.1.51
            canary.filters.antpath: /api/user/**
            canary.filters.regexpath: ^/api/user/.+$
            canary.filters.ip: 192.168.1.11
 * 
 * @author wayshall
 * <br/>
 */
public class CanaryFilterMetaPredicate extends AbstractServerPredicate {
	public static final String CANARY_FILTERS_KEY = "canary.filters.";
	
	private CanaryMatcherRegister matcherManager = CanaryMatcherRegister.INSTANCE;

	@Override
	public boolean apply(PredicateKey input) {
		if(!DiscoveryEnabledServer.class.isInstance(input.getServer())){
			return false;
		}
		DiscoveryEnabledServer server = (DiscoveryEnabledServer) input.getServer();
		Map<String, String> metaData = server.getInstanceInfo().getMetadata();
		return matchMetaData(metaData);
	}
	
	protected boolean matchMetaData(Map<String, String> metaData){
		CanaryContext context = createCanaryContext();
		boolean match = false;
		for(Entry<String, String> ruleEntry : metaData.entrySet()){
			if(!ruleEntry.getKey().startsWith(CANARY_FILTERS_KEY)){
				continue;
			}
			// matcherName: client-tag
			String matcherName = ruleEntry.getKey().substring(CANARY_FILTERS_KEY.length());
			// patterns: v-2.1.51
			String[] patterns = GuavaUtils.split(ruleEntry.getValue(), ",");
			// matcher = new ContainAnyOneMatcher("clientTag", {v-2.1.51})
			Matcher<CanaryContext> matcher = matcherManager.createMatcher(matcherName, patterns);
			// patterns.contains(context[matcherName]) -> canary.filters.client-tag.contains(context.clientTag)
			if(!matcher.matches(context)){
				return false;
			}else{
				match = true;
			}
		}
		return match;
	}
	
	private CanaryContext createCanaryContext(){
		DefaultCanaryContext ctx = new DefaultCanaryContext();
        final HttpServletRequest request = CanaryUtils.getHttpServletRequest();

		String requestPath = RequestUtils.getUrlPathHelper().getLookupPathForRequest(request);
		String clientIp = RequestUtils.getRemoteAddr(request);
		String cliengTag = request.getHeader(CanaryUtils.HEADER_CLIENT_TAG);
		ctx.setClientTag(cliengTag);
		ctx.setRequestPath(requestPath);
		ctx.setClientIp(clientIp);
		
		CanaryUtils.storeCanaryContext(ctx);
		
		return ctx;
	}
	
	

}
