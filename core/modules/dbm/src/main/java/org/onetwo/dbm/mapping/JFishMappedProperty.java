package org.onetwo.dbm.mapping;

import javax.persistence.Enumerated;

import org.onetwo.common.utils.JFishProperty;


public class JFishMappedProperty extends AbstractMappedField {
	
	private DbmEnumType enumType;
	
	public JFishMappedProperty(DbmMappedEntry entry, JFishProperty prop){
		super(entry, prop);
		if(prop.hasAnnotation(Enumerated.class)){
			Enumerated enumerated = prop.getAnnotation(Enumerated.class);
			this.enumType = DbmEnumType.valueOf(enumerated.value().name());
		}
	}

	@Override
	public boolean isEnumerated() {
		return enumType!=null;
	}

	public DbmEnumType getEnumType() {
		return enumType;
	}
	
}
