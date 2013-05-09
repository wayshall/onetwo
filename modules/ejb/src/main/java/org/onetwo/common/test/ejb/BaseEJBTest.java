package org.onetwo.common.test.ejb;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.onetwo.common.test.BaseTest;

public class BaseEJBTest extends BaseTest {
 
	protected static EntityManagerFactory entityManageFactory;
	protected static String unitName;
	
	protected EntityManager entityManager;
	protected static boolean ignoreJPA;
	 
	
	public static boolean isIgnoreJPA() {
		return ignoreJPA;
	}

	public static void setIgnoreJPA(boolean ignoreJPA) {
		BaseEJBTest.ignoreJPA = ignoreJPA;
	}

	public static void createEntityManageFactory(String unitName){
		entityManageFactory = Persistence.createEntityManagerFactory(unitName, System.getProperties());
	}
	  
	public static void closeEntityManageFactory(){
		if(entityManageFactory!=null){
			entityManageFactory.close();
		}
	}
	
	public void setup(){
		entityManager = entityManageFactory.createEntityManager();
	}
	
	public void tearDown(){
//		transaction.commit();
		if(entityManager!=null){
			entityManager.close();
		}
	}
	
	public <T> T createBeanAndInjectEntityManager(Class<T> clazz){
		T bean = EJBTestUtils.create(clazz);
		if(ignoreJPA)
			return bean;
		EJBTestUtils.autoFindAndInjectFields(bean, EntityManager.class, this.entityManager);
		return bean;
	}
	
	public BaseEJBTest setEntityManager(Object obj){
		injectField(obj, "entityManager", entityManager);
		return this;
	}

	public static EntityManagerFactory getEntityManageFactory() {
		return entityManageFactory;
	}

	public EntityManager getEntityManage() {
		return entityManager;
	}

	public static void setUnitName(String unitName) {
		BaseEJBTest.unitName = unitName;
	}

}
