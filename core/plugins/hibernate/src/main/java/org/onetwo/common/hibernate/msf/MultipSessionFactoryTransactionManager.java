package org.onetwo.common.hibernate.msf;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

@SuppressWarnings("serial")
public class MultipSessionFactoryTransactionManager extends HibernateTransactionManager {
	
//	@Autowired
//	private JFishMultipleSessionFactory multipleSessionFactory;
	
	public MultipSessionFactoryTransactionManager(){
		this.setAutodetectDataSource(false);
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
			throw new UnsupportedOperationException("the implementor is not supported multip sessionFactory: " + sessionFactory);
		}
		super.setSessionFactory(sessionFactory);
	}


	@Override
	public DataSource getDataSource() {
		return SessionFactoryUtils.getDataSource(getSessionFactory());
	}


}
