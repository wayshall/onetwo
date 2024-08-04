package org.onetwo.cloud.zuul.bugfix;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.DummyPing;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

/***
 * spring-cloud-netfix-eureka-client已经把Ribbon利用Eureka取注册在其上的信息这部分代码已经移除了。所以Zuul只能利用静态信息了。
 * @author way
 *
 */
@Configuration
@ConditionalOnBean(SpringClientFactory.class)
public class RibbonEurekaClientConfig {
	
//	private static final Logger logger = JFishLoggerFactory.getLogger(RibbonEurekaClientConfig.class);
	 
    @Autowired
    private DiscoveryClient discoveryClient;
 
    @Bean
    public IPing ribbonPing() {
        return new DummyPing();
    }
 
    @Bean
    public IRule ribbonRule(IClientConfig clientConfig) {
        AvailabilityFilteringRule rule = new AvailabilityFilteringRule();
        rule.initWithNiwsConfig(clientConfig);
        return rule;
    }
 
    @Bean
    public ServerList<?> ribbonServerList(IClientConfig clientConfig) {
 
        return new ServerList<Server>() {
            @Override
            public List<Server> getInitialListOfServers() {
                return new ArrayList<>();
            }
 
            @Override
            public List<Server> getUpdatedListOfServers() {
                List<Server> serverList = new ArrayList<>();
                List<ServiceInstance> instances = discoveryClient.getInstances(clientConfig.getClientName());
                if (instances != null && instances.size() == 0) {
                    return serverList;
                }
                for (ServiceInstance instance : instances) {
                    if (instance.isSecure()) {
                        serverList.add(new Server("https", instance.getHost(), instance.getPort()));
                    } else {
                        serverList.add(new Server("http", instance.getHost(), instance.getPort()));
                    }
                }
                return serverList;
            }
        };
    }

}
