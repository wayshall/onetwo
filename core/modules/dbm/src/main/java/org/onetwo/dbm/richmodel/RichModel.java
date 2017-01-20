package org.onetwo.dbm.richmodel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Transient;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.builder.WhereCauseBuilder;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.support.Dbms;

@SuppressWarnings("unchecked")
abstract public class RichModel<E, ID extends Serializable> {
	
	public static Class<?> getEntityClass(){
		throw new NotImplementedYetException();
	}
	public static int batchInsert(Collection<?> entities){
//		return ((DbmDao)entityManager().getRawManagerObject()).batchInsert(entities);
		throw new NotImplementedYetException();
	}
	
	public static Number count(Object...params){
//		return entityManager().countRecord(getEntityClass(), params);
		throw new NotImplementedYetException();
	}
	
	public static <T> List<T> findList(Object... properties){
//		return (List<T>)entityManager().findList(getEntityClass(), properties);
		throw new NotImplementedYetException();
	}
	
	public static void findPage(Page<?> page, Object... properties){
//		entityManager().findPage(getEntityClass(), page, properties);
		throw new NotImplementedYetException();
	}
	
	public static <T> T findById(Serializable id){
//		return (T)entityManager().findById(getEntityClass(), id);
		throw new NotImplementedYetException();
	}
	
	public static <T> T loadById(Serializable id){
//		return (T)entityManager().load(getEntityClass(), id);
		throw new NotImplementedYetException();
	}
	
	public static <T> T removeById(Serializable id){
//		return (T)entityManager().removeById(getEntityClass(), id);
		throw new NotImplementedYetException();
	}
	
	public static <T> T findOne(Object... properties){
//		return (T)entityManager().findOne(getEntityClass(), properties);
		throw new NotImplementedYetException();
	}

	public static WhereCauseBuilder where(){
		return Querys.from(entityManager(), getEntityClass()).where();
	}

	protected static BaseEntityManager entityManager(){
		return Dbms.obtainBaseEntityManager();
	}
	
	protected static ValidatorWrapper getValidator(){
		return SpringApplication.getInstance().getValidator();
	}
	
	public static int removeAll(){
		return entityManager().removeAll(getEntityClass());
	}
	
	public static boolean exists(Object...params){
		Number numb = count(params);
		if(numb!=null && numb.longValue()>0)
			return true;
		return false;
	}

	@Transient
	private Class<E> entityClass;
	
	public RichModel() {
		this.entityClass = (Class<E>)ReflectUtils.getSuperClassGenricType(this.getClass(), null);
	}
	
	public void save(){
		entityManager().save(this);
	}
	
	public void remove(){
		entityManager().remove(this);
	}
}
