package org.onetwo.common.hibernate;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.tuple.StandardProperty;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

public final class HibernateUtils {
	

	private static SessionFactory sessionFactory;
	
	static void initSessionFactory(SessionFactory sessionFactory) {
		HibernateUtils.sessionFactory = sessionFactory;
	}

	public static void init(Object object){
		Hibernate.initialize(object);
	}

	public static ClassMetadata getClassMeta(Session s, Class<?> entityClass){
		return s.getSessionFactory().getClassMetadata(entityClass);
	}

	public static ClassMetadata getClassMeta(Session s, String entityClass){
		return s.getSessionFactory().getClassMetadata(entityClass);
	}
	
	public static ClassMetadata getClassMeta(String entityClass){
		return sessionFactory.getClassMetadata(entityClass);
	}
	
	public static ClassMetadata getClassMeta(Class<?> entityClass){
		return sessionFactory.getClassMetadata(entityClass);
	}
	
	public static boolean setPropertyState(StandardProperty[] props, Object[] currentState, String property, Object value){
		Assert.hasText(property);
		for (int i = 0; i < props.length; i++) {
			if (props[i].getName().equals(property)) {
				currentState[i] = value;
				return true;
			}
		}
		return false;
	}
	
	public static final Class<? extends Annotation>[] IGNORE_ANNO_CLASSES = new Class[]{ManyToOne.class, ManyToMany.class, OneToMany.class, OneToOne.class, Transient.class};
	
	public static <T> void copyWithoutRelations(T source, T target){
		ReflectUtils.copyIgnoreAnnotations(source, target, IGNORE_ANNO_CLASSES);
	}
}
