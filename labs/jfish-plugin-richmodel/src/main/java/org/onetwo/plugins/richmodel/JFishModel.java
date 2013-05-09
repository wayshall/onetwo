package org.onetwo.plugins.richmodel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.fish.JFishQueryBuilder;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.validator.ValidationBindingResult;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.Page;

public class JFishModel {
	
	protected static RichModelEntityManager getJFishEntityManager(){
		return (RichModelEntityManager)SpringApplication.getInstance().getBaseEntityManager();
	}
	
	protected static ValidatorWrapper getValidator(){
		return SpringApplication.getInstance().getValidator();
	}
	
	public static Class<?> getEntityClass(){
		throw new NotImplementedYetException();
	}
	
	public static JFishQueryBuilder where(){
		throw new NotImplementedYetException();
	}
	
	public static JFishQueryBuilder query(){
		throw new NotImplementedYetException();
	}
	
	public static int removeAll(){
		throw new NotImplementedYetException();
	}
	
	public static int batchInsert(Collection<?> entities){
		throw new NotImplementedYetException();
	}
	
	public static Number count(Object...params){
		throw new NotImplementedYetException();
	}
	
	public static boolean hasRecord(Object...params){
		Number numb = count(params);
		if(numb!=null && numb.longValue()>0)
			return true;
		return false;
	}
	
	public static <T> List<T> findList(Object... properties){
		throw new NotImplementedYetException();
	}
	
	public static void findPage(Page<?> page, Object... properties){
		throw new NotImplementedYetException();
	}
	
	public static <T> T findById(Serializable id){
//		return getJFishEntityManager().findById(getEntityClass(), id);
		throw new NotImplementedYetException();
	}
	
	public static <T> T loadById(Serializable id){
//		return getJFishEntityManager().load(getEntityClass(), id);
		throw new NotImplementedYetException();
	}
	
	public static <T> T removeById(Serializable id){
//		return getJFishEntityManager().removeById(getEntityClass(), id);
		throw new NotImplementedYetException();
	}
	
	public static <T> T findOne(Object... properties){
		throw new NotImplementedYetException();
	}
	
	public static DataQuery createNamedQuery(String name){
//		return getJFishEntityManager().createNamedQuery(name);
		throw new NotImplementedYetException();
	}
	
	public static void findPageByQName(String queryName, Page<?> page, Object... properties){
//		getJFishEntityManager().findPageByQName(queryName, page, properties);
		throw new NotImplementedYetException();
	}
	
	public static <T> T findOneByQName(String queryName, Object... properties){
//		return getJFishEntityManager().findUniqueByQName(queryName, properties);
		throw new NotImplementedYetException();
	}
	
	public void save(){
		getJFishEntityManager().save(this);
	}
	
	protected void saveWith(String...relatedFields){
		getJFishEntityManager().saveWith(this, relatedFields);
	}
	
	public CascadeModel cascade(String relatedField){
		return getJFishEntityManager().createCascadeModel(this, relatedField);
	}
	
	public void remove(){
		getJFishEntityManager().remove(this);
	}
	
	protected ValidationBindingResult validate(Class<?>... groups){
		return getValidator().validate(this, groups);
	}
	
	protected void validateAndThrow(Class<?>... groups){
		getValidator().validateAndThrow(this, groups);
	}

	protected int saveRef(String... relatedFields){
		return getJFishEntityManager().saveRef(this, relatedFields);
	}
	protected int saveRef(boolean dropAllInFirst, String... relatedFields){
		return getJFishEntityManager().getJfishDao().saveRef(this, dropAllInFirst, relatedFields);
	}

	protected int dropRef(String... relatedFields){
		return getJFishEntityManager().getJfishDao().dropRef(this, relatedFields);
	}

	protected int clearRef(String... relatedFields){
		return getJFishEntityManager().clearRef(this, relatedFields);
	}
}
