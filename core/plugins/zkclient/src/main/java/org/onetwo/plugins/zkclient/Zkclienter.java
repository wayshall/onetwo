package org.onetwo.plugins.zkclient;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.javatuples.Pair;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.eventbus.EventBus;

public class Zkclienter implements InitializingBean, Watcher{
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

//	@Resource
	private ZkclientPluginConfig zkclientPluginConfig;
	private ZooKeeper zooKeeper;
	private List<ACL> acl = Ids.OPEN_ACL_UNSAFE;

	private EventBus zkeventBus = new EventBus("zkeventBus");
//	private Zkclienter zkclienter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		zooKeeper = new ZooKeeper(zkclientPluginConfig.getServers(), zkclientPluginConfig.getSessionTimeout(),  this);
	}
	
	@Override
	public void process(WatchedEvent event) {
//		logger.info("zk event[{}] has trigged!", event.getType());
		if(event.getState()==KeeperState.SyncConnected){
			logger.info("zkclient has connected!");
		}
		zkeventBus.post(new ZkclientEvent(event, this));
		logger.info("zkclient receive event[{}] from server, and zkeventBus has post it!", event.getType());
	}
	

	public Pair<String, Code> createPersistentSeq(final String path){
		return create(path, LangUtils.EMPTY_STRING.getBytes(), acl, CreateMode.PERSISTENT_SEQUENTIAL);
	}
	
	public Pair<String, Code> createPersistent(final String path){
		return create(path, LangUtils.EMPTY_STRING.getBytes(), acl, CreateMode.PERSISTENT);
	}
	
	public Pair<String, Code> createEphemeralSeq(final String path){
		return create(path, LangUtils.EMPTY_STRING.getBytes(), acl, CreateMode.EPHEMERAL_SEQUENTIAL);
	}
	
	public Pair<String, Code> createEphemeral(final String path){
		return create(path, LangUtils.EMPTY_STRING.getBytes(), acl, CreateMode.EPHEMERAL);
	}
	
	public Pair<String, Code> create(final String path, CreateMode createMode){
		return create(path, LangUtils.EMPTY_STRING.getBytes(), acl, createMode);
	}
	
	public Pair<String, Code> create(final String path, byte data[], List<ACL> acl, CreateMode createMode){
		String nodePath = null;
		Code errorCode = null;
		try {
			nodePath = zooKeeper.create(path, data, acl, createMode);
		}/*catch (KeeperException.InvalidACLException e) {
			errorCode = e.code();
			logger.warn("invalid acl : " + e.getMessage(), e);
		} */
		catch (KeeperException e) {
			errorCode = e.code();
			/*if(Code.NODEEXISTS==errorCode){
				
			}*/
			logger.warn("create node error with code : " + errorCode, e);
		} catch (Exception e) {
			throw new BaseException("create node error: " + e.getMessage(), e);
		}
		return Pair.with(nodePath, errorCode);
	}
	
	public Zkclienter register(ZkEventListener zkEventListener){
		this.zkeventBus.register(zkEventListener);
		return this;
	}

	public ZooKeeper getZooKeeper() {
		return zooKeeper;
	}

	public void setZkclientPluginConfig(ZkclientPluginConfig zkclientPluginConfig) {
		this.zkclientPluginConfig = zkclientPluginConfig;
	}


}
