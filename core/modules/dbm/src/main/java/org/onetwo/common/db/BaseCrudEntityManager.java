package org.onetwo.common.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.onetwo.common.db.builder.QueryBuilder;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.Page;
import org.slf4j.Logger;

@SuppressWarnings({"unchecked"})
public class BaseCrudEntityManager<T, PK extends Serializable> implements CrudEntityManager<T, PK> {
 
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected Class<T> entityClass;
	protected BaseEntityManager baseEntityManager;

	public BaseCrudEntityManager(Class<T> entityClass){
		this(entityClass, SpringApplication.getInstance().getBean(BaseEntityManager.class));
	}
	public BaseCrudEntityManager(Class<T> entityClass, BaseEntityManager baseEntityManager){
		if(entityClass==null){
			this.entityClass = (Class<T>)ReflectUtils.getSuperClassGenricType(this.getClass(), BaseCrudEntityManager.class);
		}else{
			this.entityClass = entityClass;
		}
		this.baseEntityManager = baseEntityManager;
	}
	protected BaseCrudEntityManager(BaseEntityManager baseEntityManager){
		this((Class<T>)null);
		this.baseEntityManager = baseEntityManager;
	}

	public BaseEntityManager getBaseEntityManager() {
		BaseEntityManager bem = this.baseEntityManager;
		if(bem==null){
			bem = SpringApplication.getInstance().getBean(BaseEntityManager.class);
			if(this.baseEntityManager==null){
				this.baseEntityManager = bem;
			}
		}
		return bem;
	}

	@Override
	public T findById(PK id) {
		return (T)getBaseEntityManager().findById(entityClass, id);
	}
	
	@Override
	public Optional<T> findOptionalById(PK id) {
		return Optional.ofNullable(getBaseEntityManager().findById(entityClass, id));
	}


	@Override
	public Number countRecord(Map<Object, Object> properties) {
		return getBaseEntityManager().countRecordByProperties(entityClass, properties);
	}

	@Override
	public Number countRecord(Object... params) {
		return getBaseEntityManager().countRecord(entityClass, params);
	}

	@Override
	public List<T> findAll() {
		return getBaseEntityManager().findAll(entityClass);
	}

	@Override
	public List<T> findByProperties(Map<Object, Object> properties) {
		return getBaseEntityManager().findListByProperties(entityClass, properties);
	}

	@Override
	public List<T> findByProperties(Object... properties) {
		return getBaseEntityManager().findList(entityClass, properties);
	}

	@Override
	public Page<T> findPage(Page<T> page, Map<Object, Object> properties) {
		getBaseEntityManager().findPageByProperties(entityClass, page, properties);
		return page;
	}

	@Override
	public Page<T> findPage(Page<T> page, Object... properties) {
		getBaseEntityManager().findPage(entityClass, page, properties);
		return page;
	}

	@Override
	public T load(PK id) {
		return (T)getBaseEntityManager().load(entityClass, id);
	}

	@Override
	public T remove(T entity) {
		getBaseEntityManager().remove(entity);
		return entity;
	}

	@Override
	public void removes(Collection<T> entities) {
		getBaseEntityManager().removes(entities);
	}

	@Override
	public T removeById(PK id) {
		return (T)getBaseEntityManager().removeById(entityClass, id);
	}
	@Override
	public Collection<T> removeByIds(PK[] ids){
		List<T> list = new ArrayList<>(ids.length);
		Stream.of(ids).forEach(id->list.add(removeById(id)));
		return list;
	}

	/*@Override
	public T createNew(T entity) {
		getBaseEntityManager().persist(entity);
		return entity;
	}

	@Override
	public T updateAttributes(T entity) {
		getBaseEntityManager().update(entity);
		return entity;
	}*/

	public T save(T entity) {
		return getBaseEntityManager().save(entity);
	}

	@Override
	public T findUnique(Map<Object, Object> properties) {
		return (T)getBaseEntityManager().findUniqueByProperties(entityClass, properties);
	}

	@Override
	public T findUnique(Object... properties) {
		return (T)getBaseEntityManager().findUnique(entityClass, properties);
	}
	@Override
	public T findOne(Object... properties) {
		return (T)getBaseEntityManager().findOne(entityClass, properties);
	}

	@Override
	public List<T> findByProperties(QueryBuilder query) {
		return getBaseEntityManager().findList(query);
	}
	@Override
	public void findPage(final Page<T> page, QueryBuilder query) {
		getBaseEntityManager().findPage(page, query);
	}

	@Override
	public int removeAll() {
		return getBaseEntityManager().removeAll(entityClass);
	}
}
