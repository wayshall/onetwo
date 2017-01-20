package org.onetwo.dbm.richmodel;

import javassist.CtClass;
import javassist.CtMethod;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.support.DbmDao;
import org.slf4j.Logger;

public class StaticMethodsEnhancer implements ModelEnhancer {
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	private final static String DBM_DAO_CLASS = DbmDao.class.getName();
	private final static String PAGE_CLASS = Page.class.getName();

	@Override
	public void enhance(EnhanceContext context) {
		CtClass ctClass = context.getCtClass();
		
		this.addMethod("public static Class getEntityClass(){ return "+context.getClassName()+".class; }", ctClass);
		
		this.addMethod("public static int batchInsert(java.util.Collection entities) { return (("+DBM_DAO_CLASS+")entityManager().getRawManagerObject()).batchInsert(entities); }", ctClass);
		
		this.addMethod("public static Number count(Object[] params) { return entityManager().countRecord(getEntityClass(), params); }", ctClass);
		
		this.addMethod("public static java.util.List findList(Object[] properties) { return entityManager().findList(getEntityClass(), properties); }", ctClass);
		
		this.addMethod("public static void findPage("+PAGE_CLASS+" page, Object[] properties) { entityManager().findPage(getEntityClass(), page, properties); }", ctClass);
		
		this.addMethod("public static int removeAll() { return entityManager().removeAll(getEntityClass()); }", ctClass);

		this.addMethod("public static Object findById(java.io.Serializable id) { return entityManager().findById(getEntityClass(), id); }", ctClass);
		this.addMethod("public static Object loadById(java.io.Serializable id) { return entityManager().load(getEntityClass(), id); }", ctClass);
		this.addMethod("public static Object removeById(java.io.Serializable id) { return entityManager().removeById(getEntityClass(), id); }", ctClass);
		this.addMethod("public static Object findOne(Object[] properties) { return entityManager().findOne(getEntityClass(), properties); }", ctClass);
		
		logger.info("model["+context.getClassName()+"] enhance static methods");
		ctClass.defrost();
	}
	
	protected void addMethod(String body, CtClass ctClass){
		CtMethod entityMethod;
		try {
			entityMethod = CtMethod.make(body, ctClass);
			ctClass.addMethod(entityMethod);
		} catch (Exception e) {
			throw new BaseException("add method error : " + body, e);
		}
	}

}
