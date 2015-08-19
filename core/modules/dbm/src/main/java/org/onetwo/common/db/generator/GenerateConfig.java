package org.onetwo.common.db.generator;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.db.generator.DbGenerator.DbTableGenerator.TableGeneratedConfig;
import org.onetwo.common.db.generator.GlobalConfig.OutfilePathFunc;

import com.google.common.collect.Maps;

public class GenerateConfig {

	public static interface ControllerPathGenerator {
		String getControllerPath(TableGeneratedConfig tableConfig);
	}
	
	private HashMap<String, Object> rootContext = Maps.newHashMap();

	private String javaBasePackage;
	private String javaSrcDir;
	
	private String pageFileBaseDir;
	
	private OutfilePathFunc outfilePathFunc;
	private ControllerPathGenerator controllerPathGenerator;

	public GenerateConfig() {
		super();
		put("_config", this);;
	}

	final public void put(String key, Object value){
		this.rootContext.put(key, value);
	}

	public void putAll(Map<String, Object> m){
		this.rootContext.putAll(m);
	}

	public HashMap<String, Object> getRootContext() {
		return rootContext;
	}
	
	/*public String getControllerPath(TableMeta table){
		
	}*/
	
	public OutfilePathFunc getOutfilePathFunc() {
		return outfilePathFunc;
	}

	public GenerateConfig outfilePathFunc(OutfilePathFunc outFileNameFunc) {
		this.outfilePathFunc = outFileNameFunc;
		return this;
	}

	public String getJavaBasePackage() {
		return javaBasePackage;
	}

	public void setJavaBasePackage(String javaBasePackage) {
		this.javaBasePackage = javaBasePackage;
	}

	public String getPageFileBaseDir() {
		return pageFileBaseDir;
	}

	public GenerateConfig pageFileBaseDir(String pageFileBaseDir) {
		this.pageFileBaseDir = pageFileBaseDir;
		return this;
	}

	public String getJavaSrcDir() {
		return javaSrcDir;
	}

	public GenerateConfig javaSrcDir(String javaSrcDir) {
		this.javaSrcDir = javaSrcDir;
		return this;
	}

	public ControllerPathGenerator getControllerPathGenerator() {
		return controllerPathGenerator;
	}

	public GenerateConfig controllerPathGenerator(ControllerPathGenerator controllerPathGenerator) {
		this.controllerPathGenerator = controllerPathGenerator;
		return this;
	}
	
}
