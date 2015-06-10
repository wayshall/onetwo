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
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.zkclient.ZkclientPluginConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import com.google.common.base.Function;
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
	private String rootNode;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.rootNode = zkclientPluginConfig.getRootNode();
		
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
		logger.info("========> zkclient receive event[{} -> {}] from server, and zkeventBus has post it!", event.getPath(), event.getType());
		zkeventBus.post(new ZkclientEvent(event, this));
	}

	public Stat exists(String path, boolean watch){
		try {
			String nodePath = getActualNodePath(path);
			return zooKeeper.exists(nodePath, watch);
		} catch (Exception e) {
			handleException(path, e);
		} 
		return null;
	}

	public Stat existsOrCreate(String path, boolean watch, Function<String, String> action){
		try {
			String nodePath = getActualNodePath(path);
			Stat stat = zooKeeper.exists(nodePath, watch);
			if(stat==null){
				String createPath = action.apply(path);
				return exists(createPath, true);
			}
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
	
	protected String getActualNodePath(final String path){
		String nodePath = StringUtils.appendStartWithSlash(path);
		nodePath = StringUtils.appendStartWith(nodePath, rootNode);
		return nodePath;
	}
	
	public String create(final String path, byte data[], CreateMode createMode){
		return create(path, data, acl, createMode);
	}
	public String create(final String path, byte data[], List<ACL> acl, CreateMode createMode){
		String nodePath = getActualNodePath(path);
		
		try {
			nodePath = zooKeeper.create(nodePath, data, acl, createMode);
			logger.info("node[{}] has created!", path);
		}
		catch (Exception e) {
			handleException(nodePath, e);
			nodePath = null;
		}
		return nodePath;
	}
	
	protected void handleException(String path, Exception e){
		if(KeeperException.class.isInstance(e)){
			KeeperException ke = (KeeperException)e;
			Code errorCode = ke.code();
			logger.warn("create node[{"+path+"}] error with code : " + errorCode);
			if(errorCode!=Code.NODEEXISTS){
				throw new BaseException("create node["+path+"] error : " + errorCode);
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

	public String getRootNode() {
		return rootNode;
	}


}
