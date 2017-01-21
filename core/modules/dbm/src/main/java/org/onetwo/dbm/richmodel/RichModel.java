package org.onetwo.dbm.richmodel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.onetwo.common.db.builder.WhereCauseBuilder;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.exception.NotImplementedDbmOperationException;
import org.onetwo.dbm.support.BaseModel;

abstract public class RichModel<E, ID extends Serializable> extends BaseModel<E, ID> {
	
	protected static Class<?> obtainStaticEntityClass(){
		throw new NotImplementedDbmOperationException();
	}
	public static int batchInsert(Collection<?> entities){
//		return ((DbmDao)entityManager().getRawManagerObject()).batchInsert(entities);
		throw new NotImplementedDbmOperationException();
	}
	
	public static Number count(Object...params){
//		return entityManager().countRecord(getEntityClass(), params);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> List<T> findList(Object... properties){
//		return (List<T>)entityManager().findList(getEntityClass(), properties);
		throw new NotImplementedDbmOperationException();
	}
	
	public static void findPage(Page<?> page, Object... properties){
//		entityManager().findPage(getEntityClass(), page, properties);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> T findById(Serializable id){
//		return (T)entityManager().findById(getEntityClass(), id);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> T loadById(Serializable id){
//		return (T)entityManager().load(getEntityClass(), id);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> T removeById(Serializable id){
//		return (T)entityManager().removeById(getEntityClass(), id);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> T findOne(Object... properties){
//		return (T)entityManager().findOne(getEntityClass(), properties);
		throw new NotImplementedDbmOperationException();
	}

	public static WhereCauseBuilder where(){
//		return Querys.from(entityManager(), getEntityClass()).where();
		throw new NotImplementedDbmOperationException();
	}
	
	protected static ValidatorWrapper getValidator(){
		return Springs.getInstance().getValidator();
	}
	
	public static int removeAll(){
		return entityManager().removeAll(obtainStaticEntityClass());
	}
	
	public static boolean exists(Object...params){
		Number numb = count(params);
		if(numb!=null && numb.longValue()>0)
			return true;
		return false;
	}
	
}
