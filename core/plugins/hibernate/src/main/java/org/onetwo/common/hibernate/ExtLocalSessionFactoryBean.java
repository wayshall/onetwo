package org.onetwo.common.hibernate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.onetwo.common.ds.JFishMultipleDatasource;
import org.onetwo.common.hibernate.event.EntityPackageRegisterEvent;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.config.JFishPropertyPlaceholder;
import org.onetwo.common.spring.plugin.ContextPluginManager;
import org.onetwo.common.spring.plugin.ContextPluginManagerFactory;
import org.onetwo.common.utils.list.JFishList;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

public class ExtLocalSessionFactoryBean extends LocalSessionFactoryBean implements ApplicationContextAware {
	private static final String DEFAULT_HIBERNATE_CONFIG_PREFIX = "hibernate.";
	private static final String EXT_HIBERNATE_CONFIG_PREFIX = "hib.";

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private ApplicationContext applicationContext;
	private PreInsertEventListener[] preInsertEventListeners;
	private PreUpdateEventListener[] preUpdateEventListeners;
	private SaveOrUpdateEventListener[] saveOrUpdateEventListeners;
	private PreDeleteEventListener[] preDeleteEventListeners;
	
	@Autowired
	private JFishPropertyPlaceholder configHolder;
	
	private boolean autoScanMultipleDatasources;
//	private String masterName;
	private DataSource dataSourceHolder;
	
//	@Resource
	private ContextPluginManager<?> contextPluginManager = ContextPluginManagerFactory.getContextPluginManager();
	private JFishList<String> packageListToScan = JFishList.create();
	
	public ExtLocalSessionFactoryBean(){
	}
	
	public void afterPropertiesSet() throws IOException {
		if(getHibernateProperties()==null || getHibernateProperties().isEmpty()){
			this.setHibernateProperties(autoHibernateConfig());
		}
		if(autoScanMultipleDatasources){
			Map<String, DataSource> datasources = SpringUtils.getBeansAsMap(applicationContext, DataSource.class);
			logger.info("scan datasources: " + datasources);
			/*JFishMultipleDatasource mds = new JFishMultipleDatasource();
			mds.setDatasources(datasources);
			mds.setMasterDatasource(dataSourceHolder);*/
//			String beanName = StringUtils.uncapitalize(JFishMultipleDatasource.class.getSimpleName());
//			SpringUtils.registerSingleton(applicationContext, beanName, mds);
			JFishMultipleDatasource mds = SpringUtils.registerBean(applicationContext, JFishMultipleDatasource.class, 
									"datasources", datasources, 
									"masterDatasource", dataSourceHolder);
			this.setDataSource(mds);
		}
		
//		this.contextPluginManager.registerEntityPackage(packageListToScan);
		this.contextPluginManager.getEventBus().postEvent(new EntityPackageRegisterEvent(contextPluginManager, packageListToScan));
		super.setPackagesToScan(packageListToScan.toArray(new String[0]));
		
		super.afterPropertiesSet();
	}

	public void setPackagesToScan(String... packagesToScan) {
		packageListToScan.addArray(packagesToScan);
	}
	
	
	public void setAutoScanMultipleDatasources(boolean autoScanMultipleDatasources) {
		this.autoScanMultipleDatasources = autoScanMultipleDatasources;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSourceHolder = dataSource;
		super.setDataSource(dataSource);
	}
	
	protected Properties autoHibernateConfig(){
		Properties props = configHolder.getMergedConfig();
		Properties hibConfig = new Properties();
		String key = null;
		logger.info("================ hibernate config ================");
		for (Map.Entry<Object, Object> e : props.entrySet()){
			key = e.getKey().toString();
			if(key.startsWith(DEFAULT_HIBERNATE_CONFIG_PREFIX)){
				logger.info("{}: {}", key, e.getValue().toString());
				hibConfig.setProperty(key, e.getValue().toString());
				
			}else if(key.startsWith(EXT_HIBERNATE_CONFIG_PREFIX)){
				key = key.substring(EXT_HIBERNATE_CONFIG_PREFIX.length());
				logger.info("{}: {}", key, e.getValue().toString());
				hibConfig.setProperty(key, e.getValue().toString());
			}
		}
		logger.info("================ hibernate config ================");
		return hibConfig;
	}
	

	protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder sfb) {
		/*if(sfb.getInterceptor()==null){
			sfb.setInterceptor(new TimestampInterceptor());
		}*/
		
		sfb.setNamingStrategy(new ImprovedNamingStrategy());
		
		SessionFactory sf = super.buildSessionFactory(sfb);
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) sf;
		EventListenerRegistry reg = sfi.getServiceRegistry().getService(EventListenerRegistry.class);
		
		if(preInsertEventListeners==null){
			List<PreInsertEventListener> preInserts = SpringUtils.getBeans(applicationContext, PreInsertEventListener.class);
			this.preInsertEventListeners = preInserts.toArray(new PreInsertEventListener[0]);
		}
		reg.getEventListenerGroup(EventType.PRE_INSERT).appendListeners(preInsertEventListeners);

		if(preUpdateEventListeners==null){
			List<PreUpdateEventListener> preUpdates = SpringUtils.getBeans(applicationContext, PreUpdateEventListener.class);
			this.preUpdateEventListeners = preUpdates.toArray(new PreUpdateEventListener[0]);
		}
		reg.getEventListenerGroup(EventType.PRE_UPDATE).appendListeners(preUpdateEventListeners);

		if(preDeleteEventListeners==null){
			List<PreDeleteEventListener> preUpdates = SpringUtils.getBeans(applicationContext, PreDeleteEventListener.class);
			this.preDeleteEventListeners = preUpdates.toArray(new PreDeleteEventListener[0]);
		}
		reg.getEventListenerGroup(EventType.PRE_DELETE).appendListeners(preDeleteEventListeners);

		if(saveOrUpdateEventListeners==null){
			List<SaveOrUpdateEventListener> preUpdates = SpringUtils.getBeans(applicationContext, SaveOrUpdateEventListener.class);
			this.saveOrUpdateEventListeners = preUpdates.toArray(new SaveOrUpdateEventListener[0]);
		}
		reg.getEventListenerGroup(EventType.SAVE_UPDATE).appendListeners(saveOrUpdateEventListeners);
//		reg.getEventListenerGroup(EventType.SAVE_UPDATE).appendListener(new SaveOrUpdateTimeListener());
//		HibernateUtils.initSessionFactory(sf);
		return sf;
	}

	public void setPreInsertEventListeners(PreInsertEventListener[] preInsertEventListeners) {
		this.preInsertEventListeners = preInsertEventListeners;
	}

	public void setPreUpdateEventListeners(PreUpdateEventListener[] preUpdateEventListeners) {
		this.preUpdateEventListeners = preUpdateEventListeners;
	}

	public void setSaveOrUpdateEventListeners(SaveOrUpdateEventListener[] saveOrUpdateEventListeners) {
		this.saveOrUpdateEventListeners = saveOrUpdateEventListeners;
	}

	public void setPreDeleteEventListeners(PreDeleteEventListener[] preDeleteEventListeners) {
		this.preDeleteEventListeners = preDeleteEventListeners;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
