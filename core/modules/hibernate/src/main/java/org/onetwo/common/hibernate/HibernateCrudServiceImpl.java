package org.onetwo.common.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.ValidationException;

import org.onetwo.common.db.BaseCrudServiceImpl;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.validator.ValidationBindingResult;
import org.onetwo.common.utils.Page;
import org.springframework.transaction.annotation.Transactional;

abstract public class HibernateCrudServiceImpl<T, PK extends Serializable> extends BaseCrudServiceImpl<T, PK> {

	private BaseEntityManager baseEntityManager;
	
	public HibernateCrudServiceImpl(){
	}
	
	public BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}

	@Resource
	public void setBaseEntityManager(BaseEntityManager baseEntityManager) {
		this.baseEntityManager = baseEntityManager;
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
	public List<T> findByProperties(Map<Object, Object> properties) {
		return super.findByProperties(properties);
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> findByProperties(Object... properties) {
		return super.findByProperties(properties);
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
	public void removeList(List<T> entities) {
		super.removeList(entities);
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

	@Override
	@Transactional
	public void persist(Object entity) {
		super.persist(entity);
	}

	@Override
	@Transactional
	public void delete(ILogicDeleteEntity entity) {
		super.delete(entity);
	}

	@Override
	@Transactional
	public ILogicDeleteEntity deleteById(Serializable id) {
		return super.deleteById(id);
	}

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
	
	protected ValidationBindingResult validate(Object obj, Class<?>... groups){
		ValidationBindingResult validations = SpringApplication.getInstance().getValidator().validate(obj);
		return validations;
	}
	
	protected void validateAndThrow(Object obj, Class<?>... groups) throws BusinessException{
		ValidationBindingResult validations = validate(obj, groups);
		if(validations.hasErrors()){
			throw new ValidationException(validations.getFieldErrorMessagesAsString());
		}
	}

}
