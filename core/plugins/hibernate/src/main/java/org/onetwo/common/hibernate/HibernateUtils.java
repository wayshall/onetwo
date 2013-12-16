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
import org.onetwo.common.db.IBaseEntity;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.ReflectUtils.IgnoreAnnosCopyer;

public final class HibernateUtils {
	

	public static final Class<? extends Annotation>[] IGNORE_ANNO_CLASSES = new Class[]{ManyToOne.class, ManyToMany.class, OneToMany.class, OneToOne.class, Transient.class};
	public static final String[] TIMESTAMP_FIELDS = new String[]{"createTime", "lastUpdateTime"};
	public static final class HiberanteCopyer extends IgnoreAnnosCopyer {
		private String[] ignoreFields;
		
		public HiberanteCopyer() {
			super(null);
		}
		public HiberanteCopyer(Class<? extends Annotation>[] classes, String... ignoreFields) {
			super(classes);
			this.ignoreFields = ignoreFields;
		}

		@Override
		public void copy(Object source, Object target, PropertyDescriptor prop) {
			if(target instanceof IBaseEntity){
				if(ArrayUtils.contains(TIMESTAMP_FIELDS, prop.getName()))
					return ;
			}
			if(ArrayUtils.contains(ignoreFields, prop.getName())){
				return ;
			}
			super.copy(source, target, prop);
		}
		
		protected boolean isIgnoreValue(Object val){
			if(val==null)
				return true;
			return false;
		}
		
	};

	private static final HiberanteCopyer WITHOUT_RELATION = new HiberanteCopyer(IGNORE_ANNO_CLASSES);
	private static final HiberanteCopyer COMMON = new HiberanteCopyer(new Class[]{Transient.class});
	
	private static SessionFactory sessionFactory;
	
	public static void initSessionFactory(SessionFactory sessionFactory) {
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
		return getClassMeta(entityClass, false);
	}
	
	public static ClassMetadata getClassMeta(Class<?> entityClass, boolean throwIfNoteFound){
		ClassMetadata meta = sessionFactory.getClassMetadata(entityClass);
		if(meta==null && throwIfNoteFound)
			throw new BaseException("can not find the entity["+entityClass+"], check it please!");
		return meta;
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
	
	
	/*****
	 * 复制对象属性，但会忽略那些null值和配置了关系的属性
	 * @param source
	 * @param target
	 */
	public static <T> void copyWithoutRelations(T source, T target){
		ReflectUtils.getIntro(target.getClass()).copy(source, target, WITHOUT_RELATION);
	}
	public static <T> void copyIgnoreRelationsAndFields(T source, T target, String... ignoreFields){
		ReflectUtils.getIntro(target.getClass()).copy(source, target, new HiberanteCopyer(IGNORE_ANNO_CLASSES, ignoreFields));
	}
	/****
	 * 复制对象属性，但会忽略那些null值
	 * @param source
	 * @param target
	 */
	public static <T> void copy(T source, T target){
		ReflectUtils.getIntro(target.getClass()).copy(source, target, COMMON);
	}
	public static <T> void copyIgnore(T source, T target, String... ignoreFields){
		ReflectUtils.getIntro(target.getClass()).copy(source, target, new HiberanteCopyer(new Class[]{Transient.class}, ignoreFields));
	}
	
	/****
	 * 复制对象属性，但会忽略那些null值、空白字符和配置了关系的属性
	 * @param source
	 * @param targetClass
	 * @return
	 */
	public static <T> T copyWithoutRelations(T source, Class<T> targetClass){
		return ReflectUtils.copy(source, targetClass, WITHOUT_RELATION);
	}
	/***
	 * 复制对象属性，但会忽略那些null值、空白字符
	 * @param source
	 * @param targetClass
	 */
	public static <T> void copy(T source, Class<T> targetClass){
		ReflectUtils.copy(source, targetClass, COMMON);
	}
	
}
