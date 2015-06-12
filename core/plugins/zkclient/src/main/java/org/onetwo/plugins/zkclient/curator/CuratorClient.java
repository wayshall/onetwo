package org.onetwo.plugins.zkclient.curator;

import java.util.List;
import java.util.function.Function;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.zkclient.ZkclientPluginConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;

public class CuratorClient implements InitializingBean {
	
	
	final private Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private String connectString;
	private CuratorFramework curator;
	private int sessionTimeout = 60 * 1000;
	final private String rootPath;
	private CreateMode defaultMode = CreateMode.PERSISTENT;
	
	private DataSerializer dataSerializer;
	

	public CuratorClient(ZkclientPluginConfig zkclientPluginConfig) {
		this.connectString = zkclientPluginConfig.getServers();
		this.sessionTimeout = zkclientPluginConfig.getSessionTimeout();
		this.rootPath = zkclientPluginConfig.getRootPath();
	}

	public CuratorClient(String rootPath, String connectString) {
		super();
		this.connectString = connectString;
		this.rootPath = StringUtils.emptyIfNull(rootPath);
	}
	public CuratorClient(String rootPath, CuratorFramework curator) {
		this.curator = curator;
		this.rootPath = StringUtils.emptyIfNull(rootPath);
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(curator==null){
			Assert.hasText(connectString);
			curator = CuratorFrameworkFactory.builder()
					.connectString(connectString)
					.sessionTimeoutMs(sessionTimeout)
					.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
					.build();
		}
		
		this.start();
	}

	private void start() {
		curator.start();
		logger.info("client has connect to zkserver!");
	}

	protected String getActualNodePath(final String path){
		String nodePath = StringUtils.appendStartWithSlash(path);
		nodePath = StringUtils.appendStartWith(nodePath, rootPath);
		return nodePath;
	}

	public Stat checkExists(String path){
		try {
			String fullPath = getActualNodePath(path);
			return curator.checkExists().forPath(fullPath);
		} catch (Exception e) {
			throw new BaseException("checkExists error for path: " + path, e);
		}
	}
	

	public String creatingParentsIfNeeded(String path){
		return creatingParentsIfNeeded(path, LangUtils.EMPTY_STRING.getBytes());
	}
	

	public String creatingParentsIfNeeded(String path, Object data){
		return creatingParentsIfNeeded(path, data, defaultMode, false);
	}
	public String creatingParentsIfNeeded(String path, Object data, CreateMode mode, boolean checkBeforeCreate){
		String fullPath = getActualNodePath(path);
		if(checkBeforeCreate && checkExists(fullPath)!=null){
			return fullPath;
		}
		if(mode==null){
			mode = defaultMode;
		}
		byte[] seriaData = dataSerializer.serialize(data);
		try {
			String result = curator.create()
							.creatingParentsIfNeeded()
							.withMode(mode)
							.forPath(fullPath, seriaData);
			logger.info("create path success : {}", result);
			return result;
		} catch (Exception e) {
			throw new BaseException("create node error for path: " + path, e);
		}
	}
	

	public String creating(String path, Object data, CreateMode mode, boolean checkBeforeCreate){
		String fullPath = getActualNodePath(path);
		if(checkBeforeCreate && checkExists(fullPath)!=null){
			return fullPath;
		}
		if(mode==null){
			mode = defaultMode;
		}

		byte[] seriaData = dataSerializer.serialize(data);
		try {
			String result = curator.create()
							.withMode(mode)
							.forPath(fullPath, seriaData);
			logger.info("create path success : {}", result);
			return result;
		} catch (Exception e) {
			throw new BaseException("create node error for path: " + path, e);
		}
	}
	

	public <R> R doWith(Function<CuratorFramework, R> action){
		try {
			return action.apply(curator);
		} catch (Exception e) {
			throw new BaseException("occur error with curator object: " + e.getMessage(), e);
		}
	}

	
	public <T> T getData(String path, Class<T> dataClass){
		try {
			byte[] data = curator.getData().forPath(path);
			return dataSerializer.deserialize(data, dataClass);
		} catch (Exception e) {
			throw new BaseException("get node error for path: " + path, e);
		}
	}
	
	public List<String> getChildren(String path){
		try {
			String fullPath = getActualNodePath(path);
			return curator.getChildren().forPath(fullPath);
		} catch (Exception e) {
			throw new BaseException("getChildren error for path: " + path, e);
		}
	}

	public String findFirstChild(String path){
		List<String> children = getChildren(path);
		return LangUtils.isEmpty(children)?null:children.get(0);
	}
	
	public CuratorFramework getCurator() {
		return curator;
	}

	public void setDataSerializer(DataSerializer dataSerializer) {
		this.dataSerializer = dataSerializer;
	}

}
