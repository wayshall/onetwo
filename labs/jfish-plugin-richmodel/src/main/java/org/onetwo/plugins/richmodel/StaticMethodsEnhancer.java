package org.onetwo.plugins.richmodel;

import javassist.CtClass;
import javassist.CtMethod;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.fish.JFishQueryBuilder;
import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;

public class StaticMethodsEnhancer implements JFishEnhancer {
	
	public static final String JMODEL_NAME = JFishModel.class.getName();
	public static final String DATAQUERY_NAME = DataQuery.class.getName();
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	@Override
	public void enhance(EnhanceContext context) {
		CtClass ctClass = context.getCtClass();
		this.addMethod("public static Class getEntityClass(){ return "+context.getClassName()+".class; }", ctClass);
		
		String jqbClass = JFishQueryBuilder.class.getName();
		this.addMethod("public static "+jqbClass+" where(){ return getJFishEntityManager().createQueryBuilder(getEntityClass()); }", ctClass);
		this.addMethod("public static "+jqbClass+" query(){ return getJFishEntityManager().createQueryBuilder(getEntityClass()); }", ctClass);
		
		this.addMethod("public static int removeAll() { return getJFishEntityManager().removeAll(getEntityClass()); }", ctClass);
		
		this.addMethod("public static int batchInsert(java.util.Collection entities) { return getJFishEntityManager().getJfishDao().batchInsert(entities); }", ctClass);
		
		this.addMethod("public static Number count(Object[] params) { return getJFishEntityManager().countRecord(getEntityClass(), params); }", ctClass);
		
		this.addMethod("public static java.util.List findList(Object[] properties) { return getJFishEntityManager().findByProperties(getEntityClass(), properties); }", ctClass);
		
		this.addMethod("public static void findPage(org.onetwo.common.utils.Page page, Object[] properties) { return getJFishEntityManager().findPage(getEntityClass(), page, properties); }", ctClass);
		this.addMethod("public static void findPageByQName(String queryName, org.onetwo.common.utils.Page page, Object[] properties) { getJFishEntityManager().findPageByQName(queryName, page, properties); }", ctClass);
		this.addMethod("public static Object findOneByQName(String queryName, Object[] properties) { return getJFishEntityManager().findUniqueByQName(queryName, properties); }", ctClass);
		
		this.addMethod("public static Object findOne(Object[] properties) { return getJFishEntityManager().findUnique(getEntityClass(), properties); }", ctClass);
		
		this.addMethod("public static Object findById(java.io.Serializable id) { return getJFishEntityManager().findById(getEntityClass(), id); }", ctClass);
		this.addMethod("public static Object loadById(java.io.Serializable id) { return getJFishEntityManager().load(getEntityClass(), id); }", ctClass);
		this.addMethod("public static Object removeById(java.io.Serializable id) { return getJFishEntityManager().removeById(getEntityClass(), id); }", ctClass);
		
		this.addMethod("public static "+DATAQUERY_NAME+" createNamedQuery(String name) { return getJFishEntityManager().createNamedQuery(name); }", ctClass);
		
		logger.info("model["+context.getClassName()+"] enhance static methods");
		ctClass.defrost();
	}
	
	protected void addMethod(String body, CtClass ctClass){
		CtMethod entityMethod;
		try {
			entityMethod = CtMethod.make(body, ctClass);
			ctClass.addMethod(entityMethod);
		} catch (Exception e) {
			throw new BaseException("add method error : " + e.getMessage(), e);
		}
	}

}
