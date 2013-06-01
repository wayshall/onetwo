package org.onetwo.plugins.richmodel;

import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.fish.JFishEntityManagerImpl;
import org.onetwo.common.fish.JFishQueryBuilder;
import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedField;
import org.onetwo.common.fish.relation.JFishOneToManyMappedField;
import org.onetwo.common.fish.relation.JFishToOneMappedField;
import org.onetwo.common.fish.relation.JoinTableInfo;
import org.onetwo.common.utils.LangUtils;

public class JFishRichModelEntityManagerImpl extends JFishEntityManagerImpl implements RichModelEntityManager {

	/******
	 * 
	 * @param entity 主表实体
	 * @param relatedClass 关联表实体类
	 * @param relatedMappedFields 关联字段映射，主表实体字段名称在前
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JFishQueryBuilder createCascadeQueryBuilder(Object entity, Class<?> relatedClass, String... relatedMappedFields){
		Map<String, String> mappedFields = (Map<String, String>)LangUtils.asMap((Object[])relatedMappedFields);
		JFishMappedEntry entry = this.getJfishDao().getMappedEntryManager().getEntry(entity);
		JFishQueryBuilder qb = this.createQueryBuilder(relatedClass);
		for(Entry<String, String> mapField : mappedFields.entrySet()){
			JFishMappedField mainEntityField = entry.getField(mapField.getKey());
			qb.field(mapField.getValue()).equalTo(mainEntityField.getColumnValue(entity));
		}
		return qb;
	}
	
	public CascadeModel createCascadeModel(Object entity, String relatedField){
		JFishMappedEntry entry = this.getJfishDao().getMappedEntryManager().getEntry(entity);
		JFishMappedField field = entry.getField(relatedField);
		JFishQueryBuilder qb = null;
		if(JFishOneToManyMappedField.class.isInstance(field)){
			JFishOneToManyMappedField many = (JFishOneToManyMappedField) field;
			if(many.isJoinTableField()){
//				throw new JFishOrmException("JoinTable field can not create a cascade query builder : " + field);
				qb = this.createQueryBuilder(many.getCascadeEntry().getEntityClass());
				JoinTableInfo jt = many.getJoinTableMapper().getJoinTable();
				String ref_alias = "t_ref";
				qb.leftJoin(jt.getTable(), ref_alias).on(jt.getInverseJoinColumn().getReferencedColumnName(), ref_alias+"."+jt.getInverseJoinColumn().getName());
				String inverse_alias = "t_inverse";
				String refTableInverseField = ref_alias+"."+jt.getJoinColum().getName();
				qb.leftJoin(entry.getTableInfo().getName(), inverse_alias).on(inverse_alias+"."+jt.getJoinColum().getReferencedColumnName(), refTableInverseField);
				qb.field(refTableInverseField).equalTo(many.getJoinTableMapper().getReferenceFieldOfJoinColumn().getColumnValue(entity));
			}else{
				qb = this.createCascadeQueryBuilder(entity, many.getCascadeEntry().getEntityClass(), many.getJoinColumnMapper().getReferencedField().getName(), many.getJoinColumnMapper().getJoinField().getName());
			}
		}else if(JFishToOneMappedField.class.isInstance(field)){
			if(field.isJoinTableField()){
				throw new JFishOrmException("JoinTable field can not create a cascade query builder : " + field);
			}else{
				JFishToOneMappedField one = (JFishToOneMappedField) field;
				qb = this.createCascadeQueryBuilder(entity, one.getCascadeEntry().getEntityClass(), one.getName(), one.getJoinColumnMapper().getReferencedColumnName());
			}
		}else{
			throw new JFishOrmException("this field can not create a cascade query builder : " + field);
		}
		
		CascadeModel cascade = new CascadeModel(qb);
		return cascade;
	}
}
