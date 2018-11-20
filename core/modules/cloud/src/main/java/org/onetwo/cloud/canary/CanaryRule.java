package org.onetwo.cloud.canary;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.cloud.canary.CanaryUtils.CanaryErrors;
import org.onetwo.cloud.canary.CanaryUtils.CanaryMode;
import org.onetwo.cloud.canary.CanaryUtils.CanaryServerNotFoundActions;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.web.utils.WebHolder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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
	final private Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	@Autowired
	private CanaryProperties canaryProperties;
	
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
    	CanaryMode canaryMode = canaryProperties.getDefaultMode();
    	if(requestOpt.isPresent()){
    		HttpServletRequest req = requestOpt.get();
    		canaryMode = CanaryMode.of(req.getHeader(CanaryUtils.HEADER_CANARY_ENABLED), canaryProperties.getDefaultMode());
    	}
    	
    	CanaryErrors canaryErrors = null;
    	if (canaryMode==CanaryMode.FORCE){
    		ILoadBalancer lb = getLoadBalancer();
    		Optional<Server> server = canaryPredicate.chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
    		if(server.isPresent()) {
    			return server.get();
	        }
    		String errorContext = CanaryUtils.getCurrentCanaryContext().map(ctx->ctx.toString()).orElse("");
    		JFishLoggerFactory.getCommonLogger().error("errorContext: {}", errorContext);
//    		throw new BaseException(CanaryErrors.CANARY_SERVER_NOT_MATCH);
    		canaryErrors = CanaryErrors.CANARY_SERVER_NOT_MATCH;
    		
    	} else if (canaryMode==CanaryMode.SMOOTHNESS){
    		ILoadBalancer lb = getLoadBalancer();
            Optional<Server> server = canaryPredicate.chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
            if (server.isPresent()) {
                return server.get();
            }
    	} else if (canaryMode==CanaryMode.CANARY_NONE){
    		ILoadBalancer lb = getLoadBalancer();
            Optional<Server> server = noCanaryFilterMetaPredicate.chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
            if (server.isPresent()) {
                return server.get();
            }
//    		throw new BaseException(CanaryErrors.CANARY_NONE_SERVER_NOT_MATCH);
    		canaryErrors = CanaryErrors.CANARY_NONE_SERVER_NOT_MATCH;
    	}
    	
    	if (canaryErrors!=null) {
    		CanaryServerNotFoundActions action = this.canaryProperties.getServerNotFoundAction();
    		if(action==CanaryServerNotFoundActions.THROW){
        		throw new BaseException(canaryErrors);
    		}
    		if(logger.isInfoEnabled()){
    			logger.info("client canaryMode[{}] can not found matched server, use {}. CanaryContext: {}", 
    						canaryMode, 
    						action,
    						CanaryUtils.getCurrentCanaryContext().map(ctx->ctx.toString()).orElse(""));
    		}
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
