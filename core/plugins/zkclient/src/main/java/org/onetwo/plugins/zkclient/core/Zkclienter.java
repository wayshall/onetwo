package org.onetwo.plugins.zkclient.core;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.zkclient.ZkclientPluginConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import com.google.common.eventbus.EventBus;

public class Zkclienter implements InitializingBean, Watcher{
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

//	@Resource
	private ZkclientPluginConfig zkclientPluginConfig;
	private ZooKeeper zooKeeper;
	private List<ACL> acl = Ids.OPEN_ACL_UNSAFE;

	private EventBus zkeventBus = new EventBus("zkeventBus");
	private volatile boolean connected;
	@Resource
	private ApplicationContext applicationContext;
//	private Zkclienter zkclienter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, ZkEventListener> listeners = SpringUtils.getBeansAsMap(applicationContext, ZkEventListener.class);
		listeners.forEach((k, v)->{
			register(v);
			logger.info("reigistered ZkWatchedEventListener : {}", k);
		});
		
		zooKeeper = new ZooKeeper(zkclientPluginConfig.getServers(), zkclientPluginConfig.getSessionTimeout(),  this);
	}
	
	@Override
	public void process(WatchedEvent event) {
//		logger.info("zk event[{}] has trigged!", event.getType());
		if(event.getState()==KeeperState.SyncConnected){
			connected = true;
			logger.info("zkclient has connected!");
		}
		logger.info("zkclient receive event[{} -> {}] from server, and zkeventBus has post it!", event.getPath(), event.getType());
		zkeventBus.post(new ZkclientEvent(event, this));
	}
	
	public Stat exists(String path, boolean watch){
		try {
			return zooKeeper.exists(path, watch);
		} catch (Exception e) {
			handleException(path, e);
		} 
		return null;
	}

	public String createPersistentSeq(final String path){
		return create(path, LangUtils.EMPTY_STRING.getBytes(), acl, CreateMode.PERSISTENT_SEQUENTIAL);
	}
	
	public String createPersistent(final String path){
		return create(path, LangUtils.EMPTY_STRING.getBytes(), acl, CreateMode.PERSISTENT);
	}
	
	public String createEphemeralSeq(final String path){
		return create(path, LangUtils.EMPTY_STRING.getBytes(), acl, CreateMode.EPHEMERAL_SEQUENTIAL);
	}
	
	public String createEphemeral(final String path){
		return create(path, LangUtils.EMPTY_STRING.getBytes(), acl, CreateMode.EPHEMERAL);
	}
	
	public String create(final String path, CreateMode createMode){
		return create(path, LangUtils.EMPTY_STRING.getBytes(), acl, createMode);
	}
	
	public String create(final String path, byte data[], List<ACL> acl, CreateMode createMode){
		String nodePath = null;
		Code errorCode = null;
		try {
			nodePath = zooKeeper.create(path, data, acl, createMode);
		}/*catch (KeeperException.InvalidACLException e) {
			errorCode = e.code();
			logger.warn("invalid acl : " + e.getMessage(), e);
		} */
		catch (Exception e) {
			handleException(path, e);
		}
		return nodePath;
	}
	
	protected void handleException(String path, Exception e){
		if(KeeperException.class.isInstance(e)){
			KeeperException ke = (KeeperException)e;
			Code errorCode = ke.code();
			logger.warn("create node error with code : " + errorCode, ke);
			if(errorCode!=Code.NODEEXISTS){
				throw new BaseException("create node["+zkclientPluginConfig.getBaseNode()+"] error : " + errorCode);
			}
		}else{

			throw new BaseException("create node error: " + e.getMessage(), e);
		}
	}
	
	final public Zkclienter register(ZkEventListener zkEventListener){
		this.zkeventBus.register(zkEventListener);
		logger.info("zkclient registered listener: {}", zkEventListener);
		return this;
	}

	public boolean isConnected() {
		return connected;
	}

	public ZooKeeper getZooKeeper() {
		return zooKeeper;
	}

	public void setZkclientPluginConfig(ZkclientPluginConfig zkclientPluginConfig) {
		this.zkclientPluginConfig = zkclientPluginConfig;
	}


}
