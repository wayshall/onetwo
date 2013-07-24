package org.onetwo.plugins.codegen.generator;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.utils.FileUtils;
import org.onetwo.plugins.codegen.db.TableInfo;


abstract public class AbstractBuilder implements TemplateContextBuilder {

	private String name;
	
	public AbstractBuilder(String name){
		this.name = name;
	}

	@Override
	public TemplateContext buildTemplateContext(final TableInfo table, final GenContext ctx) {
		table.setPrefix(ctx.getTablePrefix());
		final Map<String, Object> context = new HashMap<String, Object>();
		if(ctx.getContext()!=null)
			context.putAll(ctx.getContext());
		context.put("table", table);
		TemplateContext templateConext = buildTheTemplateContext(table, context, ctx);
		return templateConext;
	}
	
	abstract protected TemplateContext buildTheTemplateContext(final TableInfo table, Map<String, Object> context, final GenContext ctx);
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String[] args){
		String templateFile = "service_Service.java";
		System.out.println(FileUtils.getFileNameWithoutExt(templateFile));
		System.out.println(FileUtils.getExtendName(templateFile));
	}

}
