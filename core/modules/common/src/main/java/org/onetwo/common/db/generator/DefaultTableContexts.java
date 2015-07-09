package org.onetwo.common.db.generator;

import java.util.Map;

import org.onetwo.common.db.generator.DbGenerator.DbTableGenerator.TableGeneratedConfig;
import org.onetwo.common.db.generator.GlobalConfig.TableContexts;
import org.onetwo.common.utils.StringUtils;

import com.google.common.collect.Maps;

public class DefaultTableContexts implements TableContexts {
	private String stripTablePrefix;
	
	final private GlobalConfig globalGeneratedConfig;
	private TableContexts tableContexts;
	

	public DefaultTableContexts(GlobalConfig globalGeneratedConfig) {
		super();
		this.globalGeneratedConfig = globalGeneratedConfig;
	}

	@Override
	public Map<String, Object> getContexts(TableGeneratedConfig tableConfig) {
		Map<String, Object> context = Maps.newHashMap();
		
		String tableNameWithoutPrefix = tableConfig.tableNameStripStart(stripTablePrefix);
		context.put("tableNameWithoutPrefix", tableNameWithoutPrefix);
		context.put("className", StringUtils.toClassName(tableNameWithoutPrefix));
		context.put("propertyName", StringUtils.toPropertyName(tableNameWithoutPrefix));
		if(tableContexts!=null){
			Map<String, Object> other = tableContexts.getContexts(tableConfig);
			if(other!=null){
				context.putAll(other);
			}
		}
		return context;
	}

	
	public DefaultTableContexts stripTablePrefix(String stripTablePrefix) {
		this.stripTablePrefix = stripTablePrefix;
		return this;
	}

	public String getStripTablePrefix() {
		return stripTablePrefix;
	}

	public GlobalConfig end(){
		return globalGeneratedConfig;
	}

	public DefaultTableContexts tableContexts(TableContexts tableContexts) {
		this.tableContexts = tableContexts;
		return this;
	}
	
}