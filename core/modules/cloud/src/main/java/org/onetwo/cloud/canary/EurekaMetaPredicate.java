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
 * @author wayshall
 * <br/>
 */
public class EurekaMetaPredicate extends AbstractServerPredicate {
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
		for(Entry<String, String> entry : metaData.entrySet()){
			if(!entry.getKey().startsWith(CANARY_FILTERS_KEY)){
				continue;
			}
			String matcherName = entry.getKey().substring(CANARY_FILTERS_KEY.length());
			String[] patterns = GuavaUtils.split(entry.getValue(), ",");
			Matcher<CanaryContext> matcher = matcherManager.createMatcher(matcherName, patterns);
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
