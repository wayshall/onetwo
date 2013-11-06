package org.onetwo.common.spring.sql;

import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;

public class JFishNamedFileQueryInfo extends NamespaceProperty {
	public static final String COUNT_POSTFIX = "-count";

	public static boolean isCountName(String name){
		return name.endsWith(COUNT_POSTFIX);
	}
	public static String trimCountPostfix(String name){
		if(!isCountName(name))
			return name;
		return name.substring(0, name.length() - COUNT_POSTFIX.length());
	}
	
	private String mappedEntity;
	private String countSql;
	private FileSqlParserType parser = FileSqlParserType.NONE;
	
	
	private Class<?> mappedEntityClass;


	public String getSql() {
		return getValue();
	}
	
	public String getCountName(){
		return getFullName() + COUNT_POSTFIX;
	}

	public void setSql(String sql) {
		this.setValue(sql);
	}

	public String getMappedEntity() {
		return mappedEntity;
	}

	public Class<?> getMappedEntityClass() {
		return mappedEntityClass;
	}

	public void setMappedEntity(String mappedEntity) {
		this.mappedEntity = mappedEntity;
		if(StringUtils.isNotBlank(mappedEntity)){
			this.mappedEntityClass = ReflectUtils.loadClass(mappedEntity);
		}
	}

	public String getCountSql() {
		if(StringUtils.isBlank(countSql)){
			this.countSql = ExtQueryUtils.buildCountSql(this.getSql(), "");
		}
		return countSql;
	}

	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}

	public boolean isIgnoreNull() {
		return parser==FileSqlParserType.IGNORENULL;
	}

	
	public FileSqlParserType getFileSqlParserType() {
		return parser;
	}

	public void setParser(String parser) {
		this.parser = FileSqlParserType.valueOf(parser.toUpperCase());
	}
/*
	public boolean isNeedParseSql(){
		return isIgnoreNull();
	}*/

	public String toString() {
		return LangUtils.append("{namespace:, ", getNamespace(), ", name:", getName(), ", mappedEntity:", mappedEntity, ", sql:", getSql(), ", countSql:", getCountSql(), "}");
	}
}
