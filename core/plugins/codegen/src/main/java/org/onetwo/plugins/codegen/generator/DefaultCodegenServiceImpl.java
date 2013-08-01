package org.onetwo.plugins.codegen.generator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.list.JFishList;
import org.onetwo.plugins.codegen.db.TableInfo;

public class DefaultCodegenServiceImpl {

	private FreemarkerTemplate freemarkerTemplate;
	
	private DefaultTableManager tableManager;
	
	private Map<String, TemplateContextBuilder> builders = new HashMap<String, TemplateContextBuilder>();
	

	public DefaultCodegenServiceImpl(FreemarkerTemplate freemarkerTemplate, DefaultTableManager tableManager) {
		super();
		this.freemarkerTemplate = freemarkerTemplate;
		this.tableManager = tableManager;
	}

	public DefaultCodegenServiceImpl addContextBuilder(TemplateContextBuilder builder){
		this.builders.put(builder.getName(), builder);
		return this;
	}

	public JFishList<Object> generateTables(List<String> tables, GenContext context) {
		JFishList<Object> result = new JFishList<Object>();
		if(tables==null){
			result.add("no tables!");
			return result;
		}
		List<TableInfo> tableInfos = tableManager.getTables(tables);

		for(TableInfo table : tableInfos){
			List<Object> rs = this.generateFile(table, context);
			if(rs!=null){
				result.addAll(rs);
			}
		}
		return result;
//		openOutdir();
	}


	public List<Object> generateFile(String tableName, GenContext context) {
		return this.generateFile(tableManager.getTable(tableName), context);
	}

	public JFishList<Object> generateFile(TableInfo table, GenContext context) {
		if(this.builders==null || this.builders.isEmpty())
			return null;
		
		JFishList<Object> result = new JFishList<Object>();
		for(TemplateContextBuilder builder : this.builders.values()){
			try {
				File f = freemarkerTemplate.generateFile(builder.buildTemplateContext(table, context));
				result.add(f);
			} catch (Exception e) {
				e.printStackTrace();
				result.add(e.getMessage());
			}
		}
		return result;
	}
}
