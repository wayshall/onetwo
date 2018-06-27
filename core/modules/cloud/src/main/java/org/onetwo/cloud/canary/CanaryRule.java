package org.onetwo.cloud.canary;

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
    
    public CanaryRule(){
    	EurekaMetaPredicate metaPredicate = new EurekaMetaPredicate();
    	this.canaryPredicate = CompositePredicate.withPredicates(metaPredicate)
								                /*.addFallbackPredicate(this.getPredicate())
								                .addFallbackPredicate(AbstractServerPredicate.alwaysTrue())*/
								                .build();
    }
	
    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        Optional<Server> server = canaryPredicate.chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
        if (server.isPresent()) {
            return server.get();
        } else {
            return super.choose(key);
        }
    }

}
