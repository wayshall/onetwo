package org.onetwo.common.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.onetwo.common.db.builder.QueryBuilder;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.Page;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings({"unchecked"})
public class BaseCrudEntityManager<T, PK extends Serializable> implements CrudEntityManager<T, PK> {

 
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	protected Class<T> entityClass;
	protected volatile BaseEntityManager baseEntityManager;

	
	public BaseCrudEntityManager(Class<T> entityClass, BaseEntityManager baseEntityManager){
		if(entityClass==null){
			this.entityClass = (Class<T>)ReflectUtils.getSuperClassGenricType(this.getClass(), BaseCrudEntityManager.class);
		}else{
			this.entityClass = entityClass;
		}
		this.baseEntityManager = baseEntityManager;
	}
	
	public BaseCrudEntityManager(BaseEntityManager baseEntityManager){
		this((Class<T>)null, baseEntityManager);
	}

	public BaseEntityManager getBaseEntityManager() {
		BaseEntityManager bem = this.baseEntityManager;
		if(bem==null){
			bem = Springs.getInstance().getBean(BaseEntityManager.class);
			Objects.requireNonNull(bem, "BaseEntityManager not found");
			if(this.baseEntityManager==null){
				this.baseEntityManager = bem;
			}
		}
		return bem;
	}

	@Transactional
	@Override
	public int batchInsert(Collection<T> entities) {
		return getBaseEntityManager().getSessionFactory().getSession().batchInsert(entities);
	}

	@Transactional(readOnly=true)
	@Override
	public T findById(PK id) {
		return (T)getBaseEntityManager().findById(entityClass, id);
	}

	@Transactional(readOnly=true)
	@Override
	public Optional<T> findOptionalById(PK id) {
		return Optional.ofNullable(getBaseEntityManager().findById(entityClass, id));
	}


	@Transactional(readOnly=true)
	@Override
	public Number countRecord(Map<Object, Object> properties) {
		return getBaseEntityManager().countRecordByProperties(entityClass, properties);
	}

	@Transactional(readOnly=true)
	@Override
	public Number countRecord(Object... params) {
		return getBaseEntityManager().countRecord(entityClass, params);
	}

	@Transactional(readOnly=true)
	@Override
	public List<T> findAll() {
		return getBaseEntityManager().findAll(entityClass);
	}

	@Transactional(readOnly=true)
	@Override
	public List<T> findListByProperties(Map<Object, Object> properties) {
		return getBaseEntityManager().findListByProperties(entityClass, properties);
	}

	@Transactional(readOnly=true)
	@Override
	public List<T> findListByProperties(Object... properties) {
		return getBaseEntityManager().findList(entityClass, properties);
	}

	@Transactional(readOnly=true)
	@Override
	public Page<T> findPage(Page<T> page, Map<Object, Object> properties) {
		getBaseEntityManager().findPageByProperties(entityClass, page, properties);
		return page;
	}

	@Transactional(readOnly=true)
	@Override
	public Page<T> findPage(Page<T> page, Object... properties) {
		getBaseEntityManager().findPage(entityClass, page, properties);
		return page;
	}

	@Transactional(readOnly=true)
	@Override
	public T load(PK id) {
		return (T)getBaseEntityManager().load(entityClass, id);
	}

	@Transactional
	@Override
	public T remove(T entity) {
		getBaseEntityManager().remove(entity);
		return entity;
	}

	@Transactional
	@Override
	public void removes(Collection<T> entities) {
		getBaseEntityManager().removes(entities);
	}

	@Transactional
	@Override
	public T removeById(PK id) {
		return (T)getBaseEntityManager().removeById(entityClass, id);
	}
	
	@Transactional
	@Override
	public Collection<T> removeByIds(PK[] ids){
		List<T> list = new ArrayList<>(ids.length);
		Stream.of(ids).forEach(id->list.add(removeById(id)));
		return list;
	}


	@Transactional
	public T save(T entity) {
		return getBaseEntityManager().save(entity);
	}

	@Transactional
	@Override
	public void update(T entity) {
		getBaseEntityManager().update(entity);
	}

	@Transactional
	@Override
	public void persist(T entity) {
		getBaseEntityManager().persist(entity);
	}

	@Transactional(readOnly=true)
	@Override
	public T findUnique(Map<Object, Object> properties) {
		return (T)getBaseEntityManager().findUniqueByProperties(entityClass, properties);
	}

	@Transactional(readOnly=true)
	@Override
	public T findUnique(Object... properties) {
		return (T)getBaseEntityManager().findUnique(entityClass, properties);
	}

	@Transactional(readOnly=true)
	@Override
	public T findOne(Object... properties) {
		return (T)getBaseEntityManager().findOne(entityClass, properties);
	}

	@Transactional(readOnly=true)
	@Override
	public List<T> findListByProperties(QueryBuilder query) {
		return getBaseEntityManager().findList(query);
	}

	@Transactional(readOnly=true)
	@Override
	public void findPage(final Page<T> page, QueryBuilder query) {
		getBaseEntityManager().findPage(page, query);
	}

	@Transactional
	@Override
	public int removeAll() {
		return getBaseEntityManager().removeAll(entityClass);
	}

	@Transactional(readOnly=true)
	@Override
	public List<T> findListByExample(Object example) {
		return Querys.from(baseEntityManager, entityClass)
				.where()
				.addFields(example)
				.ignoreIfNull()
				.end()
				.toQuery()
				.list();
	}

	@Transactional(readOnly=true)
	@Override
	public Page<T> findPageByExample(Page<T> page, Object example) {
		return Querys.from(baseEntityManager, entityClass)
						.where()
						.addFields(example)
						.ignoreIfNull()
						.end()
						.toQuery()
						.page(page);
	}
	
}
