package org.onetwo.common.db.generator;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.onetwo.common.db.generator.GlobalConfig.OutfilePathFunc;
import org.onetwo.common.db.generator.dialet.DatabaseMetaDialet;
import org.onetwo.common.db.generator.dialet.MysqlMetaDialet;
import org.onetwo.common.db.generator.mapping.ColumnMapping;
import org.onetwo.common.db.generator.mapping.ColumnMapping.ColumnAttrValueFunc;
import org.onetwo.common.db.generator.meta.TableMeta;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.StringUtils;

import com.google.common.collect.Lists;

public class DbGenerator {
	private static final String TABLE_CONTEXT_KEY = GlobalConfig.TABLE_CONTEXT_KEY;
	private static final String TABLE_KEY = "table";
	
	public static DbGenerator newGenerator(DataSource dataSource){
		return new DbGenerator(dataSource);
	}

	private DataSource dataSource;
	private List<DbTableGenerator> tableGenerators = Lists.newArrayList();
	private DatabaseMetaDialet dialet;
	private TemplateEngine templateEngine;
	private GlobalConfig globalConfig = new GlobalConfig(this);
	
	public DbGenerator(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public DbGenerator(DatabaseMetaDialet dialet) {
		super();
		this.dialet = dialet;
	}

	public DbGenerator(DataSource dataSource, TemplateEngine ftlGenerator) {
		super();
		this.dataSource = dataSource;
		this.templateEngine = ftlGenerator;
	}
	public DbGenerator templateEngine(TemplateEngine templateEngine){
		this.templateEngine = templateEngine;
		return this;
	}
	
	public DbGenerator mysql(){
		this.dialet = new MysqlMetaDialet(dataSource);
		return this;
	}
	
	public DbGenerator dialet(DatabaseMetaDialet dialet){
		this.dialet = dialet;
		return this;
	}
	
	public DbGenerator allColumnMappingAttr(String name, Object value){
		this.dialet.getMetaMapping().getColumnMappings().forEach(map->map.attr(name, value));
		return this;
	}
	
	public DbGenerator allColumnMappingAttr(String name, ColumnAttrValueFunc value){
		this.dialet.getMetaMapping().getColumnMappings().forEach(map->map.attr(name, value));
		return this;
	}
	
	public ColumnMappingBuilder columnMapping(int sqlType){
		ColumnMapping mapping = this.dialet.getColumnMapping(sqlType);
		return new ColumnMappingBuilder(mapping);
	}

	public DbGenerator addTableDbGenerator(DbTableGenerator tableGenerator){
		tableGenerators.add(tableGenerator);
		return this;
	}

	public GlobalConfig globalConfig(){
		return globalConfig;
	}

	public DbGenerator stripTablePrefix(String stripTablePrefix){
//		globalConfig.defaultTableContexts().stripTablePrefix(stripTablePrefix);
		globalConfig.stripTablePrefix(stripTablePrefix);
		return this;
	}

	public DbTableGenerator table(String tableName){
		DbTableGenerator tableg = new DbTableGenerator(tableName);
		tableGenerators.add(tableg);
		return tableg;
	}

	public DbGenerator tables(String... tableNames){
		Stream.of(tableNames).forEach(tableName->{
			DbTableGenerator tableg = new DbTableGenerator(tableName);
			tableGenerators.add(tableg);
		});
		return this;
	}

	/*public DbGenerator generateConfig(String templatePath, String outDir){
		tableGenerators.forEach(tg->{
			String prefix = tg.tableName.replace("_", "-");
			String outfilePath = outDir + "/"+ prefix + "-" + FileUtils.getFileNameWithoutExt(templatePath);
			tg.template(templatePath).outfilePath(outfilePath);
		});
		return this;
	}*/
	public DbGenerator generateConfig(String templatePath, OutfilePathFunc outFunc){
		tableGenerators.forEach(tg->{
			tg.template(templatePath).outfilePathFunc(outFunc);
		});
		return this;
	}
	
	public List<GeneratedResult<File>> generate(Map<String, Object> context){
		templateEngine.afterPropertiesSet();
		Assert.notNull(dialet, "dialet can not be null!");
		List<GeneratedResult<File>> results = Lists.newArrayList();
		tableGenerators.forEach(table->{
			GeneratedResult<File> r = table.generate(dialet, templateEngine, context);
			results.add(r);
		});
		return results;
	}
	
	public List<GeneratedResult<String>> generateString(Map<String, Object> context){
		templateEngine.afterPropertiesSet();
		Assert.notNull(dialet, "dialet can not be null!");
		List<GeneratedResult<String>> results = Lists.newArrayList();
		tableGenerators.forEach(table->{
			GeneratedResult<String> r = table.generateString(dialet, templateEngine, context);
			results.add(r);
		});
		return results;
	}

	
	public class ColumnMappingBuilder {
		private ColumnMapping metaMapping;

		public ColumnMappingBuilder(ColumnMapping metaMapping) {
			super();
			this.metaMapping = metaMapping;
		}
		
		public ColumnMappingBuilder javaType(Class<?> javaType){
			this.metaMapping.javaType(javaType);
			return this;
		}
		
		public ColumnMappingBuilder attr(String name, Object value){
			this.metaMapping.attr(name, value);
			return this;
		}
		
		public DbGenerator endColumMapping(){
			return DbGenerator.this;
		}
		
	}
	public class DbTableGenerator {
		
		private String tableName;
		private List<TableGeneratedConfig> generatedConfigs = Lists.newArrayList();
		

		public DbTableGenerator(String tableName) {
			super();
			this.tableName = tableName;
		}
		
		public DbTableGenerator addGeneratedConfig(String templatePath, String outfilePath){
			generatedConfigs.add(new TableGeneratedConfig(templatePath, outfilePath));
			return this;
		}
		
		public TableGeneratedConfig template(String templatePath){
			TableGeneratedConfig config = new TableGeneratedConfig(tableName, templatePath);
			generatedConfigs.add(config);
			return config;
		}
		
		public TableGeneratedConfig template(String templatePath, OutfilePathFunc outFileNameFunc){
			TableGeneratedConfig config = new TableGeneratedConfig(tableName, templatePath);
			config.outfilePathFunc(outFileNameFunc);
			generatedConfigs.add(config);
			return config;
		}
		
		public DbTableGenerator pageTemplate(String templatePath){
			TableGeneratedConfig config = new TableGeneratedConfig(tableName, templatePath);
			config.outfilePathFunc(c->{
										String tableShortName = c.tableNameStripStart(c.globalGeneratedConfig().getStripTablePrefix());
										String filePath = c.globalGeneratedConfig().getPageFileBaseDir()+
										"/"+c.globalGeneratedConfig().getModuleName()+"/"+
										tableShortName.replace('_', '-')+
										"-"+FileUtils.getFileNameWithoutExt(templatePath);
										return filePath;
									}
								);
			generatedConfigs.add(config);
			return this;
		}
		
		public DbTableGenerator mybatisDaoXmlTemplate(String templatePath){
			TableGeneratedConfig config = new TableGeneratedConfig(tableName, templatePath);
			config.outfilePathFunc(c->{
										String filePath = c.globalGeneratedConfig().getFullModulePackageNameAsPath();
										filePath =  "/mybatis/dao/" + filePath + "/dao";
										filePath = this.getResourceDirOutfilePathByModule(c, filePath, templatePath);
										return filePath;
									}
								);
			generatedConfigs.add(config);
			return this;
		}
		
		private String getResourceDirOutfilePathByModule(TableGeneratedConfig c, String typePath, String templatePath){
			String tableShortName = c.tableNameStripStart(c.globalGeneratedConfig().getStripTablePrefix());
			String filePath = c.globalGeneratedConfig().getResourceDir()+typePath+ "/" + 
			StringUtils.toClassName(tableShortName) + FileUtils.getFileNameWithoutExt(templatePath);
			return filePath;
		}
		
		public DbTableGenerator controllerTemplate(String templatePath){
			TableGeneratedConfig config = new TableGeneratedConfig(tableName, templatePath);
			config.outfilePathFunc(c->{
									String tableShortName = c.tableNameStripStart(c.globalGeneratedConfig().getStripTablePrefix());
									String filePath = c.globalGeneratedConfig().getFullModulePackagePath()+"/web/"+
									StringUtils.toClassName(tableShortName)+
									FileUtils.getFileNameWithoutExt(templatePath);
									return filePath;
								}
							);
			generatedConfigs.add(config);
			return this;
		}
		
		public DbTableGenerator serviceImplTemplate(String templatePath){
			TableGeneratedConfig config = new TableGeneratedConfig(tableName, templatePath);
			config.outfilePathFunc(c->{
									String tableShortName = c.tableNameStripStart(c.globalGeneratedConfig().getStripTablePrefix());
									String filePath = c.globalGeneratedConfig().getFullModulePackagePath()+"/service/"+
									StringUtils.toClassName(tableShortName)+
									FileUtils.getFileNameWithoutExt(templatePath);
									return filePath;
								}
							);
			generatedConfigs.add(config);
			return this;
		}
		
		public DbTableGenerator daoTemplate(String templatePath){
			TableGeneratedConfig config = new TableGeneratedConfig(tableName, templatePath);
			config.outfilePathFunc(c->getJavaSrcOutfilePathByType(config, "/dao", templatePath));
			generatedConfigs.add(config);
			return this;
		}
		
		public DbTableGenerator entityTemplate(String templatePath){
			TableGeneratedConfig config = new TableGeneratedConfig(tableName, templatePath);
			config.outfilePathFunc(c->getJavaSrcOutfilePathByType(config, "/entity", templatePath));
			generatedConfigs.add(config);
			return this;
		}
		
		private String getJavaSrcOutfilePathByType(TableGeneratedConfig c, String typePath, String templatePath){
			String tableShortName = c.tableNameStripStart(c.globalGeneratedConfig().getStripTablePrefix());
			String filePath = c.globalGeneratedConfig().getFullModulePackagePath()+ typePath+ "/" + 
			StringUtils.toClassName(tableShortName)+
			FileUtils.getFileNameWithoutExt(templatePath);
			return filePath;
		}
		
		public DbGenerator end(){
			return DbGenerator.this;
		}
		
		public String getTableName() {
			return tableName;
		}

		private GeneratedResult<File> generate(DatabaseMetaDialet dialet, TemplateEngine ftlGenerator, Map<String, Object> outContext){
			if(outContext!=null)
				globalConfig.putAll(outContext);
			
			TableMeta tableMeta = dialet.getTableMeta(tableName);
			tableMeta.setStripPrefix(globalConfig.getStripTablePrefix());
			globalConfig.put(TABLE_KEY, tableMeta);
			
			List<File> files = Lists.newArrayList();
			generatedConfigs.stream().forEach(config->{
				Assert.hasText(config.templatePath);
				
//				Assert.hasText(config.outfilePath);
				OutfilePathFunc outFileNameFunc = config.outfilePathFunc==null?globalConfig.getOutFileNameFunc():config.outfilePathFunc;
				Assert.notNull(outFileNameFunc, "no outFileNameFunc");
				String outfilePath = outFileNameFunc.getOutFileName(config);
				
				Map<String, Object> tableContext = globalConfig.getTableContexts().getContexts(config);
				if(tableContext!=null){
					globalConfig.put(TABLE_CONTEXT_KEY, tableContext);
				}
				File file = ftlGenerator.generateFile(globalConfig.getRootContext(), config.templatePath, outfilePath);
				files.add(file);
			});
			return new GeneratedResult<File>(tableName, files);
		}

		private GeneratedResult<String> generateString(DatabaseMetaDialet dialet, TemplateEngine ftlGenerator, Map<String, Object> outContext){
			if(outContext!=null)
				globalConfig.putAll(outContext);
			
			TableMeta tableMeta = dialet.getTableMeta(tableName);
			globalConfig.put(TABLE_KEY, tableMeta);
			
			List<String> contents = Lists.newArrayList();
			generatedConfigs.stream().forEach(config->{
				Assert.hasText(config.templatePath);
				
//				Assert.hasText(config.outfilePath);
				/*OutfilePathFunc outFileNameFunc = config.outfilePathFunc==null?globalGeneratedConfig.getOutFileNameFunc():config.outfilePathFunc;
				Assert.notNull(outFileNameFunc, "no outFileNameFunc");
				String outfilePath = outFileNameFunc.getOutFileName(config);*/
				
				Map<String, Object> tableContext = globalConfig.getTableContexts().getContexts(config);
				if(tableContext!=null){
					globalConfig.put(TABLE_CONTEXT_KEY, tableContext);
				}
				String content = ftlGenerator.generateString(globalConfig.getRootContext(), config.templatePath);
				contents.add(content);
			});
			return new GeneratedResult<String>(tableName, contents);
		}

		public class TableGeneratedConfig {
			private String tableName;
			private String templatePath;
			private OutfilePathFunc outfilePathFunc;
			
			public TableGeneratedConfig(String tableName, String templatePath) {
				super();
				this.tableName = tableName;
				this.templatePath = templatePath;
			}

			public TableGeneratedConfig(String tableName, String templatePath, String outfilePath) {
				super();
				this.tableName = tableName;
				this.templatePath = templatePath;
				this.outfilePathFunc = config->outfilePath;
			}

			public TableGeneratedConfig outfilePath(String outfilePath) {
				this.outfilePathFunc = config->outfilePath;
				return this;
			}

			public TableGeneratedConfig outfilePathFunc(OutfilePathFunc outFileNameFunc) {
				this.outfilePathFunc = outFileNameFunc;
				return this;
			}

			public DbTableGenerator end() {
				return DbTableGenerator.this;
			}

			public String getTemplatePath() {
				return templatePath;
			}

			public OutfilePathFunc getOutFileNameFunc() {
				return outfilePathFunc;
			}

			public String getTableName() {
				return tableName;
			}

			public String tableNameStripStart(String stripChars) {
				if(StringUtils.isBlank(stripChars))
					return tableName;
				return org.apache.commons.lang3.StringUtils.stripStart(tableName.toLowerCase(), stripChars.toLowerCase());
			}
			
			public GlobalConfig globalGeneratedConfig(){
				return globalConfig;
			}
			
		}
	}
	
	
	
}
