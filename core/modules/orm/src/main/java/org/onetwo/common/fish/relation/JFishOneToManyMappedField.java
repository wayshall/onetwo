package org.onetwo.common.fish.relation;

import java.util.Collection;

import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedFieldType;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.LangUtils;


public class JFishOneToManyMappedField extends AbstractCascadeMappedField implements CascadeCollectionMappedField {

//	private JoinColumnInfo joinColumnOfCascadeEntry;//on one side
//	private JFishMappedField joinFieldOfCascadeEntry;
	
//	private JoinTableInfo joinTable;
//	private SQLBuilder joinTableBuilder;
	
	public JFishOneToManyMappedField(JFishMappedEntry entry, JFishProperty prop) {
		super(entry, prop);
		if(!Collection.class.isAssignableFrom(prop.getType()))
			throw new JFishOrmException("the many side must be a collection type: " + prop.getType());
		setMappedFieldType(JFishMappedFieldType.ONE_TO_MANY);
	}
	


//	public AbstractMappedField getInverseReferencedField() {
//		AbstractMappedField relatedField = inverseFieldEntry.getFieldByColumnName(getInverseJoinColumn().getReferencedColumnName());
//		if(relatedField==null)
//			throw new JFishOrmException("there is not mapped field for referenced column : " + getInverseJoinColumn().getReferencedColumnName());
//		return relatedField;
//	}
//
//	public void setInverseJoinFieldValue(Object oppositeEntity, Object parentEntity) {
//		if(getMappedFieldType()==JFishMappedFieldType.FIELD){
//			Object fieldValue = this.getInverseReferencedField().getValue(parentEntity);
//			getInverseJoinField().setValue(oppositeEntity, fieldValue);
//		}else if(getMappedFieldType()==JFishMappedFieldType.TO_ONE){
//			getInverseJoinField().setValue(oppositeEntity, parentEntity);
//		}else{//many
//			throw new UnsupportedOperationException("unsupported mapped type : " + getMappedFieldType());
//		}
//	}

	
	/*public JFishMappedField getReferencedFieldOfJoinCloumn(JFishMappedEntry referencedFieldEntry){
		JFishMappedField referncedField = referencedFieldEntry.getFieldByColumnName(getJoinColumnOfCascadeEntry().getReferencedColumnName());
		if(referncedField==null)
			throw new JFishOrmException("can not find the JoinColumn reference field: " + getJoinColumnOfCascadeEntry().getReferencedColumnName());
		return referncedField;
	}*/
	
	public Collection<?> getValueAsCollection(Object entity){
		Collection<?> value = (Collection<?>)getValue(entity);
		return CUtils.emptyIfNull(value);
	}
	
	public FieldValueHolder getValueAsValueHolder(Object entity){
		Collection<?> value = getValueAsCollection(entity);
		if(LangUtils.isEmpty(value))
			return null;
		return new FieldValueHolder(value);
	}


	protected class FieldValueHolder {
		public Collection<Object> news;
		public Collection<Object> saveds;
		public FieldValueHolder(Collection<?> values) {
			news = CUtils.newArrayList(values.size());
			saveds = CUtils.newArrayList(values.size());
			for(Object obj : values.toArray()){
				if(getCascadeEntry().hasIdentifyValue(obj)){
					saveds.add(obj);
				}else{
					news.add(obj);
				}
			}
		}
		
		public boolean isEmpty(){
			return news.isEmpty() && saveds.isEmpty();
		}
		
	}

}
