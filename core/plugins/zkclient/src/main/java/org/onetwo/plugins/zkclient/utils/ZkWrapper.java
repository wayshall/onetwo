package org.onetwo.plugins.zkclient.utils;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.javatuples.Pair;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

public class ZkWrapper {
	private static final Logger logger = JFishLoggerFactory.getLogger(ZkWrapper.class);
	
	public static ZkWrapper wrap(ZooKeeper zooKeeper){
		return new ZkWrapper(zooKeeper);
	}
	
	public static ZkWrapper wrap(ZooKeeper zooKeeper, List<ACL> acl){
		return new ZkWrapper(zooKeeper, acl);
	}
	
	private ZooKeeper zooKeeper;
	private List<ACL> acl;


	public ZkWrapper(ZooKeeper zooKeeper) {
		this(zooKeeper, Ids.OPEN_ACL_UNSAFE);
	}
	public ZkWrapper(ZooKeeper zooKeeper, List<ACL> acl) {
		super();
		this.zooKeeper = zooKeeper;
		this.acl = acl;
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
	public ZooKeeper getZooKeeper() {
		return zooKeeper;
	}

}
