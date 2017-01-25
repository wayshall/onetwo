package org.onetwo.dbm.richmodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.Modifier;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.mapping.ScanedClassContext;
import org.slf4j.Logger;

import com.google.common.collect.Maps;

public class RichModelEnhanceProcessor implements PackageScanedProcessor {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private MultiEnhancer enhancer = new MultiEnhancer();
	private ClassPool classPool;
	private Map<String, ModelInfo> classes = new HashMap<>(50);
	
	public RichModelEnhanceProcessor() {
		super();
		classPool = ClassPool.getDefault();
		this.classPool.insertClassPath(new ClassClassPath(this.getClass()));
//		this.classPool.insertClassPath(new LoaderClassPath(ClassUtils.getDefaultClassLoader()));
		this.enhancer.addEnhancer(new StaticMethodsEnhancer());
	}
	

	public void processClasses(Collection<ScanedClassContext> clssNameList) {
		CtClass ctclass = null;
		for(ScanedClassContext ctx : clssNameList){
			if(classes.containsKey(ctx.getClassName())){
				continue;
			}
			try {
				ctclass = enhanceClass(ctx);
				if(ctclass==null){
					continue;
				}
				ctclass.toClass(ClassUtils.getDefaultClassLoader(), ClassUtils.class.getProtectionDomain());
				classes.put(ctx.getClassName(), null);
			} catch (Exception e) {
				throw new DbmException("enchan model["+ctx.getClassName()+"] error : "+e.getMessage(), e);
			}
		}
	}

	@Deprecated
	void processClasses2(Collection<ScanedClassContext> clssNameList) {
		CtClass ctclass = null;
		boolean enableSubClassEnhance = false;
		for(ScanedClassContext ctx : clssNameList){
			if(classes.containsKey(ctx.getClassName())){
				continue;
			}
			try {
				ctclass = enhanceClass(ctx);
				if(ctclass==null){
					continue;
				}
				ModelInfo info = new ModelInfo(ctx.getClassName());
				if(!enableSubClassEnhance){
					try {
						info.actualClass = ctclass.toClass(ClassUtils.getDefaultClassLoader(), ClassUtils.class.getProtectionDomain());
					} catch (CannotCompileException e) {
						enableSubClassEnhance = true;
						logger.warn("enhance RichModel error, enable SubClass enhance...");
					}
				}
				if(enableSubClassEnhance){
					String subClassName = ctx.getClassName()+"$$Dbm";
					info.subClassName = subClassName;
					ctclass = createSubCtClass(ctx.getClassName(), subClassName);
					ctclass = enhanceClass(subClassName, ctclass);
					info.actualClass = ctclass.toClass(ClassUtils.getDefaultClassLoader(), ClassUtils.class.getProtectionDomain());
				}
				info.enhanceSubClass = enableSubClassEnhance;
				info.ctClass = ctclass;
				classes.put(info.getModelClassName(), info);
			} catch (Exception e) {
				throw new DbmException("enchan model["+ctx.getClassName()+"] error : "+e.getMessage(), e);
			}
		}
		RichModels.setModelMapping(classes.values());
	}
	
	protected CtClass createSubCtClass(String superClass, String subClassName){
		try {
			CtClass subCtClass = this.classPool.makeClass(subClassName);
			CtClass superCtClass = this.classPool.get(superClass);
			subCtClass.setSuperclass(superCtClass);
			subCtClass.setModifiers(Modifier.PUBLIC);
			return subCtClass;
		} catch (Exception e) {
			throw new DbmException("enhance RichModel error for model class: " + superClass, e);
		}
	}
	
	protected CtClass enhanceClass(ScanedClassContext classCtx) throws Exception{
		String className = classCtx.getClassName();
		CtClass ctclass = classPool.makeClassIfNew(classCtx.getInputStream());
		return enhanceClass(className, ctclass);
	}
	
	protected CtClass enhanceClass(String className, CtClass ctclass) throws Exception{
		if(!ctclass.subtypeOf(classPool.get(RichModel.class.getName()))){
			return null;
		}
		if(ctclass.isFrozen())
			return ctclass;
		EnhanceContext context = new EnhanceContext(className, ctclass);
		enhancer.enhance(context);
//		ctclass.toClass();
		logger.info("enhance model : " + className);
		return ctclass;
	}

	public static class ModelInfo {
		private String modelClassName;
		private boolean enhanceSubClass;
		private String subClassName;
		private CtClass ctClass;
		private Class<?> actualClass;
		public ModelInfo(String modelClassName) {
			this.modelClassName = modelClassName;
		}
		public String getModelClassName() {
			return modelClassName;
		}
		public boolean isEnhanceSubClass() {
			return enhanceSubClass;
		}
		public String getSubClassName() {
			return subClassName;
		}
		public CtClass getCtClass() {
			return ctClass;
		}
		public Class<?> getActualClass() {
			return actualClass;
		}
		
	}

	@Deprecated
	static private final class RichModels {
		
		static private Map<String, ModelInfo> MODEL_MAPPING = Maps.newHashMap();
		
		@SuppressWarnings({ "unchecked", "unused" })
		public static <T, S extends T> S get(Class<T> clazz){
			ModelInfo info = MODEL_MAPPING.get(clazz.getName());
			return (S) ReflectUtils.newInstance(info.getActualClass());
		}

		static void addMapping(ModelInfo info){
			String className = info.getModelClassName();
			if(MODEL_MAPPING.containsKey(className)){
				throw new DbmException("mapping has be exists : " + className);
			}
			MODEL_MAPPING.put(className, info);
		}
		static void setModelMapping(Collection<ModelInfo> classes){
			MODEL_MAPPING = Maps.newHashMapWithExpectedSize(classes.size());
			classes.stream().forEach(cls->{
				addMapping(cls);
			});
		}
		private RichModels(){
		}
	}
}
