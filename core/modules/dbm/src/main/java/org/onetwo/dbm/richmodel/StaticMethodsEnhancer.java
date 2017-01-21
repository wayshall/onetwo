package org.onetwo.dbm.richmodel;

import javassist.CtClass;
import javassist.CtMethod;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.support.Dbms;
import org.slf4j.Logger;

public class StaticMethodsEnhancer implements ModelEnhancer {
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
//	private final static String DBM_DAO_CLASS = DbmDao.class.getName();
	private final static String PAGE_CLASS = Page.class.getName();
	/*private final static String WCB_CLASS = WhereCauseBuilder.class.getName();
	private final static String QUERYS_CLASS = Querys.class.getName();*/

	@Override
	public void enhance(EnhanceContext context) {
		CtClass ctClass = context.getCtClass();
		
		String modelName = context.getClassName();
		String crudManagerStr = Dbms.class.getName()+".obtainCrudManager(obtainStaticEntityClass())";
		this.addMethod("protected static Class obtainStaticEntityClass(){ return "+modelName+".class; }", ctClass);
		
		this.addMethod("public static int batchInsert(java.util.Collection entities) { return "+crudManagerStr+".batchInsert(entities); }", ctClass);
		
		this.addMethod("public static Number count(Object[] params) { return "+crudManagerStr+".countRecord(params); }", ctClass);
		
		this.addMethod("public static java.util.List findList(Object[] properties) { return "+crudManagerStr+".findListByProperties(properties); }", ctClass);
		this.addMethod("public static java.util.List findListByExample(Object example) { return "+crudManagerStr+".findListByExample(example); }", ctClass);
		
		this.addMethod("public static "+PAGE_CLASS+" findPage("+PAGE_CLASS+" page, Object[] properties) { return "+crudManagerStr+".findPage(page, properties); }", ctClass);
		this.addMethod("public static "+PAGE_CLASS+" findPageByExample("+PAGE_CLASS+" page, Object example) { return "+crudManagerStr+".findPageByExample(page, example); }", ctClass);
		
		this.addMethod("public static int removeAll() { return "+crudManagerStr+".removeAll(); }", ctClass);

		this.addMethod("public static Object findById(java.io.Serializable id) { return "+crudManagerStr+".findById(id); }", ctClass);
		this.addMethod("public static Object loadById(java.io.Serializable id) { return "+crudManagerStr+".load(id); }", ctClass);
		this.addMethod("public static java.util.Collection removeByIds(java.io.Serializable[] ids) { return "+crudManagerStr+".removeByIds(ids); }", ctClass);
		this.addMethod("public static Object removeById(java.io.Serializable id) { return "+crudManagerStr+".removeById(id); }", ctClass);
		this.addMethod("public static Object findOne(Object[] properties) { return "+crudManagerStr+".findOne(properties); }", ctClass);
//		this.addMethod("protected static "+WCB_CLASS+" where() { return "+QUERYS_CLASS+".from(entityManager(), obtainStaticEntityClass()).where(); }", ctClass);
		
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
