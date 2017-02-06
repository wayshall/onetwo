package org.onetwo.dbm.support;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import javax.sql.DataSource;

import org.apache.commons.lang3.tuple.Pair;
import org.onetwo.common.db.BaseCrudEntityManager;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.CrudEntityManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.dbm.exception.DbmException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

final public class Dbms {
	
	final private static LoadingCache<Class<?>, CrudEntityManager<?, ?>> CRUD_MANAGER_MAPPER = CacheBuilder.newBuilder()
																						.weakKeys()
																						.weakValues()
																						.build(new CacheLoader<Class<?>, CrudEntityManager<?, ?>>() {

																							@Override
																							public CrudEntityManager<?, ?> load(Class<?> entityClass) throws Exception {
																								Pair<String, BeanDefinition> beanDef = createCrudEntityManagerBeanBeanDefinition(entityClass);
																								CrudEntityManager<?, ?> crudManager = (CrudEntityManager<?, ?>)Springs.getInstance().getBean(beanDef.getLeft());
																								return crudManager;
																							}
																							
																						});
	
	final private static LoadingCache<DataSource, BaseEntityManager> ENTITY_MANAGER_MAPPER = CacheBuilder.newBuilder()
																						.weakKeys()
																						.weakValues()
																						.build(new CacheLoader<DataSource, BaseEntityManager>() {

																							@Override
																							public BaseEntityManager load(DataSource ds) throws Exception {
																								return Dbms.newEntityManager(ds);
																							}
																							
																						});
	
	private static class BaseEntityManagerHoder {
		private static BaseEntityManager instance = Springs.getInstance().getBean(BaseEntityManager.class);
	}
	
	/***
	 * 获取默认的BaseEntityManager实例
	 * @return
	 */
	public static BaseEntityManager obtainBaseEntityManager(){
		return BaseEntityManagerHoder.instance;
	}
	
	/****
	 * 获取数据源对应的BaseEntityManager实例，dbm会根据传入的数据库创建一个BaseEntityManager实例
	 * @param dataSource
	 * @return
	 */
	public static BaseEntityManager obtainBaseEntityManager(DataSource dataSource) {
		try {
			return ENTITY_MANAGER_MAPPER.get(dataSource);
		} catch (ExecutionException e) {
			throw new BaseException("obtain BaseEntityManager error: " + e.getMessage(), e);
		}
	}
	/***
	 * 创建BaseEntityManager对象
	 * 注意BaseEntityManager对象都不包含事务拦截
	 * @param dataSource
	 * @return
	 */
	public static BaseEntityManager newEntityManager(DataSource dataSource) {
		DbmDaoImplementor dbmDao = (DbmDaoImplementor)newDao(dataSource);
		DbmEntityManagerImpl entityManager = new DbmEntityManagerImpl(dbmDao);
		try {
			entityManager.afterPropertiesSet();
		} catch (Exception e) {
			throw new DbmException("init CrudEntityManager error: " +e.getMessage());
		}
		return entityManager;
	}
	
	@SuppressWarnings("unchecked")
	public static <E, ID  extends Serializable> CrudEntityManager<E, ID> obtainCrudManager(Class<E> entityClass){
		try {
			return (CrudEntityManager<E, ID>)CRUD_MANAGER_MAPPER.get(entityClass);
		} catch (ExecutionException e) {
			throw new BaseException("obtain entityManager error: " + e.getMessage(), e);
		}
	}
	
	/****
	 * 使用spring配置的数据源创建CrudEntityManager
	 * 注意这样直接new创建的CrudEntityManager没有事务拦截，请在已配置事务的环境中使用
	 * @param entityClass
	 * @return
	 */
	public static <E, ID  extends Serializable> CrudEntityManager<E, ID> newCrudManager(Class<E> entityClass){
		return new BaseCrudEntityManager<>(entityClass);
	}
	
	static Pair<String, BeanDefinition> createCrudEntityManagerBeanBeanDefinition(Class<?> entityClass){
		String beanName = CrudEntityManager.class.getSimpleName()+"-"+entityClass.getName();
		BeanDefinition beandef = BeanDefinitionBuilder.rootBeanDefinition(BaseCrudEntityManager.class)
				.addConstructorArgValue(entityClass)
				.setScope(BeanDefinition.SCOPE_SINGLETON)
//				.setRole(BeanDefinition.ROLE_APPLICATION)
				.getBeanDefinition();
		BeanDefinitionRegistry registry = SpringUtils.getBeanDefinitionRegistry(Springs.getInstance().getAppContext());
		registry.registerBeanDefinition(beanName, beandef);
		return Pair.of(beanName, beandef);
	}
	/*****
	 * 使用指定的数据源创建CrudEntityManager
	 * 注意这样直接new创建的CrudEntityManager没有事务拦截，请在已配置事务的环境中使用
	 * @param entityClass
	 * @param dataSource
	 * @return
	 */
	public static <E, ID  extends Serializable> CrudEntityManager<E, ID> newCrudManager(Class<E> entityClass, DataSource dataSource){
		return new BaseCrudEntityManager<>(entityClass, newEntityManager(dataSource));
	}
	public static <E, ID  extends Serializable> CrudEntityManager<E, ID> newCrudManager(Class<E> entityClass, BaseEntityManager baseEntityManager){
		return new BaseCrudEntityManager<>(entityClass, baseEntityManager);
	}
	
	
	public static DbmDao newDao(DataSource dataSource){
		DbmDaoImpl dao = new DbmDaoImpl(dataSource);
		dao.setServiceRegistry(SimpleDbmInnerServiceRegistry.obtainServiceRegistry(dataSource));
		dao.initialize();
		return dao;
	}

	private Dbms(){
	}
}
