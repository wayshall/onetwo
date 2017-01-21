package org.onetwo.dbm.richmodel;

import javassist.CtClass;
import javassist.CtMethod;

import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.builder.WhereCauseBuilder;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.support.DbmDao;
import org.slf4j.Logger;

public class StaticMethodsEnhancer implements ModelEnhancer {
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	private final static String DBM_DAO_CLASS = DbmDao.class.getName();
	private final static String PAGE_CLASS = Page.class.getName();
	private final static String WCB_CLASS = WhereCauseBuilder.class.getName();
	private final static String QUERYS_CLASS = Querys.class.getName();

	@Override
	public void enhance(EnhanceContext context) {
		CtClass ctClass = context.getCtClass();
		
		this.addMethod("protected static Class getStaticEntityClass(){ return "+context.getClassName()+".class; }", ctClass);
		
		this.addMethod("public static int batchInsert(java.util.Collection entities) { return (("+DBM_DAO_CLASS+")entityManager().getRawManagerObject()).batchInsert(entities); }", ctClass);
		
		this.addMethod("public static Number count(Object[] params) { return entityManager().countRecord(getStaticEntityClass(), params); }", ctClass);
		
		this.addMethod("public static java.util.List findList(Object[] properties) { return entityManager().findList(getStaticEntityClass(), properties); }", ctClass);
		
		this.addMethod("public static void findPage("+PAGE_CLASS+" page, Object[] properties) { entityManager().findPage(getStaticEntityClass(), page, properties); }", ctClass);
		
		this.addMethod("public static int removeAll() { return entityManager().removeAll(getStaticEntityClass()); }", ctClass);

		this.addMethod("public static Object findById(java.io.Serializable id) { return entityManager().findById(getStaticEntityClass(), id); }", ctClass);
		this.addMethod("public static Object loadById(java.io.Serializable id) { return entityManager().load(getStaticEntityClass(), id); }", ctClass);
		this.addMethod("public static Object removeById(java.io.Serializable id) { return entityManager().removeById(getStaticEntityClass(), id); }", ctClass);
		this.addMethod("public static Object findOne(Object[] properties) { return entityManager().findOne(getStaticEntityClass(), properties); }", ctClass);
		this.addMethod("public static "+WCB_CLASS+" where() { return "+QUERYS_CLASS+".from(entityManager(), getStaticEntityClass()).where(); }", ctClass);
		
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
