package org.onetwo.plugins.dq;

import java.util.Collection;

import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.spring.JFishEntityManagerLifeCycleListener;
import org.onetwo.common.fish.spring.JFishNamedFileQueryInfo;
import org.onetwo.common.fish.spring.JFishNamedFileQueryManager;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;
import org.onetwo.common.utils.propconf.NamespacePropertiesManagerImpl.NamespaceProperties;
import org.slf4j.Logger;

public class DefaultDynamicNamedQueryDaoFactory implements JFishEntityManagerLifeCycleListener {
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

//	private String packageToScan;
	
	private JFishNamedFileQueryManager jfishNamedFileQueryManager;
//	private ClassPool classPool = ClassPool.getDefault();
	
	@Override
	public void onInit(JFishEntityManager em) {
		this.jfishNamedFileQueryManager = em.getJfishDao().getJfishFileQueryFactory();
		if(!(jfishNamedFileQueryManager instanceof NamespacePropertiesManager)){
			logger.warn("jfishNamedFileQueryManager is not a instance of NamespacePropertiesManager, ignore generate dynamic query class.");
			return ;
		}
		
		NamespacePropertiesManager<JFishNamedFileQueryInfo> namespaceManager = (NamespacePropertiesManager<JFishNamedFileQueryInfo>)this.jfishNamedFileQueryManager;
		Collection<NamespaceProperties<JFishNamedFileQueryInfo>> namespacelist = namespaceManager.getAllNamespaceProperties();
		
		Class<?> dqInterface = null;
		DynamicQueryProxyFactory factory = null;
		for(NamespaceProperties<JFishNamedFileQueryInfo> nsp : namespacelist){
			dqInterface = ReflectUtils.loadClass(nsp.getNamespace());
			factory = new DynamicQueryHandler(object, proxiedInterfaces);
		}
	}

	@Override
	public void onDestroy(JFishEntityManager dao) {
		
	}
}
