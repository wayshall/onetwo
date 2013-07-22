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
	private CascadeMappedField mappedOneField;
	
	public JFishOneToManyMappedField(JFishMappedEntry entry, JFishProperty prop) {
		super(entry, prop);
		if(!Collection.class.isAssignableFrom(prop.getType()))
			throw new JFishOrmException("the many side must be a collection type: " + prop.getType());
		setMappedFieldType(JFishMappedFieldType.ONE_TO_MANY);
	}
	
	
	public CascadeMappedField getMappedOneField() {
		return mappedOneField;
	}

	public void setMappedOneField(CascadeMappedField mappedOneField) {
		this.mappedOneField = mappedOneField;
	}

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

	@Override
	public JoinColumnMapper getJoinColumnMapper() {
		JoinColumnMapper colMapper = super.getJoinColumnMapper();
		if(colMapper==null)
			colMapper = this.mappedOneField.getJoinColumnMapper();
		return colMapper;
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
