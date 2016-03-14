package org.onetwo.plugins.jsonrpc.server.zk;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.plugins.zkclient.curator.CuratorClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class RpcServerZkClienter implements InitializingBean {
	
	final private Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private CuratorClient curatorClient;
	

	public RpcServerZkClienter() {
		
	}
	

	public RpcServerZkClienter(CuratorClient curatorClient) {
		super();
		this.curatorClient = curatorClient;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(curatorClient);
		
	}
	
	public CuratorClient getCuratorClient() {
		return curatorClient;
	}

	public void setCuratorClient(CuratorClient curatorClient) {
		this.curatorClient = curatorClient;
	}


}
