package org.onetwo.common.hibernate;

import org.hibernate.Hibernate;

public abstract class HibernateUtils {

	public static void init(Object object){
		Hibernate.initialize(object);
	}
}
