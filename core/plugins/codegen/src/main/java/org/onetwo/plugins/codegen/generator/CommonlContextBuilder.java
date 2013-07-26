package org.onetwo.plugins.codegen.generator;

import java.util.Map;
import java.util.Set;

import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.codegen.db.SqlTypeFactory.DataBase;
import org.onetwo.plugins.codegen.db.TableInfo;

public class CommonlContextBuilder extends AbstractBuilder {
	
	
	
	private String fileNamePostfix;
	private String filePostfix;
	
	private String basePackage;
	private String archetypePackage;
	private String moduleName;
	private String selfPackage;
	private String template;
	
	private DataBase dataBase;
	private Expression exp = Expression.DOLOR;
	private String generateOutDir;
	
//	private boolean outFileNameCapitalize;
	
	public CommonlContextBuilder(DataBase dataBase){
		super("common");
		this.dataBase = dataBase;
	}
	

	public void setSelfPackage(String selfPackage) {
		if(selfPackage.startsWith(".")){
			selfPackage = selfPackage.substring(1);
		}
		this.selfPackage = selfPackage;
	}
	
	protected String getFullPackage(GenContext ctx){
		String fp = getModulePackage();
		if(StringUtils.isNotBlank(selfPackage)){
			fp += "." +selfPackage;
		}
		return fp;
	}
	
	protected String getModulePackage(){
		String fp = getBasePackage();
		if(StringUtils.isNotBlank(getArchetypePackage())){
			fp += "." + getArchetypePackage();
		}
		if(StringUtils.isNotBlank(getModuleName())){
			fp += "." +getModuleName();
		}
		return fp;
	}
	

	
	public String dheader(String header){
		return "<@"+header;
	}
	
	public String dtail(String tail){
		return "</@"+tail+">";
	}

	@Override
	public TemplateContext buildTheTemplateContext(final TableInfo table, final Map<String, Object> context, final GenContext ctx) {
		if(table.getPrimaryKey()==null){
			System.err.println("[" + table.getName() + "] no table primaryKey. service implements igonre generated file.");
			return null;
		}

		String fullPackage = getFullPackage(ctx);
		Set<String> classes = GenerateUtils.importClasses(table);
		
		String commonName = ctx.getClassName(table);
		
		context.put("dataBase", dataBase);
		context.put("table", table);
		context.put("builder", this);
		context.put("importClasses", classes);
		context.put("moduleName", moduleName);
		context.put("moduleRequestPath", StringUtils.isBlank(moduleName)?"":"/"+moduleName.replace('.', '/'));
		context.put("commonName", commonName);
		context.put("commonName_uncapitalize", StringUtils.uncapitalize(commonName));
		context.put("commonName_underLine", StringUtils.convert2UnderLineName(commonName));
		context.put("commonName_partingLine", StringUtils.convert2UnderLineName(commonName, "-"));

		context.put("table_class_name", commonName);
		context.put("table_name_no_prefix", ctx.getTableNameNoPrefix(table));

		context.put("selfPackage", selfPackage);
		context.put("commonPackage", basePackage);
		context.put("basePackage", basePackage);
		context.put("modulePackage", getModulePackage());
		context.put("fullPackage", fullPackage);
		String selfFileName = commonName + getFileNamePostfix();
		context.put("selfClassName", selfFileName);
		context.put("selfFileName", selfFileName);
		
		
		return new TemplateContext(){

			@Override
			public Object get(String key) {
				return context.get(key)==null?"":context.get(key);
			}

			@Override
			public Map getContext() {
				return context;
			}

			@Override
			public String getOutfile() {
				return getOutfilePath(context);
			}

			@Override
			public String getTemplate() {
				return template;
			}
			
		};
	}
	public String getOutfilePath(Map<String, Object> context){
		String fullPackage = (String)context.get("fullPackage");
		String selfFileName = (String)context.get("selfFileName");
		
		String baseDir = getGenerateOutDir();
		if(baseDir==null){
			if(getFilePostfix().equalsIgnoreCase("java")){
				 baseDir = FileUtils.getMavenProjectDir().getPath() + "/src/main/java/";
			}else if(getFilePostfix().equalsIgnoreCase("ftl")){
				 baseDir = FileUtils.getMavenProjectDir().getPath() + "/src/main/webapp/WEB-INF/ftl/";
				 fullPackage = getModuleName();
				 selfFileName = StringUtils.convert2UnderLineName(selfFileName, "-");
			}else{
				baseDir = "/";
			}
		}
		String outfileName = "";
		outfileName = baseDir + "/" + fullPackage.replace('.', '/') + "/" + selfFileName + "." + getFilePostfix();;
		outfileName = outfileName.replace("//", "/");
		return outfileName;
	}
	

	


	public String getFileNamePostfix() {
		return fileNamePostfix;
	}


	public void setFileNamePostfix(String fileNamePostfix) {
		this.fileNamePostfix = fileNamePostfix;
	}


	public String getFilePostfix() {
		return filePostfix;
	}

	public void setFilePostfix(String filePostfix) {
		this.filePostfix = filePostfix;
	}

	public void setTemplate(String template) {
		this.template = template;
	}


	public String getBasePackage() {
		return basePackage;
	}


	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}


	public String getGenerateOutDir() {
		return generateOutDir;
	}


	public void setGenerateOutDir(String generateOutDir) {
		this.generateOutDir = generateOutDir;
	}


	public String getModuleName() {
		return moduleName;
	}


	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}


	public String getArchetypePackage() {
		return archetypePackage;
	}


	public void setArchetypePackage(String archetypePackage) {
		this.archetypePackage = archetypePackage;
	}

	
}
