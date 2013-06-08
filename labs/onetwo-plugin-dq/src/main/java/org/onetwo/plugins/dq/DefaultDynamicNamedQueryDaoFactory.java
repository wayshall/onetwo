package org.onetwo.plugins.dq;

import java.util.Collection;

import javassist.ClassPool;

import org.onetwo.common.fish.spring.JFishDaoImplementor;
import org.onetwo.common.fish.spring.JFishDaoLifeCycleListener;
import org.onetwo.common.fish.spring.JFishNamedFileQueryInfo;
import org.onetwo.common.fish.spring.JFishNamedFileQueryManager;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;
import org.onetwo.common.utils.propconf.NamespacePropertiesManagerImpl.NamespaceProperties;
import org.slf4j.Logger;

public class DefaultDynamicNamedQueryDaoFactory implements JFishDaoLifeCycleListener {
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

//	private String packageToScan;
	private static final String PROXY_POSTFIX = "$proxy";
	
	private JFishNamedFileQueryManager jfishNamedFileQueryManager;
	private ClassPool classPool = ClassPool.getDefault();;
	
	@Override
	public void onInit(JFishDaoImplementor dao) {
		classPool = ClassPool.getDefault();
		this.jfishNamedFileQueryManager = dao.getJfishFileQueryFactory();
		if(!(jfishNamedFileQueryManager instanceof NamespacePropertiesManager)){
			logger.warn("jfishNamedFileQueryManager is not a instance of NamespacePropertiesManager, ignore generate dynamic query class.");
			return ;
		}
		NamespacePropertiesManager<JFishNamedFileQueryInfo> namespaceManager = (NamespacePropertiesManager<JFishNamedFileQueryInfo>)this.jfishNamedFileQueryManager;
		Collection<NamespaceProperties<JFishNamedFileQueryInfo>> namespacelist = namespaceManager.getAllNamespaceProperties();
		
		Class<?> dqInterface = null;
		for(NamespaceProperties<JFishNamedFileQueryInfo> nsp : namespacelist){
			dqInterface = ReflectUtils.loadClass(nsp.getNamespace());
			this.classPool.makeClass(dqInterface.getName()+PROXY_POSTFIX);
		}
	}

	@Override
	public void onDestroy(JFishDaoImplementor dao) {
		
	}
}
