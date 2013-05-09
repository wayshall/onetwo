package org.onetwo.common.db.wheel;

import org.onetwo.common.utils.LangUtils;

public class DefaultConnectionExecutor implements ConnectionExecutor {
	
//	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private boolean transaction;
	
	public DefaultConnectionExecutor(boolean transaction) {
		this.transaction = transaction;
	}

	@Override
	public Object execute(ConnectionCreator creator, ConnectionCallback cb) {
		DBConnection dbcon = creator.getConnection();
		Object result = null;
		try {
			if(transaction)
				dbcon.setAutoCommit(false);
			result = cb.doInConnection(dbcon);
			if(transaction)
				dbcon.commit();
		} catch (Exception e) {
			LangUtils.throwBaseException("do In Connection error : " + e.getMessage(), e);
		}finally{
			dbcon.close();
		}
		return result;
	}
	

}