package org.onetwo.dbm.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.onetwo.common.db.BaseCrudEntityManager;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.builder.QueryBuilder;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.validator.ValidationBindingResult;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.mapping.JFishMappedEntry;
import org.springframework.transaction.annotation.Transactional;

abstract public class DbmCrudServiceImpl<T, PK extends Serializable> extends BaseCrudEntityManager<T, PK> {

	
	public DbmCrudServiceImpl(Class<T> entityClass) {
		super(entityClass);
	}

	protected DbmCrudServiceImpl(BaseEntityManager baseEntityManager) {
		super(baseEntityManager);
	}

	protected String serviceQName(String methodName){
		return this.getClass().getSimpleName() + "." + methodName;
	}

	@Override
	@Transactional(readOnly=true)
	public T findById(PK id) {
		return super.findById(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Number countRecord(Map<Object, Object> properties) {
		return super.countRecord(properties);
	}

	@Override
	@Transactional(readOnly=true)
	public Number countRecord(Object... params) {
		return super.countRecord(params);
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> findAll() {
		return super.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> findListByProperties(Map<Object, Object> properties) {
		return super.findListByProperties(properties);
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> findListByProperties(Object... properties) {
		return super.findListByProperties(properties);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<T> findPage(Page<T> page, Map<Object, Object> properties) {
		return super.findPage(page, properties);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<T> findPage(Page<T> page, Object... properties) {
		return super.findPage(page, properties);
	}

	@Override
	@Transactional(readOnly=true)
	public T load(PK id) {
		return super.load(id);
	}

	@Override
	@Transactional
	public T remove(T entity) {
		return super.remove(entity);
	}
	
	@Override
	@Transactional
	public void removes(Collection<T> entities) {
		super.removes(entities);
	}

	@Override
	@Transactional
	public T removeById(PK id) {
		return super.removeById(id);
	}

	@Override
	@Transactional
	public T save(T entity) {
		return super.save(entity);
	}

	/*@Override
	@Transactional
	public void persist(Object entity) {
		super.persist(entity);
	}*/

	@Override
	@Transactional(readOnly=true)
	public T findUnique(Map<Object, Object> properties) {
		return super.findUnique(properties);
	}

	@Override
	@Transactional(readOnly=true)
	public T findUnique(Object... properties) {
		return super.findUnique(properties);
	}
	
	@Transactional
	public int removeAll(){
		return baseEntityManager.removeAll(entityClass);
	}
	
	protected ValidationBindingResult validate(Object obj, Class<?>... groups){
		ValidationBindingResult validations = SpringApplication.getInstance().getValidator().validate(obj);
		return validations;
	}
	
	protected void validateAndThrow(Object obj, Class<?>... groups) throws BusinessException{
		ValidationBindingResult validations = validate(obj, groups);
		if(validations.hasErrors()){
			throw new ValidationException(validations.getErrorMessagesAsString());
		}
	}
	
	protected JFishMappedEntry getMappedEntry(){
		DbmEntityManager dem = (DbmEntityManager)this.getBaseEntityManager();
		return dem.getDbmDao().getMappedEntryManager().getEntry(entityClass);
	}
	
	protected QueryBuilder createQueryBuilder(){
		DbmEntityManager dem = (DbmEntityManager)this.getBaseEntityManager();
		return dem.createQueryBuilder(entityClass);
	}
}
