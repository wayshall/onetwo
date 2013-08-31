package org.onetwo.common.spring.sql;

import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;

public class JFishNamedFileQueryInfo extends NamespaceProperty {
	private String mappedEntity;
	private String countSql;
	private boolean ignoreNull;
	
	private Class<?> mappedEntityClass;


	public String getSql() {
		return getValue();
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
		return ignoreNull;
	}

	public void setIgnoreNull(boolean ignoreNull) {
		this.ignoreNull = ignoreNull;
	}
	
	public boolean isNeedParseSql(){
		return isIgnoreNull();
	}

	public String toString() {
		return LangUtils.append("{namespace:, ", getNamespace(), ", name:", getName(), ", mappedEntity:", mappedEntity, ", sql:", getSql(), ", countSql:", getCountSql(), "}");
	}
}
