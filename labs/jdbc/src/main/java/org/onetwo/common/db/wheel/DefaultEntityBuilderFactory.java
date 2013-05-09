package org.onetwo.common.db.wheel;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;

public class DefaultEntityBuilderFactory implements EntityBuilderFactory, WheelAware {
	
	private TableInfoBuilder tableInfoBuilder;
	private SQLBuilderFactory sqlBuilderFactory;
	
	private Wheel wheel;
	
	private Map<String, EntityBuilder> builders = new HashMap<String, EntityBuilder>();
	
	public DefaultEntityBuilderFactory(TableInfoBuilder tableInfoBuilder, SQLBuilderFactory sqlBuilderFactory){
		this.tableInfoBuilder = tableInfoBuilder;
		this.sqlBuilderFactory = sqlBuilderFactory;
	}
	
	protected EntityBuilder getFormCache(String entityName){
		return builders.get(entityName);
	}
	
	protected void putIntoCache(EntityBuilder builder){
		builders.put(builder.getTableInfo().getEntityName(), builder);
	}
	
	public EntityBuilder create(Object entityClass){
		TableInfo tableInfo = tableInfoBuilder.buildTableInfo(entityClass);
		if(tableInfo==null)
			LangUtils.throwBaseException("can not build table info for " + entityClass);
		return create(tableInfo);
	}
	
	public EntityBuilder create(TableInfo tableInfo){
		EntityBuilder eb = null;
		if(wheel.config().isProduct()){
			eb = getFormCache(tableInfo.getEntityName());
			if(eb!=null)
				return eb;
		}
		eb = newEntityBuilder(tableInfo);
		eb.setSQLBuilderFactory(sqlBuilderFactory);
		putIntoCache(eb);
		return eb;
	}
	
	protected EntityBuilder newEntityBuilder(TableInfo tableInfo){
		return new DefaultEntityBuilder(tableInfo);
	}

	public Wheel getWheel() {
		return wheel;
	}

	public void setWheel(Wheel wheel) {
		this.wheel = wheel;
	}
}
