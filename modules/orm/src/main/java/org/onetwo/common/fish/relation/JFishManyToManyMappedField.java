package org.onetwo.common.fish.relation;

import java.util.Collection;

import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedFieldType;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.LangUtils;


public class JFishManyToManyMappedField extends JFishOneToManyMappedField implements CascadeCollectionMappedField {


	public JFishManyToManyMappedField(JFishMappedEntry entry, JFishProperty prop) {
		super(entry, prop);
		if(!Collection.class.isAssignableFrom(prop.getType()))
			throw new JFishOrmException("the many side must be a collection type: " + prop.getType());
		setMappedFieldType(JFishMappedFieldType.MANY_TO_MANY);
	}
	

	
	public Collection<?> getUpdateValues(Object entity){
		Collection<?> value = getValueAsCollection(entity);
		if(LangUtils.isEmpty(value))
			return value;
		return CUtils.emptyIfNull(value);
	}

}
