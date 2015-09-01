package org.onetwo.common.db.generator;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.db.generator.DbGenerator.DbTableGenerator.TableGeneratedConfig;
import org.onetwo.common.utils.StringUtils;

import com.google.common.collect.Maps;

public class GlobalConfig {
	public static final String TABLE_CONTEXT_KEY = "_tableContext";
	public static final String GLOBAL_CONTEXT_KEY = "_globalConfig";

	public static interface OutfilePathFunc {
		String getOutFileName(TableGeneratedConfig tableConfig);
	}

	public static interface ControllerPathGenerator {
		String getControllerPath(TableGeneratedConfig tableConfig);
	}

	public static interface TableContextCreator {
		Map<String, Object> createContexts(TableGeneratedConfig tableConfig);
	}
	
	private HashMap<String, Object> rootContext = Maps.newHashMap();

	private String javaBasePackage;
	private String moduleName;
	private String javaSrcDir;
	private String resourceDir;

	private String pageFileBaseDir;
	
	private OutfilePathFunc outFileNameFunc;
	private ControllerPathGenerator controllerPathGenerator;
	private DefaultTableContexts defaultTableContexts = new DefaultTableContexts(this);
	
	private final DbGenerator dbGenerator;
	
	private String stripTablePrefix;

	public GlobalConfig(DbGenerator dbGenerator) {
		super();
		this.dbGenerator = dbGenerator;
		put(GLOBAL_CONTEXT_KEY, this);;
	}

	final public DbGenerator end(){
		return dbGenerator;
	}

	final public GlobalConfig put(String key, Object value){
		this.rootContext.put(key, value);
		return this;
	}

	public GlobalConfig putAll(Map<String, Object> m){
		this.rootContext.putAll(m);
		return this;
	}

	HashMap<String, Object> getRootContext() {
		return rootContext;
	}
	
	/*public String getControllerPath(TableMeta table){
		
	}*/
	
	OutfilePathFunc getOutFileNameFunc() {
		return outFileNameFunc;
	}

	public GlobalConfig outFileNameFunc(OutfilePathFunc outFileNameFunc) {
		this.outFileNameFunc = outFileNameFunc;
		return this;
	}

	public String getJavaBasePackage() {
		return javaBasePackage;
	}

	public GlobalConfig javaBasePackage(String javaBasePackage) {
		this.javaBasePackage = javaBasePackage;
		return this;
	}

	public String getPageFileBaseDir() {
		return pageFileBaseDir;
	}

	public GlobalConfig pageFileBaseDir(String pageFileBaseDir) {
		this.pageFileBaseDir = pageFileBaseDir;
		return this;
	}

	public String getJavaSrcDir() {
		return javaSrcDir;
	}

	public GlobalConfig javaSrcDir(String javaSrcDir) {
		this.javaSrcDir = javaSrcDir;
		return this;
	}

	public ControllerPathGenerator getControllerPathGenerator() {
		return controllerPathGenerator;
	}

	public GlobalConfig controllerPathGenerator(ControllerPathGenerator controllerPathGenerator) {
		this.controllerPathGenerator = controllerPathGenerator;
		return this;
	}

	TableContextCreator getTableContextCreator() {
		return defaultTableContexts;
	}

	public DefaultTableContexts defaultTableContexts() {
		return defaultTableContexts;
	}

	public String getModuleName() {
		return moduleName;
	}

	public GlobalConfig moduleName(String moduleName) {
		this.moduleName = moduleName;
		return this;
	}

	public String getFullModulePackageName() {
		return getJavaBasePackage()+StringUtils.appendStartWith(getModuleName(), ".");
	}

	public String getFullModulePackageNameAsPath() {
		String packagePath = StringUtils.replaceEach(getFullModulePackageName(), ".", "/");
		return packagePath;
	}

	public String getFullModulePackagePath() {
		String packagePath = StringUtils.replaceEach(getFullModulePackageName(), ".", "/");
		String path = StringUtils.trimEndWith(getJavaSrcDir(), "/");
		path = path + "/" + packagePath;
		return path;
	}

	public void stripTablePrefix(String stripTablePrefix) {
		this.stripTablePrefix = stripTablePrefix;
	}

	public String getStripTablePrefix() {
		return stripTablePrefix;
	}

	public String getResourceDir() {
		return resourceDir;
	}

	public GlobalConfig resourceDir(String resourceDir) {
		this.resourceDir = resourceDir;
		return this;
	}
}
