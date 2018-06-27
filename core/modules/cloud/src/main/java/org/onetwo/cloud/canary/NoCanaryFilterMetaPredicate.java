package org.onetwo.cloud.canary;

import java.util.Map;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

/**
 * @author wayshall
 * <br/>
 */
public class NoCanaryFilterMetaPredicate extends AbstractServerPredicate {
	
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
		boolean hasCanaryFilter = metaData.entrySet().stream().anyMatch(entry->{
			return entry.getKey().startsWith(CanaryFilterMetaPredicate.CANARY_FILTERS_KEY);
		});
		return !hasCanaryFilter;
	}
	
}
