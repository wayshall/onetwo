package org.onetwo.common.db.wheel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class DBValueHandlerSetter implements PreparedStatementSetter, WheelAware {
	
	private Wheel wheel;
	private Map<Class<?>, DBValueHanlder> dbValueHandlers = DBUtils.DBVALUE_HANDLERS;

	public void setParameter(PreparedStatement preStatement, int index, Object value) throws SQLException{
		Class<?> type = value==null?null:value.getClass();
		getValueHandler(type).setValue(preStatement, value, index);
	}

	@Override
	public void setWheel(Wheel wheel) {
		this.wheel = wheel;
	}
	
	public DBValueHanlder getValueHandler(Class<?> clazz){
		if(wheel!=null){
			return wheel.getValueHandler(clazz);
		}
		if(!dbValueHandlers.containsKey(clazz)){
			clazz = Object.class;
		}
		return dbValueHandlers.get(clazz);
	}
	
	
}
