package org.onetwo.common.hibernate.msf;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.onetwo.common.exception.BaseException;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

@SuppressWarnings("serial")
public class MultipSessionFactoryTransactionManager extends HibernateTransactionManager {
	
	public static final int DEFAULT_TIMEOUT_IN_SECONDS = 120;
	
//	@Autowired
//	private JFishMultipleSessionFactory multipleSessionFactory;
	
	public MultipSessionFactoryTransactionManager(){
		this.setAutodetectDataSource(false);
		this.setDefaultTimeout(DEFAULT_TIMEOUT_IN_SECONDS);
	}


	@Override
	public SessionFactory getSessionFactory() {
		SessionFactory sf = super.getSessionFactory();
		if(JFishMultipleSessionFactory.class.isInstance(sf)){
			return ((JFishMultipleSessionFactory)sf).getCurrentSessionFactory();
		}else{
			return sf;
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		if(!JFishMultipleSessionFactory.class.isInstance(sessionFactory)){
			throw new BaseException("the implementor is not supported multip sessionFactory: " + sessionFactory);
		}
		super.setSessionFactory(sessionFactory);
	}


	@Override
	public DataSource getDataSource() {
		return SessionFactoryUtils.getDataSource(getSessionFactory());
	}


}
