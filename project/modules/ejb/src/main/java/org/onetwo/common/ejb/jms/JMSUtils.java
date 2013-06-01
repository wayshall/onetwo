package org.onetwo.common.ejb.jms;

import javax.jms.Connection;
import javax.jms.Session;

import org.onetwo.common.utils.LangUtils;

public abstract class JMSUtils {

	public static void close(Session session){
		try {
			if(session!=null)
				session.close();
		} catch (Exception e) {
			LangUtils.throwBaseException("close session error : " + e.getMessage(), e);
		}
	}

	public static void close(Connection con){
		try {
			if(con!=null){
				con.stop();
				con.close();
			}
		} catch (Exception e) {
			LangUtils.throwBaseException("close connection error : " + e.getMessage(), e);
		}
	}
	
	public static void rollback(Session session){
		try {
			session.rollback();
		} catch (Exception e) {
			LangUtils.throwBaseException("session rollback error : " + e.getMessage(), e);
		}
	}
	
	public static void commit(Session session){
		try {
			session.commit();
		} catch (Exception e) {
			LangUtils.throwBaseException("session commit error : " + e.getMessage(), e);
		}
	}
	
	
}
