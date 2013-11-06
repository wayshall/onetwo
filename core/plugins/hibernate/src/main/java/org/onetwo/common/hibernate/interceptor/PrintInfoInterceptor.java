package org.onetwo.common.hibernate.interceptor;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

@SuppressWarnings("serial")
public class PrintInfoInterceptor extends EmptyInterceptor {
	private int updates;
    private int creates;
    private int loads;
    
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {

		updates++;
		return false;
	}

	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		loads++;
		return false;
	}

	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {

		creates++;
		return false;
	}
	
	public void afterTransactionCompletion(Transaction tx) {
        if ( tx.wasCommitted() ) {
            System.out.println("Creations: " + creates + ", Updates: " + updates + ", Loads: " + loads);
        }
        updates=0;
        creates=0;
        loads=0;
    }
}
