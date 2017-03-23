package org.onetwo.dbm.core.internal;

import java.util.Optional;

import org.onetwo.dbm.core.spi.CachableSession;
import org.onetwo.dbm.core.spi.DbmInterceptor;
import org.onetwo.dbm.core.spi.DbmInterceptorChain;
import org.onetwo.dbm.core.spi.DbmSession;
import org.onetwo.dbm.core.spi.DbmSessionFactory;

public class SessionCacheInterceptor implements DbmInterceptor {
	
	private DbmSessionFactory sessionFactory;

	@Override
	public Object intercept(DbmInterceptorChain chain) {
		Optional<DbmSession> sessionOpt = sessionFactory.getCurrentSession();
		if(!sessionOpt.isPresent() || !CachableSession.class.isInstance(sessionOpt.get())){
			return chain.invoke();
		}
		
		CachableSession session = (CachableSession)sessionOpt.get();
		if(chain.isDatabaseUpdate()){
			session.flush();
		}else if(chain.isDatabaseRead()){
			return session.getCaccheOrInvoke(chain);
		}
		return chain.invoke();
	}
	
	

}
