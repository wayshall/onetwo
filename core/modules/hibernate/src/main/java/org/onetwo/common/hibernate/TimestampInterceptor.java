package org.onetwo.common.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.onetwo.common.db.IBaseEntity;

@SuppressWarnings("serial")
public class TimestampInterceptor extends EmptyInterceptor {
	private int updates;
    private int creates;
    private int loads;
    
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {

		if (entity instanceof IBaseEntity) {
			updates++;
			for (int i = 0; i < propertyNames.length; i++) {
				if ("lastUpdateTime".equals(propertyNames[i])) {
					currentState[i] = new Date();
					return true;
				}
			}
		}
		return false;
	}

	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		if (entity instanceof IBaseEntity) {
			loads++;
		}
		return false;
	}

	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {

		if (entity instanceof IBaseEntity) {
			creates++;
			for (int i = 0; i < propertyNames.length; i++) {
				if ("createTime".equals(propertyNames[i])) {
					state[i] = new Date();
					return true;
				}
			}
		}
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
