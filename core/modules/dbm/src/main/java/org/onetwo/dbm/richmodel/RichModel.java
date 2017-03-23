package org.onetwo.dbm.richmodel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.core.BaseModel;
import org.onetwo.dbm.exception.NotImplementedDbmOperationException;

abstract public class RichModel<E, ID extends Serializable> extends BaseModel<E, ID> {

	protected static Class<?> obtainStaticEntityClass(){
		throw new NotImplementedDbmOperationException();
	}
	public static <T> int batchInsert(Collection<T> entities){
//		return Dbms.obtainCrudManager((Class<T>)obtainStaticEntityClass()).batchInsert(entities);
		throw new NotImplementedDbmOperationException();
	}
	
	public static Number count(Object...params){
//		return Dbms.obtainCrudManager(obtainStaticEntityClass()).countRecord(params);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> List<T> findList(Object... properties){
//		return (List<T>)Dbms.obtainCrudManager(obtainStaticEntityClass()).findListByProperties(properties);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> List<T> findListByExample(Object example){
//		return Dbms.obtainCrudManager((Class<T>)obtainStaticEntityClass()).findListByExample(example);
		throw new NotImplementedDbmOperationException();
	}

	public static <T> Page<T> findPageByExample(Page<T> page, Object example){
//		return Dbms.obtainCrudManager((Class<T>)obtainStaticEntityClass()).findPageByExample(page, example);
//		entityManager().findPage(getEntityClass(), page, properties);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> Page<T> findPage(Page<T> page, Object... properties){
//		return Dbms.obtainCrudManager((Class<T>)obtainStaticEntityClass()).findPage(page, properties);
//		entityManager().findPage(getEntityClass(), page, properties);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> T findById(Serializable id){
//		return (T)Dbms.obtainCrudManager(obtainStaticEntityClass()).findById(id);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> T loadById(Serializable id){
//		return (T)Dbms.obtainCrudManager(obtainStaticEntityClass()).load(id);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> Collection<T> removeByIds(Serializable[] ids){
//		return (Collection<T>)Dbms.obtainCrudManager(obtainStaticEntityClass()).removeByIds(ids);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> T removeById(Serializable id){
//		return (T)Dbms.obtainCrudManager(obtainStaticEntityClass()).removeById(id);
		throw new NotImplementedDbmOperationException();
	}
	
	public static <T> T findOne(Object... properties){
//		return (T)Dbms.obtainCrudManager(obtainStaticEntityClass()).findOne(properties);
//		return (T)entityManager().findOne(getEntityClass(), properties);
		throw new NotImplementedDbmOperationException();
	}
	
	protected static ValidatorWrapper getValidator(){
		return Springs.getInstance().getValidator();
	}
	
	public static int removeAll(){
//		return Dbms.obtainCrudManager(obtainStaticEntityClass()).removeAll();
//		return entityManager().removeAll(obtainStaticEntityClass());
		throw new NotImplementedDbmOperationException();
	}
	
	public static boolean exists(Object...params){
		Number numb = count(params);
		if(numb!=null && numb.longValue()>0)
			return true;
		return false;
	}
	
}
