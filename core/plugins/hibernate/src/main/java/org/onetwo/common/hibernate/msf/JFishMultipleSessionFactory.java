package org.onetwo.common.hibernate.msf;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.StatelessSessionBuilder;
import org.hibernate.TypeHelper;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;
import org.onetwo.common.ds.SwitcherInfo;
import org.onetwo.common.fish.utils.ContextHolder;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

public class JFishMultipleSessionFactory implements SessionFactory, Ordered, InitializingBean, ApplicationContextAware {

//	public static final String SESSIONFACTORY_KEY = "SessionFactory";
	public static final String DEFAULT_SESSIONFACTORY = SwitcherInfo.DEFAULT_SWITCHER_NAME;// + SESSIONFACTORY_KEY;
	
	private Map<String, SessionFactory> sessionFactories;
	private ContextHolder contextHolder;
	private String defaultSessionFactoryName;
	private SessionFactory defaultSessionFactory;
	private boolean multipleSession = true;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(StringUtils.isBlank(defaultSessionFactoryName))
			defaultSessionFactoryName = DEFAULT_SESSIONFACTORY;
		
		Assert.notEmpty(sessionFactories, "sessionFactories can not be empty.");
		if(defaultSessionFactory==null){
//			Assert.hasText(masterName, "masterName can not be empty.");
			this.defaultSessionFactory = sessionFactories.get(defaultSessionFactoryName);
		}
		Assert.notNull(defaultSessionFactory, "defaultSessionFactory can not be null: " + defaultSessionFactoryName);
		
		if(contextHolder==null){
			contextHolder = SpringUtils.getBean(applicationContext, ContextHolder.class);
		}
		
		Assert.notNull(contextHolder, "contextHolder can not be null.");
	}

	public SessionFactory getCurrentSessionFactory(){
		if(!multipleSession)
			return defaultSessionFactory;
		
		SwitcherInfo switcher = contextHolder.getContextAttribute(SwitcherInfo.CURRENT_SWITCHER_INFO);
		SessionFactory sf = defaultSessionFactory;
		if(sessionFactories!=null && switcher!=null){
			String sfName = switcher.getCurrentSwitcherName();// + SESSIONFACTORY_KEY;
			if(sessionFactories.containsKey(sfName))
				sf = sessionFactories.get(sfName);
		}
		return sf;
	}
	
	public SessionFactory getSessionFactory(String name){
		return sessionFactories.get(name);
	}

	public void setDefaultSessionFactory(SessionFactory defaultSessionFactory) {
		this.defaultSessionFactory = defaultSessionFactory;
	}
	

	public void setSessionFactories(Map<String, SessionFactory> sessionFactories) {
		this.sessionFactories = sessionFactories;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public Reference getReference() throws NamingException {
		return getCurrentSessionFactory().getReference();
	}

	public SessionFactoryOptions getSessionFactoryOptions() {
		return getCurrentSessionFactory().getSessionFactoryOptions();
	}

	public SessionBuilder withOptions() {
		return getCurrentSessionFactory().withOptions();
	}

	public Session openSession() throws HibernateException {
		return getCurrentSessionFactory().openSession();
	}

	public Session getCurrentSession() throws HibernateException {
		return getCurrentSessionFactory().getCurrentSession();
	}

	public StatelessSessionBuilder withStatelessOptions() {
		return getCurrentSessionFactory().withStatelessOptions();
	}

	public StatelessSession openStatelessSession() {
		return getCurrentSessionFactory().openStatelessSession();
	}

	public StatelessSession openStatelessSession(Connection connection) {
		return getCurrentSessionFactory().openStatelessSession(connection);
	}

	public ClassMetadata getClassMetadata(Class entityClass) {
		return getCurrentSessionFactory().getClassMetadata(entityClass);
	}

	public ClassMetadata getClassMetadata(String entityName) {
		return getCurrentSessionFactory().getClassMetadata(entityName);
	}

	public CollectionMetadata getCollectionMetadata(String roleName) {
		return getCurrentSessionFactory().getCollectionMetadata(roleName);
	}

	public Map<String, ClassMetadata> getAllClassMetadata() {
		return getCurrentSessionFactory().getAllClassMetadata();
	}

	public Map getAllCollectionMetadata() {
		return getCurrentSessionFactory().getAllCollectionMetadata();
	}

	public Statistics getStatistics() {
		return getCurrentSessionFactory().getStatistics();
	}

	public void close() throws HibernateException {
		getCurrentSessionFactory().close();
	}

	public boolean isClosed() {
		return getCurrentSessionFactory().isClosed();
	}

	public Cache getCache() {
		return getCurrentSessionFactory().getCache();
	}

	public void evict(Class persistentClass) throws HibernateException {
		getCurrentSessionFactory().evict(persistentClass);
	}

	public void evict(Class persistentClass, Serializable id)
			throws HibernateException {
		getCurrentSessionFactory().evict(persistentClass, id);
	}

	public void evictEntity(String entityName) throws HibernateException {
		getCurrentSessionFactory().evictEntity(entityName);
	}

	public void evictEntity(String entityName, Serializable id)
			throws HibernateException {
		getCurrentSessionFactory().evictEntity(entityName, id);
	}

	public void evictCollection(String roleName) throws HibernateException {
		getCurrentSessionFactory().evictCollection(roleName);
	}

	public void evictCollection(String roleName, Serializable id)
			throws HibernateException {
		getCurrentSessionFactory().evictCollection(roleName, id);
	}

	public void evictQueries(String cacheRegion) throws HibernateException {
		getCurrentSessionFactory().evictQueries(cacheRegion);
	}

	public void evictQueries() throws HibernateException {
		getCurrentSessionFactory().evictQueries();
	}

	public Set getDefinedFilterNames() {
		return getCurrentSessionFactory().getDefinedFilterNames();
	}

	public FilterDefinition getFilterDefinition(String filterName)
			throws HibernateException {
		return getCurrentSessionFactory().getFilterDefinition(filterName);
	}

	public boolean containsFetchProfileDefinition(String name) {
		return getCurrentSessionFactory().containsFetchProfileDefinition(name);
	}

	public TypeHelper getTypeHelper() {
		return getCurrentSessionFactory().getTypeHelper();
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}


}
