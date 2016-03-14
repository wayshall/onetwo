package org.onetwo.plugins.zkclient.curator;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
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
	
	private Executor executor;
	

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
		if(dataSerializer==null){
			dataSerializer = new JsonDataSerializer();
		}
		
		this.start();
	}

	private void start() {
		curator.start();
		/*if(StringUtils.isNotBlank(rootPath))
			this.curator.usingNamespace(rootPath);*/
		logger.info("client has connect to zkserver!");
	}

	public String getActualNodePath(final String path){
		String nodePath = path;
		if(StringUtils.isNotBlank(nodePath)){
			nodePath = StringUtils.appendStartWithSlash(path);
		}
		nodePath = StringUtils.appendStartWith(nodePath, rootPath);
		return nodePath;
//		return ZKPaths.makePath(rootPath, path);
	}

	public Stat checkExists(String path){
		String fullPath = getActualNodePath(path);
		try {
			return curator.checkExists().forPath(fullPath);
		} catch (Exception e) {
			throw new BaseException("checkExists error for path: " + path + ", fullPath:"+ fullPath, e);
		}
	}
	

	public void creatingParentsIfNeeded(String path){
		creatingParentsIfNeeded(path, LangUtils.EMPTY_STRING.getBytes());
	}
	

	public void creatingParentsIfNeeded(String path, Object data){
		creatingParentsIfNeeded(path, data, defaultMode, false);
	}
	public void creatingParentsIfNeeded(String path, Object data, CreateMode mode, boolean checkBeforeCreate){
		String fullPath = getActualNodePath(path);
		byte[] seriaData = dataSerializer.serialize(data);
		
		if(mode==null){
			mode = defaultMode;
		}
		try {

			if(checkBeforeCreate && checkExists(fullPath)!=null){
				Stat result = curator.setData()
										.forPath(fullPath, seriaData);
				logger.info("create path success : {}", result);
//				return fullPath;
			}else{
				String result = curator.create()
								.creatingParentsIfNeeded()
								.withMode(mode)
								.forPath(fullPath, seriaData);
				logger.info("create path success : {}", result);
//				return result;
			}
			
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
	
	/****
	 * 
	 * @param path
	 * @return child path, not include parent path
	 */

	public List<String> getChildren(String path){
		return getChildren(path, false);
	}
	public List<String> getChildren(String path, boolean appendFullPath){
		try {
			String fullPath = getActualNodePath(path);
			List<String> childPaths = curator.getChildren().forPath(fullPath);
			if(appendFullPath){
				return childPaths.stream()
							.map(childPath->ZKPaths.makePath(fullPath, childPath))
							.collect(Collectors.toList());
			}else{
				return childPaths;
			}
		} catch (Exception e) {
			throw new BaseException("getChildren error for path: " + path, e);
		}
	}

	public String findFirstChild(String path, boolean fullPath){
		List<String> children = getChildren(path, fullPath);
		return LangUtils.isEmpty(children)?null:children.get(0);
	}
	
	public PathChildrenCache addPathChildrenListener(String parentPath, PathChildrenCacheListener listener){
		String path = getActualNodePath(parentPath);
		PathChildrenCache childrenNode = new PathChildrenCache(curator, path, true);
		try {
			childrenNode.start();
		} catch (Exception e) {
			throw new BaseException("add PathChildrenCacheListener error for path: " + path, e);
		}
		childrenNode.getListenable().addListener(listener, getExecutor());
		return childrenNode;
	}
	
	public CuratorFramework getCurator() {
		return curator;
	}

	public void setDataSerializer(DataSerializer dataSerializer) {
		this.dataSerializer = dataSerializer;
	}

	public Executor getExecutor() {
		if(executor==null){
			executor = Executors.newSingleThreadExecutor();
		}
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

}
