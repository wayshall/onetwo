package org.onetwo.common.hibernate;

import java.util.Collection;

import org.onetwo.common.db.IBaseEntity;
import org.onetwo.common.db.IdEntity;

@SuppressWarnings("unchecked")
public abstract class HibernateUtils {

	public static void init(Object object){
		if(object instanceof IBaseEntity){
			((IBaseEntity)object).getLastUpdateTime();
		}
		else if(object instanceof Collection){
			((Collection)object).size();
		} 
		else if(object instanceof IdEntity) {
			((IdEntity)object).getId();
		}
	}
}
