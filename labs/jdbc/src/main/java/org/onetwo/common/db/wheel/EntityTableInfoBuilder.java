package org.onetwo.common.db.wheel;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("rawtypes")
public class EntityTableInfoBuilder implements TableInfoBuilder {
	public static class TMeta {
		public static final String column_prefix = ":";
		public static final String meta_prefix = "#";
		public static final String table = "#table";
		public static final String entity_meta = "#entity_meta";
		public static final String pk = "#pk";
		public static final String use_keys_as_fields = "#use_keys_as_fields";
		public static final String seq_name = "#seq-name";
	}
	
	private Map<String, TableInfo> tableInfos = new HashMap<String, TableInfo>();
	private boolean cacheTableInfo;
	
	
	public EntityTableInfoBuilder(){
	}
	
	public EntityTableInfoBuilder(boolean cacheTableInfo) {
		super();
		this.cacheTableInfo = cacheTableInfo;
	}
	

	public boolean isCacheTableInfo() {
		return cacheTableInfo;
	}


	@Override
	public TableInfo buildTableInfo(Object object) {
		TableInfo tableInfo = getFormCache(object.getClass().getName());
		if(tableInfo!=null)
			return tableInfo;
		
		if(object instanceof Map){
			Map mapEntity = (Map)object;
//			boolean removeMeta = true;
			if(mapEntity.containsKey(TMeta.entity_meta)){
				mapEntity = (Map)mapEntity.get(TMeta.entity_meta);
//				removeMeta = false;
			}
			tableInfo = _buildTableInfoByMap(mapEntity);
		}else if(object instanceof Class){
			Class entityClass = (Class<?>)object;
			tableInfo = _buildTableInfo(entityClass);
		}else{
			Class entityClass;
			if(object instanceof Class){
				entityClass = (Class<?>)object;
			}else{
				entityClass = LangUtils.getFirst(object).getClass();
			}
			if(isCacheTableInfo()){
				tableInfo = getFormCache(entityClass.getName());
				if(tableInfo!=null)
					return tableInfo;
			}
			tableInfo = _buildTableInfo(entityClass);
		}
		if(tableInfo==null)
			LangUtils.throwBaseException("no tableinfo created : "+object);
		putIntoCache(tableInfo);
		return tableInfo;
	}

	protected TableInfo getFormCache(String entityName){
		return tableInfos.get(entityName);
	}
	protected void putIntoCache(TableInfo tableInfo){
		tableInfos.put(tableInfo.getEntityClass().getName(), tableInfo);
	}
	protected Object getValueFromMapEntity(Map entity, String key, boolean remove){
		if(!entity.containsKey(key))
			return null;
		Object val = entity.get(key);
		if(remove)
			entity.remove(key);
		return val;
	}
	
	protected TableInfo _buildTableInfoByMap(Map entity){
		TableInfo tableInfo = new TableInfo(entity.getClass(), "");
		
		/*boolean removeMeta = entity.containsKey(TMeta.use_keys_as_fields);
		if(removeMeta){
			entity.remove(TMeta.use_keys_as_fields);
		}*/
		boolean removeMeta = false;
		
		String table = (String) getValueFromMapEntity(entity, TMeta.table, removeMeta);
		String idName = (String) getValueFromMapEntity(entity, TMeta.pk, removeMeta);
		String seqName = (String) getValueFromMapEntity(entity, TMeta.seq_name, removeMeta);
		Assert.hasText(table, "table can not be null, entity:" + entity);
		Assert.hasText(idName, "id can not be null, entity:" + entity);
		
		tableInfo.setName(table);
		
		String k = null;
		Object v = null;
		Set<String> keys = new HashSet<String>(entity.keySet());
		for(String key : keys){
			k = key;
			v = entity.get(key);
			
			if(k.startsWith(TMeta.meta_prefix))
				continue;
			
			if(k.startsWith(TMeta.column_prefix))
				k = k.substring(TMeta.column_prefix.length());
			
			ColumnInfo column = new ColumnInfo(StringUtils.convert2UnderLineName(k));
			column.setJavaName(k);
			
			if(v instanceof Class){
				column.setJavaType((Class)v);
			}else if(v!=null){
//				column.setJavaType(v.getClass());
			}
			
//			tableInfo.column(StringUtils.convert2UnderLineName(k), k);
			tableInfo.addColumn(column);
			if(removeMeta){
//					entity.remove(key);
//					entity.put(k, entity.remove(key));
			}
			
		}
		tableInfo.id(idName, true);
		if(StringUtils.isNotBlank(seqName))
			tableInfo.setSeqName(seqName);
		
		return tableInfo;
	}
	
	protected TableInfo _buildTableInfo(Class<?> entityClass) {
		TableInfo tableInfo = new TableInfo(entityClass, null);
		Collection<String> fieldNames = ReflectUtils.desribPropertiesName(entityClass);
		for(String fieldName : fieldNames){
			tableInfo.column(fieldName);
		}
		Field id = ReflectUtils.findField(entityClass, "id");
		if(id!=null){
			tableInfo.id(id.getName());
		}
		return tableInfo;
	}

}
