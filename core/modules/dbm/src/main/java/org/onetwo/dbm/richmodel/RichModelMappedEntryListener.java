package org.onetwo.dbm.richmodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.onetwo.dbm.mapping.MappedEntryManagerListener;
import org.onetwo.dbm.mapping.ScanedClassContext;
import org.slf4j.Logger;
import org.springframework.core.Ordered;

public class RichModelMappedEntryListener implements MappedEntryManagerListener, Ordered {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private MultiEnhancer enhancer = new MultiEnhancer();
	private ClassPool classPool;
	private Set<CtClass> classes = new HashSet<CtClass>(50);
	
	public RichModelMappedEntryListener() {
		super();
		classPool = ClassPool.getDefault();
		this.classPool.insertClassPath(new ClassClassPath(this.getClass()));
//		this.classPool.insertClassPath(new LoaderClassPath(ClassUtils.getDefaultClassLoader()));
		this.enhancer.addEnhancer(new StaticMethodsEnhancer());
	}
	

	@Override
	public void beforeBuild(MappedEntryManager mappedEntryManager, Collection<ScanedClassContext> clssNameList) {
		CtClass ctclass = null;
		for(ScanedClassContext ctx : clssNameList){
			if(classes.contains(ctx)){
				continue;
			}
			try {
				ctclass = enhanceClass(ctx);
				if(ctclass!=null){
					ctclass.toClass(ClassUtils.getDefaultClassLoader(), ClassUtils.class.getProtectionDomain());
					classes.add(ctclass);
				}
			} catch (Exception e) {
				throw new DbmException("enchan model["+ctx.getClassName()+"] error : "+e.getMessage(), e);
			}
		}
		
	}
	
	protected CtClass enhanceClass(ScanedClassContext classCtx) throws Exception{
		String className = classCtx.getClassName();
		CtClass ctclass = classPool.makeClassIfNew(classCtx.getInputStream());
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
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
	
}
