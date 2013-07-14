package org.onetwo.plugins.codegen.generator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.fish.orm.TableInfo;
import org.onetwo.common.utils.LangUtils;

public class GenContext implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4005047454897540100L;

	private String modelName;
	private String tablePrefix;
	private String generateOutDir;
	
	private Map<String, ?> context = new HashMap<String, Object>();

	public String getTablePrefix() {
		return tablePrefix;
	}
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}
	public Map<String, ?> getContext() {
		return context;
	}
	public void setContext(Map<String, ?> context) {
		this.context = context;
	}

	public String getClassName(TableInfo table) {
		return GenerateUtils.toClassName(table.getName().substring(tablePrefix.length()));
	}

	public String getTableNameNoPrefix(TableInfo table) {
		return table.getName().substring(tablePrefix.length());
	}
	public String getGenerateOutDir() {
		return generateOutDir;
	}
	public void setGenerateOutDir(String generateOutDir) {
		this.generateOutDir = generateOutDir;
	}
	
	public String toString(){
		return LangUtils.toString(this);
	}


	public String getModelName() {
		return modelName;
	}


	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

}
