package org.onetwo.common.fish.relation;

import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedField;
import org.onetwo.common.fish.orm.JFishMappedFieldType;
import org.onetwo.common.utils.JFishProperty;


public class JFishToOneMappedField extends AbstractCascadeMappedField implements CascadeMappedField {

//	private Collection<JoinColumnInfo> joinColumns = new HashSet<JoinColumnInfo>();

	
	public JFishToOneMappedField(JFishMappedEntry entry, JFishProperty prop) {
		super(entry, prop, null, null);
		setMappedFieldType(JFishMappedFieldType.TO_ONE);
	}
	
	
	/*public void addJoinColumn(String table, String name, String referencedColumnName){
		JoinColumnInfo joinCol = new JoinColumnInfo(table, name, referencedColumnName, JFishRelation.ONE_TO_MANY);
		addJoinColumn(joinCol);
	}
	public void addJoinColumn(JoinColumnInfo joinCol){
		this.joinColumns.add(joinCol);
	}*/

	public void setColumnValue(Object entity, Object value){
		Object relatedValue = getPropertyInfo().getValue(entity);
		if(relatedValue==null){
			relatedValue = getPropertyInfo().getTypeClassWrapper().newInstance();
			getPropertyInfo().setValue(entity, relatedValue);
		}
		JFishMappedField relatedField = getReferencedField();
		relatedField.setValue(relatedValue, value);
	}
	
	public Object getColumnValue(Object entity){
		Object relatedValue = getPropertyInfo().getValue(entity);
		if(relatedValue==null)
			return null;
		JFishMappedField relatedField = getReferencedField();
		return relatedField.getValue(relatedValue);
	}
	

	public Class<?> getColumnType(){
		return getReferencedField().getColumnType();
	}
	
	protected JFishMappedField getReferencedField(){
		return isJoinTableField()?getJoinTableMapper().getReferencedFieldOfInverseJoinCloumn():getJoinColumnMapper().getReferencedField();
	}

	/*public void setValue(Object entity, Object value){
		propertyInfo.setValue(entity, value);
	}
	
	public Object getValue(Object entity){
		return propertyInfo.getValue(entity);
	}*/

}
