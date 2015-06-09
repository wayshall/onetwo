package org.onetwo.plugins.jsonrpc.server.zk;

import javax.annotation.Resource;

import org.apache.zookeeper.data.Stat;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.plugins.jsonrpc.server.core.JsonRpcSerivceFoundEvent;
import org.onetwo.plugins.jsonrpc.server.core.JsonRpcSerivceListener;
import org.onetwo.plugins.zkclient.core.Zkclienter;
import org.slf4j.Logger;

public class ZkRegisterListener implements JsonRpcSerivceListener {
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private Zkclienter zkclienter;

	@Override
	public void onFinded(JsonRpcSerivceFoundEvent event) {
		logger.info("zkclienter: {}", zkclienter);
		String path = event.getName();
		Stat stat = zkclienter.exists(path, true);
		if(stat==null){
			zkclienter.createEphemeral(path);
		}
	}
	
	

}
