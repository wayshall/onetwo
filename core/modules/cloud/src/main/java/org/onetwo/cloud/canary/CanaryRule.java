package org.onetwo.cloud.canary;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.cloud.canary.CanaryUtils.CanaryErrors;
import org.onetwo.cloud.canary.CanaryUtils.CanaryMode;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.web.utils.WebHolder;

import com.google.common.base.Optional;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

/**
 * @author wayshall
 * <br/>
 */
public class CanaryRule extends ZoneAvoidanceRule {

    private CompositePredicate canaryPredicate;
//	private CanaryPredicate canaryPredicate;
    private NoCanaryFilterMetaPredicate noCanaryFilterMetaPredicate;
    
    public CanaryRule(){
    	CanaryFilterMetaPredicate metaPredicate = new CanaryFilterMetaPredicate();
    	this.canaryPredicate = CompositePredicate.withPredicates(metaPredicate)
								                /*.addFallbackPredicate(this.getPredicate())
								                .addFallbackPredicate(AbstractServerPredicate.alwaysTrue())*/
								                .build();
    	this.noCanaryFilterMetaPredicate = new NoCanaryFilterMetaPredicate();
    }
	
    @Override
    public Server choose(Object key) {
    	java.util.Optional<HttpServletRequest> requestOpt = WebHolder.getRequest();
    	CanaryMode canaryMode = CanaryMode.CANARY_NONE;
    	if(requestOpt.isPresent()){
    		HttpServletRequest req = requestOpt.get();
    		canaryMode = CanaryMode.of(req.getHeader(CanaryUtils.HEADER_CANARY_ENABLED));
    	}
    	if(canaryMode==CanaryMode.FORCE){
    		ILoadBalancer lb = getLoadBalancer();
    		Optional<Server> server = canaryPredicate.chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
    		if(server.isPresent()) {
    			return server.get();
	        }
    		String errorContext = CanaryUtils.getCurrentCanaryContext().map(ctx->ctx.toString()).orElse("");
    		JFishLoggerFactory.getCommonLogger().error("errorContext: {}", errorContext);
    		throw new BaseException(CanaryErrors.CANARY_SERVER_NOT_MATCH);
    		
    	}else if(canaryMode==CanaryMode.SMOOTHNESS){
    		ILoadBalancer lb = getLoadBalancer();
            Optional<Server> server = canaryPredicate.chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
            if (server.isPresent()) {
                return server.get();
            }
    	}else if(canaryMode==CanaryMode.CANARY_NONE){
    		ILoadBalancer lb = getLoadBalancer();
            Optional<Server> server = noCanaryFilterMetaPredicate.chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
            if (server.isPresent()) {
                return server.get();
            }
    		throw new BaseException(CanaryErrors.CANARY_NONE_SERVER_NOT_MATCH);
    	}
        return super.choose(key);
    	
        /*ILoadBalancer lb = getLoadBalancer();
        Optional<Server> server = canaryPredicate.chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
        if (server.isPresent()) {
            return server.get();
        } else {
            return super.choose(key);
        }*/
    }

}
