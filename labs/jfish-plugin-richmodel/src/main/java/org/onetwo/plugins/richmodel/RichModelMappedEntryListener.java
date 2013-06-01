package org.onetwo.plugins.richmodel;

import java.util.List;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.MappedEntryManager;
import org.onetwo.common.fish.orm.MappedEntryManagerListener;
import org.onetwo.common.fish.spring.ScanedClassContext;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.slf4j.Logger;
import org.springframework.core.Ordered;

public class RichModelMappedEntryListener implements MappedEntryManagerListener, Ordered {

	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private JFishMutiEnhancer enhancer = new JFishMutiEnhancer();
	private ClassPool classPool;
	
	public RichModelMappedEntryListener() {
		super();
		classPool = ClassPool.getDefault();
		this.classPool.insertClassPath(new ClassClassPath(this.getClass()));
		this.enhancer.addEnhancer(new StaticMethodsEnhancer());
	}
	

	@Override
	public void afterAllEntryHasBuilt(MappedEntryManager mappedEntryManager, List<JFishMappedEntry> entryList) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void beforeBuild(MappedEntryManager mappedEntryManager, List<ScanedClassContext> clssNameList) {
		JFishList<CtClass> classes = JFishList.create();
		CtClass ctclass = null;
		for(ScanedClassContext ctx : clssNameList){
			try {
				ctclass = enhanceClass(ctx);
				if(ctclass!=null){
					classes.add(ctclass);
				}
			} catch (Exception e) {
				throw new JFishOrmException("enchan model["+ctx.getClassName()+"] error : "+e.getMessage(), e);
			}
		}
		
		classes.each(new NoIndexIt<CtClass>() {

			@Override
			protected void doIt(CtClass element) throws Exception {
//				Class<?> cls = element.toClass();
				element.toClass(ClassUtils.class.getClassLoader(), ClassUtils.class.getProtectionDomain());
//				element.writeFile();
//				System.out.println("class=>: " +element.getName()+", "+cls.getClassLoader());
			}
			
		});
	}
	
	protected CtClass enhanceClass(ScanedClassContext classCtx) throws Exception{
		String className = classCtx.getClassName();
		CtClass ctclass = classPool.makeClassIfNew(classCtx.getInputStream());
		if(!ctclass.subtypeOf(classPool.get(JFishModel.class.getName()))){
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
	
	/*protected CtClass enhanceClass2(ScanedClassContext classCtx) throws Exception{
		String className = classCtx.getClassName();
		CtClass ctclass = classPool.get(className);
		if(!ctclass.subtypeOf(classPool.get(JFishModel.class.getName()))){
			return null;
		}
		EnhanceContext context = new EnhanceContext(className, ctclass);
		enhancer.enhance(context);
//		ctclass.toClass();
		logger.info("enhance model : " + className);
		return ctclass;
	}*/
	
	@Override
	public void afterBuilt(MappedEntryManager mappedEntryManager, JFishMappedEntry entry) {
		
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
	
}
