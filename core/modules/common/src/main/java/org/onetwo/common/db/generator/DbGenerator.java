package org.onetwo.common.db.generator;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.onetwo.common.db.generator.dialet.DatabaseMetaDialet;
import org.onetwo.common.db.generator.dialet.MysqlMetaDialet;
import org.onetwo.common.db.generator.mapping.ColumnMapping;
import org.onetwo.common.db.generator.meta.TableMeta;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FileUtils;

import com.google.common.collect.Lists;

public class DbGenerator {
	
	public static DbGenerator newGenerator(DataSource dataSource){
		return new DbGenerator(dataSource);
	}

	private DataSource dataSource;
	private List<DbTableGenerator> tableGenerators = Lists.newArrayList();
	private DatabaseMetaDialet dialet;
	private TemplateEngine templateEngine;
	
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
	
	public ColumnMappingBuilder columnMapping(int sqlType){
		ColumnMapping mapping = this.dialet.getColumnMapping(sqlType);
		return new ColumnMappingBuilder(mapping);
	}

	public DbGenerator addTableDbGenerator(DbTableGenerator tableGenerator){
		tableGenerators.add(tableGenerator);
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

	public DbGenerator generateConfig(String templatePath, String outDir){
		tableGenerators.forEach(tg->{
			String prefix = tg.tableName.replace("_", "-");
			String outfilePath = outDir + "/"+ prefix + "-" + FileUtils.getFileNameWithoutExt(templatePath);
			tg.template(templatePath).outfilePath(outfilePath);
		});
		return this;
	}
	public DbGenerator generateConfig(String templatePath, OutFileNameFunc outFunc){
		tableGenerators.forEach(tg->{
			String outfilePath = outFunc.getOutFileName(tg, templatePath);
			tg.template(templatePath).outfilePath(outfilePath);
		});
		return this;
	}
	
	public List<GeneratedResult<File>> generate(GenerateContext context){
		templateEngine.afterPropertiesSet();
		Assert.notNull(dialet, "dialet can not be null!");
		List<GeneratedResult<File>> results = Lists.newArrayList();
		tableGenerators.forEach(table->{
			GeneratedResult<File> r = table.generate(dialet, templateEngine, context);
			results.add(r);
		});
		return results;
	}
	
	public List<GeneratedResult<String>> generateString(GenerateContext context){
		templateEngine.afterPropertiesSet();
		Assert.notNull(dialet, "dialet can not be null!");
		List<GeneratedResult<String>> results = Lists.newArrayList();
		tableGenerators.forEach(table->{
			GeneratedResult<String> r = table.generateString(dialet, templateEngine, context);
			results.add(r);
		});
		return results;
	}

	public static interface OutFileNameFunc {
		String getOutFileName(DbTableGenerator tg, String templatePath);
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
		private List<GeneratedConfig> generatedConfigs = Lists.newArrayList();
		

		public DbTableGenerator(String tableName) {
			super();
			this.tableName = tableName;
		}
		
		public DbTableGenerator addGeneratedConfig(String templatePath, String outfilePath){
			generatedConfigs.add(new GeneratedConfig(templatePath, outfilePath));
			return this;
		}
		
		public GeneratedConfig template(String templatePath){
			GeneratedConfig config = new GeneratedConfig(templatePath);
			generatedConfigs.add(config);
			return config;
		}
		
		public DbGenerator end(){
			return DbGenerator.this;
		}
		
		public String getTableName() {
			return tableName;
		}

		private GeneratedResult<File> generate(DatabaseMetaDialet dialet, TemplateEngine ftlGenerator, GenerateContext context){
			TableMeta tableMeta = dialet.getTableMeta(tableName);
			context.put("table", tableMeta);
			
			List<File> files = Lists.newArrayList();
			generatedConfigs.stream().forEach(config->{
				Assert.hasText(config.templatePath);
				Assert.hasText(config.outfilePath);
				File file = ftlGenerator.generateFile(context, config.templatePath, config.outfilePath);
				files.add(file);
			});
			return new GeneratedResult<File>(tableName, files);
		}

		private GeneratedResult<String> generateString(DatabaseMetaDialet dialet, TemplateEngine ftlGenerator, GenerateContext context){
			TableMeta tableMeta = dialet.getTableMeta(tableName);
			context.put("table", tableMeta);
			
			List<String> contents = Lists.newArrayList();
			generatedConfigs.stream().forEach(config->{
				Assert.hasText(config.templatePath);
				String content = ftlGenerator.generateString(context, config.templatePath);
				contents.add(content);
			});
			return new GeneratedResult<String>(tableName, contents);
		}

		public class GeneratedConfig {
			private String templatePath;
			private String outfilePath;
			
			public GeneratedConfig(String templatePath) {
				super();
				this.templatePath = templatePath;
			}

			public GeneratedConfig(String templatePath, String outfilePath) {
				super();
				this.templatePath = templatePath;
				this.outfilePath = outfilePath;
			}

			public DbTableGenerator outfilePath(String outfilePath) {
				this.outfilePath = outfilePath;
				return DbTableGenerator.this;
			}
			
		}
	}
}
