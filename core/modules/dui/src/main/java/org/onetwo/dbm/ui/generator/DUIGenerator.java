package org.onetwo.dbm.ui.generator;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.sql.DataSource;

import org.onetwo.common.db.generator.DbGenerator;
import org.onetwo.common.db.generator.DbGenerator.DbTableGenerator;
import org.onetwo.common.db.generator.DbGenerator.DbTableGenerator.TableGeneratedConfig;
import org.onetwo.common.db.generator.GeneratedResult;
import org.onetwo.common.db.generator.GlobalConfig.OutfilePathFunc;
import org.onetwo.common.db.generator.ftl.FtlEngine;
import org.onetwo.common.db.generator.ftl.TomcatDataSourceBuilder;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.jpa.BaseEntity;
import org.onetwo.dbm.ui.exception.DbmUIException;
import org.onetwo.dbm.ui.meta.DUIEntityMeta;
import org.onetwo.dbm.ui.spi.DUIMetaManager;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
public class DUIGenerator {

	public static DUIGenerator dataSource(DataSource dataSource){
		DUIGenerator generator = new DUIGenerator(new DbGenerator(dataSource, new FtlEngine()));
		return generator;
	}

	public static DUIGenerator createWithDburl(String dburl, String dbusername, String dbpassword){
		DataSource dataSource = TomcatDataSourceBuilder.newBuilder()
														.mysql(null, dbusername, dbpassword)
														.url(dburl)
														.build();
		DUIGenerator generator = new DUIGenerator(new DbGenerator(dataSource, new FtlEngine()));
		return generator;
	}
	
	public static DUIGenerator mysql(String dbname, String dbusername, String dbpassword){
		DataSource dataSource = TomcatDataSourceBuilder.newBuilder()
														.mysql(dbname, dbusername, dbpassword)
														.build();
		DUIGenerator generator = new DUIGenerator();
		generator.dbGenerator = new DbGenerator(dataSource, new FtlEngine());
		return generator;
	}
	
	private DbGenerator dbGenerator;
	private String projectPath = FileUtils.getMavenProjectDir().getPath();
	private String resourceDir = LangUtils.toString("${0}/src/main/resources", this.projectPath);
	private String javaSrcDir = LangUtils.toString("${0}/src/main/java", this.projectPath);
	private String pageFileBaseDir = LangUtils.toString("${0}/src/main/resources/templates", this.projectPath);
	
	private String testJavaSrcDir = LangUtils.toString("${0}/src/test/java", this.projectPath);
	private String testResourceDir = LangUtils.toString("${0}/src/test/resources", this.projectPath);
	private String testPageFileBaseDir = LangUtils.toString("${0}/src/test/resources/templates", this.projectPath);
	
	private String javaBasePackage;
	private String moduleName = "";
	private String stripTablePrefix = "";
	
	private String templateBasePath = "META-INF/dbgenerator/";
	
//	private WebadminGenerator webadmin;
	private List<WebadminGenerator> webadmins = Lists.newArrayList();
	
	private Map<String, Object> context = Maps.newHashMap();
	
	private boolean configured;
	private boolean overrideExistFile = true;
	
	public DUIGenerator(DbGenerator dbGenerator) {
		super();
		this.dbGenerator = dbGenerator;
	}
	
	private DUIGenerator() {
		super();
	}
	
	protected final void checkConfigured(String methodName){
		if(configured){
			throw new DbmException("dbGenerator has been configured, can not invoke method: " + methodName);
		}
	}
	
	public DbGenerator dbGenerator() {
		return dbGenerator;
	}

	public DUIGenerator projectPath(String projectPath) {
		this.projectPath = projectPath;
		return this;
	}

	public DUIGenerator pageFileBaseDir(String pageFileBaseDir) {
		this.pageFileBaseDir = pageFileBaseDir;
		this.dbGenerator.globalConfig().pageFileBaseDir(pageFileBaseDir);
		return this;
	}
	
	public DUIGenerator context(String key, Object value) {
		this.context.put(key, value);
		return this;
	}
	
	public DUIGenerator pluginBaseController(Class<?> pluginBaseControllerClass) {
		context.put("pluginBaseController", pluginBaseControllerClass.getName());
		return this;
	}

	public DUIGenerator configGenerator(Consumer<DbGenerator> configurer) {
		configurer.accept(dbGenerator);
		this.configured = true;
		return this;
	}
	
	public DUIGenerator mavenProjectDir(){
		Assert.hasText(javaBasePackage, "javaBasePackage not set!");
		return configGenerator(dbGenerator->{
			dbGenerator.stripTablePrefix(stripTablePrefix)
			.globalConfig()
				.projectPath(projectPath)
				.overrideExistFile(overrideExistFile)
//				.pageFileBaseDir(pageFileBaseDir)
				.resourceDir(resourceDir)
				.javaSrcDir(javaSrcDir)
				.javaBasePackage(javaBasePackage)
				.moduleName("")
//				.defaultTableContexts()
//				.end()
			.end();
		});
	}
	
	public DUIGenerator mavenProjectTestDir(){
		Assert.hasText(javaBasePackage, "javaBasePackage not set!");
		return configGenerator(dbGenerator->{
			dbGenerator.stripTablePrefix(stripTablePrefix)
			.globalConfig()
				.projectPath(projectPath)
				.pageFileBaseDir(testPageFileBaseDir)
				.resourceDir(testResourceDir)
				.javaSrcDir(testJavaSrcDir)
				.javaBasePackage(javaBasePackage)
				.moduleName(moduleName)
//				.defaultTableContexts()
//				.end()
			.end();
		});
	}
	
	public DUIGenerator pluginProjectDir(String pluginName){
		/*
		 * if (StringUtils.isBlank(moduleName)) { moduleName(pluginName); }
		 */
		this.mavenProjectDir();
		pageFileBaseDir = LangUtils.toString("${0}/src/main/resources/META-INF/resources/webftls/"+pluginName, this.projectPath);
		this.dbGenerator.globalConfig().pageFileBaseDir(pageFileBaseDir);
		
		return this;
	}
	
	public DUIGenerator javaBasePackage(String javaBasePackage) {
		this.checkConfigured("javaBasePackage");
		this.javaBasePackage = javaBasePackage;
		return this;
	}

	public DUIGenerator moduleName(String moduleName) {
//		this.checkConfigured("moduleName");
		this.moduleName = moduleName;
		this.context.put("moduleName", moduleName);
		return this;
	}

	public DUIGenerator stripTablePrefix(String stripTablePrefix) {
		this.checkConfigured("stripTablePrefix");
		this.stripTablePrefix = stripTablePrefix;
		return this;
	}
	public DUIGenerator overrideExistFile(boolean overrideExistFile) {
		this.checkConfigured("overrideExistFile");
		this.overrideExistFile = overrideExistFile;
		return this;
	}

	public WebadminGenerator webadminGenerator(String tableName){
		DbTableGenerator tableGenerator = dbGenerator.table(tableName);
		WebadminGenerator webadmin = new WebadminGenerator();
		
		Optional<DUIEntityMeta> duiEntityMeta = getDUIEntityMeta(tableName);
		if (duiEntityMeta.isPresent()) {
			webadmin.duiEntityMeta = duiEntityMeta.get();
			webadmin.duiEntityMeta.setStripPrefix(stripTablePrefix);
		}
		
		webadmin.tableGenerator = tableGenerator;
		webadmin.initGenerator();
		this.webadmins.add(webadmin);
		return webadmin;
	}

	public VuePageGenerator vueGenerator(Class<?> pageClass, String vueModuleName){
		DUIEntityMeta duiEntityMeta = getDUIEntityMeta(pageClass);
		duiEntityMeta.setStripPrefix(this.stripTablePrefix);

		DbTableGenerator tableGenerator = dbGenerator.table(duiEntityMeta.getTable().getName());
		
//		DbTableGenerator tableGenerator = dbGenerator.table(pageMeta.getTable().getName());
		VuePageGenerator vueGenerator = new VuePageGenerator();
		vueGenerator.tableGenerator = tableGenerator;
		vueGenerator.vueModuleName = vueModuleName;
		vueGenerator.duiEntityMeta = duiEntityMeta;
		vueGenerator.initGenerator();
//		vueGenerator.vueBaseDir = vueBaseDir;
//		if (LangUtils.isNotEmpty(duiEntityMeta.getEditableEntities())) {
//			duiEntityMeta.getEditableEntities().forEach(editable -> {
//				vueGenerator(editable.getEntityClass(), vueModuleName);
//			});
//		}
		return vueGenerator;
	}
	
	private DUIEntityMeta getDUIEntityMeta(Class<?> pageClass) {
		DUIEntityMeta meta = getDUIMetaManager().get(pageClass);
		if (meta==null) {
			throw new DbmUIException("DUIEntityMeta not found: " + pageClass);
		}
		return meta;
	}

	private Optional<DUIEntityMeta> getDUIEntityMeta(String tableName) {
		Optional<DUIEntityMeta> meta = getDUIMetaManager().findByTable(tableName);
		return meta;
	}
	
	private DUIMetaManager getDUIMetaManager() {
		DUIMetaManager duiMetaManager = Springs.getInstance().getBean(DUIMetaManager.class);
		if (duiMetaManager==null) {
			throw new DbmUIException("DUIMetaManager not found");
		}
		return duiMetaManager;
	}
	
	/****
	 * @see build()
	 * @author wayshall
	 */
	@Deprecated
	public void generate(){
//		Assert.hasText(javaSrcDir, "javaSrcDir not set!");
//		Assert.hasText(javaBasePackage, "javaBasePackage not set!");
//		generate(context);
		build().generate(context);
	}

	/****
	 * @see build()
	 * @author wayshall
	 */
	@Deprecated
	public void generate(Map<String, Object> context){
//		if(context!=this.context){
//			this.context.putAll(context);
//		}
//		if(webadmin!=null){
//			List<GeneratedResult<File>> resullt = this.dbGenerator.generate(context);
//			System.out.println("webadmin result: " + resullt);
//		}
		build().generate(context);
	}
	
	public GeneratorExecutor build(){
		if(!this.configured){
//			throw new BaseException("dbGenerator has not been configured");
			this.mavenProjectDir();
		}
		GeneratorExecutor executor = new GeneratorExecutor();
		return executor;
	}
	
	public class GeneratorExecutor {
		
		public void generate(){
			Assert.hasText(javaSrcDir, "javaSrcDir not set!");
			Assert.hasText(javaBasePackage, "javaBasePackage not set!");
			generate(context);
		}
		
		public void generate(Map<String, Object> context){
			Map<String, Object> gcontext = Maps.newHashMap();
			gcontext.putAll(DUIGenerator.this.context);
			if (context!=null) {
				gcontext.putAll(context);
			}
			if(!webadmins.isEmpty()){
				List<GeneratedResult<File>> resullt = dbGenerator.generate(context);
				System.out.println("generate result: " + resullt);
			}
		}
	}
	
	public class WebadminGenerator {
		private String templateName = templateBasePath + "webadmin";
		private DbTableGenerator tableGenerator;
		private DUIEntityMeta duiEntityMeta;
//		private VuePageGenerator vueGenerator;
		
		public void initGenerator() {
			if (duiEntityMeta!=null) {
				tableGenerator.context().put("DUIEntityMeta", duiEntityMeta);
			}
		}
		
		public WebadminGenerator generateServiceImpl(){
			tableGenerator.serviceImplTemplate(templateName+"/ServiceImpl.java.ftl");
			return this;
		}
		
		public WebadminGenerator generateController(Class<?> pluginBaseController){
			context.put("pluginBaseController", pluginBaseController.getName());
			tableGenerator.javaClassTemplate("controller", templateName+"/Controller.java.ftl");
			return this;
		}
		
		public WebadminGenerator generateVueController(){
			tableGenerator.javaClassTemplate("controller", templateName+"/MgrController.java.ftl");
			return this;
		}
		
		public WebadminGenerator generateEntity(){
			tableGenerator.entityTemplate(templateName+"/Entity.java.ftl");
			return this;
		}
		
		public WebadminGenerator generateUIEntity(){
			return generateUIEntity(null);
		}
		
		public WebadminGenerator generateUIEntity(Class<?> baseEntity){
			if (baseEntity!=null) {
				tableGenerator.context().put("baseEntityClass", baseEntity.getName());
			} else {
				tableGenerator.context().put("baseEntityClass", BaseEntity.class.getSimpleName());
			}
			tableGenerator.entityTemplate(templateName+"/dui/Entity.java.ftl");
			return this;
		}
		
		public WebadminGenerator generatePage(){
			tableGenerator.pageTemplate(templateName+"/index.html.ftl");
			tableGenerator.pageTemplate(templateName+"/edit-form.html.ftl");
			return this;
		}
		public WebadminGenerator generate(String templatePath, OutfilePathFunc outFileNameFunc){
			tableGenerator.pageTemplate(templatePath, outFileNameFunc);
			return this;
		}
		
		public DUIGenerator end(){
			return DUIGenerator.this;
		}
		
	}
	
	public class VuePageGenerator extends BaseDbmGenerator {
		private String templateName = templateBasePath + "vue";
//		String vueBaseDir;
		String viewDir = "/src/views";
		String apiDir = "/src/api";
		String vueModuleName;
		DUIEntityMeta duiEntityMeta;
		
//		VuePageGenerator cascadeEntityGenerator;
		
		public void initGenerator() {
			tableGenerator.context().put("vueModuleName", vueModuleName);
			tableGenerator.context().put("DUIEntityMeta", duiEntityMeta);
			
			if (LangUtils.isNotEmpty(duiEntityMeta.getEditableEntities())) {
				duiEntityMeta.getEditableEntities().forEach(editable -> {
					DUIGenerator.this.webadminGenerator(editable.getTable().getName())
										.generateVueController()
										.generateServiceImpl();
					VuePageGenerator vg = DUIGenerator.this.vueGenerator(editable.getEntityClass(), vueModuleName);
					vg.generateVueMgrForm();
					vg.generateVueJsApi();
				});
			}
		}

		Function<String, OutfilePathFunc> vueFileNameFuncCreator = path -> {
			return ctx->{
				TableGeneratedConfig c = ctx.getConfig();
//				Assert.hasText(vueBaseDir, "vueBaseDir must be has text!");
				String tableShortName = c.tableNameStripStart(c.globalGeneratedConfig().getStripTablePrefix());
				String pageFileBaseDir = c.globalGeneratedConfig().getPageFileBaseDir();
				Assert.notNull(pageFileBaseDir, "pageFileBaseDir can not be null");
				String moduleName = viewModuleName(c);
				Assert.notNull(moduleName, "moduleName can not be null");
				
				String fileName = StringUtils.toCamel(tableShortName, false);
				// Mgr.vue
				String fileNameWithoutExt = FileUtils.getFileNameWithoutExt(path);
				// remove Mgr
				fileNameWithoutExt = StringUtils.trimStartWith(fileNameWithoutExt, "Mgr");
				String filePath = pageFileBaseDir + 
									viewDir +
									"/"+moduleName+"/"+
									fileName + 
									fileNameWithoutExt;
				return filePath;
			};
		};
		
		private String viewModuleName(TableGeneratedConfig c) {
			return StringUtils.isNotBlank(vueModuleName)?vueModuleName:c.globalGeneratedConfig().getModuleName();
		}

		
		public VuePageGenerator viewDir(String viewDir){
			this.viewDir = viewDir;
			return this;
		}
		
		public VuePageGenerator apiDir(String apiDir){
			this.apiDir = apiDir;
			return this;
		}
		
		public VuePageGenerator generateVueCrud(){
			this.generateVueJsApi();
			if (duiEntityMeta.isTree()) {
				this.generateVueTree();
				if (duiEntityMeta.getTreeGrid().isCascadeOnRightStyle()) {
					VuePageGenerator cascadeEntityGenerator = vueGenerator(duiEntityMeta.getTreeGrid().getCascadeEntity(), vueModuleName);
					cascadeEntityGenerator.generateVueCrud();
				}
			}
			this.generateVueMgr();
			this.generateVueMgrForm();
			return this;
		}
		
		public VuePageGenerator generateVueMgr(){
			String mgrPath = templateName+"/Mgr.vue.ftl";
			tableGenerator.pageTemplate(mgrPath, vueFileNameFuncCreator.apply(mgrPath));
			return this;
		}
		
		public VuePageGenerator generateVueList(){
			String mgrPath = templateName+"/List.vue.ftl";
			tableGenerator.pageTemplate(mgrPath, vueFileNameFuncCreator.apply(mgrPath));
			return this;
		}
		
		public VuePageGenerator generateVueTree(){
			String mgrPath = templateName+"/Tree.vue.ftl";
			tableGenerator.pageTemplate(mgrPath, vueFileNameFuncCreator.apply(mgrPath));
			return this;
		}
		
		public VuePageGenerator generateVueMgrForm(){
			String formPath = templateName+"/Form.vue.ftl";
			tableGenerator.pageTemplate(formPath, vueFileNameFuncCreator.apply(formPath));
			return this;
		}
		
		public VuePageGenerator generateVueJsApi(){
//			String apiDir = "/src/api";
			Function<String, OutfilePathFunc> outFileNameFuncCreator = path -> {
				return ctx->{
					TableGeneratedConfig c = ctx.getConfig();
					String tableShortName = c.tableNameStripStart(c.globalGeneratedConfig().getStripTablePrefix());
					String pageFileBaseDir = c.globalGeneratedConfig().getPageFileBaseDir();
					Assert.notNull(pageFileBaseDir, "pageFileBaseDir can not be null");
					String moduleName = viewModuleName(c);
					Assert.notNull(moduleName, "moduleName can not be null");

					String fileName = StringUtils.toCamel(tableShortName, false);
					String filePath = pageFileBaseDir + 
									apiDir + 
									"/"+moduleName+"/"+
									fileName + 
									FileUtils.getFileNameWithoutExt(path);
					return filePath;
				};
			};
			
			String mgrPath = templateName+"/Api.js.ftl";
			tableGenerator.pageTemplate(mgrPath, outFileNameFuncCreator.apply(mgrPath));
			
			return this;
		}
	}
	
	abstract protected class BaseDbmGenerator {
		protected DbTableGenerator tableGenerator;
		
		public DUIGenerator end(){
			return DUIGenerator.this;
		}
	}
	
}
