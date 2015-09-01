package org.onetwo.common.db.generator;

import java.util.Map;

import org.onetwo.common.db.generator.DbGenerator.DbTableGenerator.TableGeneratedConfig;
import org.onetwo.common.db.generator.GlobalConfig.TableContextCreator;
import org.onetwo.common.utils.StringUtils;

import com.google.common.collect.Maps;

public class DefaultTableContexts implements TableContextCreator {
//	private String stripTablePrefix;
	
	final private GlobalConfig globalGeneratedConfig;
	private TableContextCreator tableContexts;
	

	public DefaultTableContexts(GlobalConfig globalGeneratedConfig) {
		super();
		this.globalGeneratedConfig = globalGeneratedConfig;
	}

	@Override
	public Map<String, Object> createContexts(TableGeneratedConfig tableConfig) {
		Map<String, Object> context = Maps.newHashMap();
		
		String tableNameWithoutPrefix = tableConfig.tableNameStripStart(globalGeneratedConfig.getStripTablePrefix());
		String className = StringUtils.toClassName(tableNameWithoutPrefix);
		String propertyName = StringUtils.toPropertyName(tableNameWithoutPrefix);
		context.put("tableNameWithoutPrefix", tableNameWithoutPrefix);
		context.put("shortTableName", tableNameWithoutPrefix);
		context.put("className", className);
		context.put("propertyName", propertyName);
		if(tableContexts!=null){
			Map<String, Object> other = tableContexts.createContexts(tableConfig);
			if(other!=null){
				context.putAll(other);
			}
		}
		return context;
	}

	/*
	public DefaultTableContexts stripTablePrefix(String stripTablePrefix) {
		this.stripTablePrefix = stripTablePrefix;
		return this;
	}

	public String getStripTablePrefix() {
		return stripTablePrefix;
	}*/

	public GlobalConfig end(){
		return globalGeneratedConfig;
	}

	public DefaultTableContexts tableContexts(TableContextCreator tableContexts) {
		this.tableContexts = tableContexts;
		return this;
	}
	
}