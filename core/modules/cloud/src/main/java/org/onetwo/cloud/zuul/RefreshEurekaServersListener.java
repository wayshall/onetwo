package org.onetwo.cloud.zuul;

import java.util.Set;

import org.onetwo.common.date.NiceDate;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;

import com.google.common.collect.Sets;
import com.netflix.discovery.EurekaEvent;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;

/**
 * @author weishao zeng
 * <br/>
 */
@Deprecated
public class RefreshEurekaServersListener implements ApplicationListener<EnvironmentChangeEvent>{

	volatile public static Set<ILoadBalancer> loadBalancers = Sets.newHashSet();
//	private ApplicationContext applicationContext;

	@Override
	public void onApplicationEvent(EnvironmentChangeEvent event) {
		loadBalancers.stream().forEach(lb -> {
			if (lb instanceof DynamicServerListLoadBalancer) {
				((DynamicServerListLoadBalancer<?>)lb).updateListOfServers();
			}
		});
	}

    protected void refreshServerList(final EurekaEvent event) {
//        ReflectUtils.invokeMethod("fireEvent", discoveryClient, event);
    }
    
    class RefrectEurekaServersEvent implements EurekaEvent {
    	@Override
        public String toString() {
            return "RefrectEurekaServersEvent[timestamp=" + NiceDate.Now().formatAsDateTime() + "]";
        }
    }
}

