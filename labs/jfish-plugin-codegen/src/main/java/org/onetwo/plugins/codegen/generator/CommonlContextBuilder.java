package org.onetwo.plugins.codegen.generator;

import java.util.Map;
import java.util.Set;

import org.onetwo.common.fish.orm.TableInfo;
import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class CommonlContextBuilder extends AbstractBuilder {
	
	
	private Expression exp = Expression.DOLOR;
	
	private String fileNamePostfix;
	private String filePostfix;
	
	private String selfPackage;
	private String template;
	
	private boolean outFileNameCapitalize;
	
	public CommonlContextBuilder(){
		super("common");
	}
	
	public boolean isFileNameExpr(){
		return exp.isExpresstion(fileNamePostfix);
	}
	

	public void setSelfPackage(String selfPackage) {
		if(selfPackage.startsWith(".")){
			selfPackage = selfPackage.substring(1);
		}
		this.selfPackage = selfPackage;
	}
	
	protected String getFullPackage(GenContext ctx){
		String fp = ctx.getBasePackage()+"."+selfPackage;
		return fp;
	}
	

	public void setOutFileNameCapitalize(boolean outFileNameCapitalize) {
		this.outFileNameCapitalize = outFileNameCapitalize;
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
		String selfClassName = ctx.getClassName(table)+this.getFileNamePostfix();
		
		context.put("builder", this);
		context.put("importClasses", classes);
		context.put("commonName", commonName);
		context.put("commonName_uncapitalize", StringUtils.uncapitalize(commonName));
		context.put("commonName_underLine", StringUtils.convert2UnderLineName(commonName));

		context.put("table_class_name", commonName);
		context.put("table_name_no_prefix", ctx.getTableNameNoPrefix(table));

		context.put("selfClassName", selfClassName);
		context.put("selfPackage", selfPackage);
		context.put("commonPackage", ctx.getBasePackage());
		context.put("basePackage", ctx.getBasePackage());
		context.put("fullPackage", fullPackage);
		
		String outfileName = null;
		if(isFileNameExpr()){
			outfileName = exp.parseByProvider(fileNamePostfix, context);
		}else{
			outfileName = selfClassName;
			if(outFileNameCapitalize){
				outfileName = StringUtils.capitalize(outfileName);
			}else{
				outfileName = StringUtils.uncapitalize(outfileName);
			}
		}
		
		outfileName = ctx.getGenerateOutDir() + "/" + fullPackage.replace('.', '/') + "/" + outfileName + "." + getFilePostfix();;
		final String outfile = outfileName.replace("//", "/");
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
				return outfile;
			}

			@Override
			public String getTemplate() {
				return template;
			}
			
		};
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

	
}
