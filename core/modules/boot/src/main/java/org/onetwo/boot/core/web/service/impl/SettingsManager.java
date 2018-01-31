package org.onetwo.boot.core.web.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.common.convert.Types;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;




/**
 * @author wayshall
 * <br/>
 */
public class SettingsManager {
	
	final private org.slf4j.Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private ConcurrentMap<String, String> settingsDefaultValueHolder = Maps.newConcurrentMap();
	private Map<String, Function<String, String>> settingsUpdater = Maps.newHashMap();
	@Autowired
	private BootJFishConfig bootJFishConfig;
	
	@PostConstruct
	public void initConfigUpdator(){
		this.registerUpdater("defaultPageSize", (newValue)->{
			int originValue = Page.getDefaultPageSize();
			Page.setDefaultPageSize(Types.asInteger(newValue));
			return String.valueOf(originValue);
		});
		this.registerUpdater("logErrorDetail", (newValue)->{
			boolean log = bootJFishConfig.isLogErrorDetail();
			bootJFishConfig.setLogErrorDetail(Types.asValue(newValue, boolean.class));
			return String.valueOf(log);
		});
	}
	
	final public void registerUpdater(String configName, Function<String, String> updater){
		this.settingsUpdater.put(configName, updater);
	}
	
	public void updateConfig(String configName, String value){
		Function<String, String> updater = this.settingsUpdater.get(configName);
		if(updater==null){
			logger.info("ignore, no updater function for config: {}", configName);
			return ;
		}
		String oldValue = updater.apply(value);
		settingsDefaultValueHolder.putIfAbsent(configName, oldValue);
		logger.info("configName[{}] change to {}", configName, value);
	}
	
	public String reset(String configName){
		String originValue = settingsDefaultValueHolder.get(configName);
		return this.settingsUpdater.get(configName).apply(originValue);
	}
	
	public void reset(){
		settingsDefaultValueHolder.forEach((configName, originValue)->{
			this.settingsUpdater.get(configName).apply(originValue);
		});
	}

}
