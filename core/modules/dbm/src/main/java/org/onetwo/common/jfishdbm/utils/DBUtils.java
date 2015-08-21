package org.onetwo.common.jfishdbm.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.jfishdbm.exception.DBException;
import org.onetwo.common.jfishdbm.exception.QueryException;
import org.onetwo.common.jfishdbm.mapping.DBValueHanlder;
import org.onetwo.common.jfishdbm.mapping.ResultSetMapper;
import org.onetwo.common.jfishdbm.mapping.SqlTypeFactory;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.map.BaseMap;
import org.onetwo.common.utils.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unchecked")
public class DBUtils {
	private static final Logger log = LoggerFactory.getLogger(DBUtils.class);

	public static final DBValueHanlder NullHandler = new DBValueHanlder(){

		@Override
		public Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException {
			return null;
		}

		@Override
		public void setValue(PreparedStatement stat, Object obj, int parameterIndex) throws SQLException {
			stat.setNull(parameterIndex, Types.NULL);
		}
		
	};
	public static final DBValueHanlder ShortHandler = new DBValueHanlder(){

		@Override
		public Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException {
			return rs.getShort(colName);
		}

		@Override
		public void setValue(PreparedStatement stat, Object obj, int parameterIndex) throws SQLException {
			if(obj instanceof Number){
				stat.setShort(parameterIndex, ((Number)obj).shortValue());
			}else{
				stat.setShort(parameterIndex, (Short)obj);
			}
		}
		
	};
	public static final DBValueHanlder IntHandler = new DBValueHanlder(){

		@Override
		public Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException {
			return rs.getInt(colName);
		}

		@Override
		public void setValue(PreparedStatement stat, Object obj, int parameterIndex) throws SQLException {
			if(obj instanceof Number){
				stat.setInt(parameterIndex, ((Number)obj).shortValue());
			}else{
				stat.setInt(parameterIndex, (Short)obj);
			}
		}
		
	};
	public static final DBValueHanlder LongHandler = new DBValueHanlder(){

		@Override
		public Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException {
			return rs.getLong(colName);
		}

		@Override
		public void setValue(PreparedStatement stat, Object obj, int parameterIndex) throws SQLException {
			if(obj instanceof Number){
				stat.setLong(parameterIndex, ((Number)obj).longValue());
			}else{
				stat.setLong(parameterIndex, (Long)obj);
			}
		}
		
	};
	public static final DBValueHanlder FloatHandler = new DBValueHanlder(){

		@Override
		public Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException {
			return rs.getFloat(colName);
		}

		@Override
		public void setValue(PreparedStatement stat, Object obj, int parameterIndex) throws SQLException {
			if(obj instanceof Number){
				stat.setFloat(parameterIndex, ((Number)obj).floatValue());
			}else{
				stat.setFloat(parameterIndex, (Float)obj);
			}
		}
		
	};
	public static final DBValueHanlder DoubleHandler = new DBValueHanlder(){

		@Override
		public Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException {
			return rs.getDouble(colName);
		}

		@Override
		public void setValue(PreparedStatement stat, Object obj, int parameterIndex) throws SQLException {
			if(obj instanceof Number){
				stat.setDouble(parameterIndex, ((Number)obj).doubleValue());
			}else{
				stat.setDouble(parameterIndex, (Double)obj);
			}
		}
		
	};
	public static final DBValueHanlder BigDecimalHandler = new DBValueHanlder(){

		@Override
		public Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException {
			return rs.getBigDecimal(colName);
		}

		@Override
		public void setValue(PreparedStatement stat, Object obj, int parameterIndex) throws SQLException {
			stat.setBigDecimal(parameterIndex, (BigDecimal)obj);
		}
		
	};
	public static final DBValueHanlder StringHandler = new DBValueHanlder(){

		@Override
		public Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException {
			return rs.getString(colName);
		}

		@Override
		public void setValue(PreparedStatement stat, Object obj, int parameterIndex) throws SQLException {
			if(obj instanceof String){
				stat.setString(parameterIndex, (String)obj);
			}else{
				stat.setString(parameterIndex, obj.toString());
			}
		}
		
	};
	public static final DBValueHanlder BooleanHandler = new DBValueHanlder(){

		@Override
		public Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException {
			return rs.getBoolean(colName);
		}

		@Override
		public void setValue(PreparedStatement stat, Object obj, int parameterIndex) throws SQLException {
			stat.setBoolean(parameterIndex, (Boolean)obj);
		}
		
	};
	public static final DBValueHanlder DateHandler = new DBValueHanlder(){

		@Override
		public Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException {
//			java.sql.Timestamp ts = rs.getTimestamp(colName);
//			Object ts = rs.getObject(colName);
			/*if(ts==null)
				return null;*/
			if(toType==null || toType==Date.class){
				java.sql.Timestamp ts = rs.getTimestamp(colName);
				if(ts==null)
					return null;
				return new Date(ts.getTime());
			}else if(toType==java.sql.Date.class){
				return rs.getDate(colName);
			}else if(toType==java.sql.Time.class){
				return rs.getTime(colName);
			}else if(toType==java.sql.Timestamp.class){
				return rs.getTimestamp(colName);
			}else if(toType==Calendar.class){
				java.sql.Timestamp ts = rs.getTimestamp(colName);
				if(ts==null)
					return null;
				Calendar cal = Calendar.getInstance();
				cal.setTime(ts);
				return cal;
			}
			throw LangUtils.asBaseException("unsupported type : " + toType);
		}

		@Override
		public void setValue(PreparedStatement stat, Object obj, int parameterIndex) throws SQLException {
			if(obj instanceof java.sql.Date){
				stat.setDate(parameterIndex, (java.sql.Date)obj);
			}else if(obj instanceof java.sql.Time){
				stat.setTime(parameterIndex, (java.sql.Time)obj);
			}else if(obj instanceof java.sql.Timestamp){
				stat.setTimestamp(parameterIndex, (java.sql.Timestamp)obj);
			}else{
				stat.setTimestamp(parameterIndex, new java.sql.Timestamp(((Date)obj).getTime()));
			}
		}
		
	};
	public static final DBValueHanlder DefHandler = new DBValueHanlder(){

		@Override
		public Object getValue(ResultSet rs, String colName, Class<?> toType) throws SQLException {
			return DBUtils.getValueByFieldFromResultSet(colName, toType, rs);
		}

		@Override
		public void setValue(PreparedStatement stat, Object obj, int parameterIndex) throws SQLException {
//			stat.setObject(parameterIndex, obj);
			DBUtils.setPstmParameterWithoutSqlType(stat, parameterIndex, obj);
		}
		
	};
	
	public static final Map<Class, DBValueHanlder> DBVALUE_HANDLERS;
	
	static {
		Map<Class, DBValueHanlder> temp = new HashMap<Class, DBValueHanlder>();

		temp.put(Integer.class, IntHandler);
		temp.put(int.class, IntHandler);
		temp.put(Short.class, ShortHandler);
		temp.put(short.class, ShortHandler);
		temp.put(Long.class, LongHandler);
		temp.put(long.class, LongHandler);
		temp.put(Boolean.class, BooleanHandler);
		temp.put(boolean.class, BooleanHandler);
		temp.put(Float.class, FloatHandler);
		temp.put(float.class, FloatHandler);
		temp.put(Double.class, DoubleHandler);
		temp.put(double.class, DoubleHandler);
		temp.put(BigDecimal.class, BigDecimalHandler);
		temp.put(String.class, StringHandler);
		temp.put(Date.class, DateHandler);
		temp.put(java.sql.Date.class, DateHandler);
		temp.put(java.sql.Time.class, DateHandler);
		temp.put(java.sql.Timestamp.class, DateHandler);
		temp.put(Date.class, DateHandler);
		temp.put(Object.class, DefHandler);
		temp.put(null, NullHandler);
		
		DBVALUE_HANDLERS = Collections.unmodifiableMap(temp);
	}
	
	private DBUtils(){
	}
	
	public static final int TYPE_UNKNOW = Integer.MIN_VALUE;
	
	public static DBValueHanlder getValueHandler(Class<?> clazz){
		if(!DBVALUE_HANDLERS.containsKey(clazz)){
			clazz = Object.class;
		}
		DBValueHanlder valueHandler = DBVALUE_HANDLERS.get(clazz);
		return valueHandler;
	}

	public static DatabaseMetaData getMetaData(Connection connection) {
		try {
			return connection.getMetaData();
		} catch (SQLException e) {
			throw new ServiceException("get DatabaseMetaData error!", e);
		}
	}
	
	/**
	 * @param pstm
	 * @param index
	 * @param value
	 * @param sqlType
	 * @throws SQLException
	 */
	public static void setPstmParameter(PreparedStatement pstm, int index, Object value, int sqlType) throws SQLException{
		if(value==null){
			if(sqlType==TYPE_UNKNOW){
				pstm.setNull(index, Types.NULL);
			}
			else{
				pstm.setNull(index, sqlType);
			}
		}
		else{
			if(sqlType==Types.VARCHAR){
				pstm.setString(index, value.toString());
			}
			else if(sqlType==Types.DECIMAL || sqlType==Types.NUMERIC){
				if(value instanceof BigDecimal){
					pstm.setBigDecimal(index, (BigDecimal)value);
				}
				else{
					pstm.setObject(index, value, sqlType);
				}
			}
			else if(sqlType==Types.DATE){
				if(value instanceof Date){
					pstm.setDate(index, new java.sql.Date(((Date)value).getTime()));
				}
				else if(value instanceof Calendar){
					pstm.setDate(index, new java.sql.Date(((Calendar)value).getTime().getTime()));
				}
				else{
					pstm.setObject(index, value, Types.DATE);
				}
			}
			else if(sqlType==Types.TIME){
				if(value instanceof Date){
					pstm.setTime(index, new java.sql.Time(((Date)value).getTime()));
				}
				else if(value instanceof Calendar){
					pstm.setTime(index, new java.sql.Time(((Calendar)value).getTime().getTime()));
				}
				else{
					pstm.setObject(index, value, Types.TIME);
				}
			}
			else if(sqlType==Types.TIMESTAMP){
				if(value instanceof Date){
					pstm.setTimestamp(index, new java.sql.Timestamp(((Date)value).getTime()));
				}
				else if(value instanceof Calendar){
					pstm.setTimestamp(index, new java.sql.Timestamp(((Calendar)value).getTime().getTime()));
				}
				else{
					pstm.setObject(index, value, Types.TIMESTAMP);
				}
			}
			else if(sqlType==TYPE_UNKNOW){
				if(value instanceof String){
					pstm.setString(index, value.toString());
				}
				else if(isDateValue(value)){
					pstm.setTimestamp(index, new java.sql.Timestamp(((Date)value).getTime()));
				}
				else{
					pstm.setObject(index, value);
				}
			}
			else{
				pstm.setObject(index, value, sqlType);
			}
		}
	}
	
	public static void setPstmParameter(PreparedStatement preparedStatement, List values){
		int index = 0;
		for(Object value : values){
			DBUtils.setPstmParameter(preparedStatement, index+1, value);
			index++;
		}
	}
	
	public static void setPstmParameter(PreparedStatement pstm, int index, Object value){
		try {
			setPstmParameter(pstm, index, value, SqlTypeFactory.getType(value));
		} catch (Exception e) {
			throw new ServiceException("setPstmParameter error : " + e.getMessage(), e);
		}
	}
	
	public static void setPstmParameterWithoutSqlType(PreparedStatement pstm, int index, Object value) throws SQLException{
		if(value==null){
			pstm.setNull(index, Types.NULL);
		}
		else{
			if(value instanceof String){
				pstm.setString(index, value.toString());
			}
			else if(value instanceof BigDecimal){
				pstm.setBigDecimal(index, (BigDecimal)value);
			}
			else if(value instanceof Integer){
				pstm.setInt(index, ((Integer)value).intValue());
			}
			else if(value instanceof Float){
				pstm.setFloat(index, ((Float)value).floatValue());
			}
			else if(value instanceof Double){
				pstm.setDouble(index, ((Double)value).doubleValue());
			}
			else if(isDateValue(value)){
				pstm.setTimestamp(index, new java.sql.Timestamp(((java.util.Date)value).getTime()));
			}
			else if(value instanceof java.util.Calendar){
				pstm.setTimestamp(index, new java.sql.Timestamp(((java.util.Calendar)value).getTime().getTime()));
			}
			else if(value instanceof java.sql.Date){
				pstm.setDate(index, new java.sql.Date(((java.sql.Date)value).getTime()));
			}
			else if(value instanceof java.sql.Time){
				pstm.setTime(index, new java.sql.Time(((java.sql.Date)value).getTime()));
			}
			else if(value instanceof java.sql.Timestamp){
				pstm.setTimestamp(index, new java.sql.Timestamp(((java.util.Date)value).getTime()));
			}
			else{
				pstm.setObject(index, value);
			}
		}
	}
	
	
	
	public static String addStatementParameter(String column, Object value){
		if(column==null || column.length()<1 || value==null)
			return "";
		StringBuffer sql = new StringBuffer();
		sql.append(column);
		sql.append("=");
		sql.append(getValueSQLString(value));
		
		return sql.toString();
	}
	
	public static String getValueSQLString(Object value){
		if(value==null)
			return "''";
		StringBuffer sql = new StringBuffer();
		if(value instanceof Number){
			sql.append(value.toString());
		}
		else if(isDateValue(value) || value instanceof java.sql.Timestamp){
			sql.append("'");
			sql.append(DateUtil.formatDateTime((Date)value));
			sql.append("'");
		}
		else if(value instanceof java.sql.Date){
			sql.append("'");
			sql.append(DateUtil.formatDate((Date)value));
			sql.append("'");
		}
		else if(value instanceof java.sql.Time){
			sql.append("'");
			sql.append(DateUtil.formatTime((Date)value));
			sql.append("'");
		}
		else {
			sql.append("'");
			sql.append(value.toString().replace("'", ""));
			sql.append("'");
		}
		
		return sql.toString();
	}
	
	public static Object getResultSetValue(ResultSet rs, String colName) throws SQLException {
		Object obj = rs.getObject(colName);
		if (obj instanceof Blob) {
			obj = rs.getBytes(colName);
		}
		else if (obj instanceof Clob) {
			obj = rs.getString(colName);
		}
		else if (obj != null && obj.getClass().getName().startsWith("oracle.sql.TIMESTAMP")) {
			obj = rs.getTimestamp(colName);
		}
		else if (obj != null && obj.getClass().getName().startsWith("oracle.sql.DATE")) {
			obj = rs.getDate(colName);
		}
		else if (obj != null && obj instanceof java.sql.Date) {
			obj = rs.getDate(colName);
		}
		return obj;
	}
	
	public static Object getValueByFieldFromResultSet(String colName, Class requiredType, ResultSet rs) throws SQLException{
		if(requiredType.isArray())
			requiredType = requiredType.getComponentType();
		if (requiredType == null) {
			return getResultSetValue(rs, colName);
		}

		Object value = null;
		boolean wasNullCheck = false;

		// Explicitly extract typed value, as far as possible.
		if (String.class.equals(requiredType)) {
			value = rs.getString(colName);
		}
		else if (boolean.class.equals(requiredType) || Boolean.class.equals(requiredType)) {
			value = Boolean.valueOf(rs.getBoolean(colName));
			wasNullCheck = true;
		}
		else if (byte.class.equals(requiredType) || Byte.class.equals(requiredType)) {
			value = new Byte(rs.getByte(colName));
			wasNullCheck = true;
		}
		else if (short.class.equals(requiredType) || Short.class.equals(requiredType)) {
			value = new Short(rs.getShort(colName));
			wasNullCheck = true;
		}
		else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
			value = new Integer(rs.getInt(colName));
			wasNullCheck = true;
		}
		else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
			value = new Long(rs.getLong(colName));
			wasNullCheck = true;
		}
		else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
			value = new Float(rs.getFloat(colName));
			wasNullCheck = true;
		}
		else if (double.class.equals(requiredType) || Double.class.equals(requiredType) ||
				Number.class.equals(requiredType)) {
			value = new Double(rs.getDouble(colName));
			wasNullCheck = true;
		}
		else if (byte[].class.equals(requiredType)) {
			value = rs.getBytes(colName);
		}
		else if (java.sql.Date.class.equals(requiredType)) {
			value = rs.getDate(colName);
		}
		else if (java.sql.Time.class.equals(requiredType)) {
			value = rs.getTime(colName);
		}
		else if (java.sql.Timestamp.class.equals(requiredType) || java.util.Date.class.equals(requiredType)) {
			value = rs.getTimestamp(colName);
		}
		else if (BigDecimal.class.equals(requiredType)) {
			value = rs.getBigDecimal(colName);
		}
		else if (Blob.class.equals(requiredType)) {
			value = rs.getBlob(colName);
		}
		else if (Clob.class.equals(requiredType)) {
			value = rs.getClob(colName);
		}
		else {
			// Some unknown type desired -> rely on getObject.
			value = getResultSetValue(rs, colName);
		}

		// Perform was-null check if demanded (for results that the
		// JDBC driver returns as primitives).
		if (wasNullCheck && value != null && rs.wasNull()) {
			value = null;
		}
		return value;
	}
	
	
	public static boolean isDateValue(Object inValue) {
		return (inValue instanceof java.util.Date && !(inValue instanceof java.sql.Date ||
				inValue instanceof java.sql.Time || inValue instanceof java.sql.Timestamp));
	}
	
	public static void setObjectFromResutlSetByRow(ResultSet rs, Object bean){
		Field[] fields = bean.getClass().getDeclaredFields();
		String colName = "";
		try{
			for(int i=0; i<fields.length; i++){
				Field f = fields[i];
				Class<?> type = f.getType();
				colName = f.getName();
				Object value = DBUtils.getValueByFieldFromResultSet(colName, type, rs);
				if(value!=null){
					MyUtils.setValue(bean, colName, value);
				}
			}
		}catch(Exception e){
			throw new DBException("set bean property ["+colName+"] error!", e);
		}
	}
	
	public static void setBeanFromResutlSetByRow(ResultSet rs, Object bean){
		String colName = null;
		try{
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
			for(PropertyDescriptor prop : props){
				Class<?> type = prop.getPropertyType();
				colName = prop.getName();
				Object value = DBUtils.getValueByFieldFromResultSet(colName, type, rs);
				if(value!=null){
					MyUtils.setValue(bean, colName, value);
				}
			}
		}catch(Exception e){
			throw new DBException("set bean property ["+colName+"] error!", e);
		}
	}

	public static void close(Connection con, Statement stm, ResultSet rs) {
		closeResultSet(rs);
		closeStament(stm);
		closeCon(con);
	}

	public static void closeCon(Connection con) {
		try {
			if (con != null)
				con.close();
		} catch (SQLException se) {
			log.error("close connection error!", se);
		}
	}

	public static void closeStament(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException se) {
			log.error("close Statement error!", se);
		}
	}

	public static void closePreparedStatement(Statement prestmt) {
		try {
			if (prestmt != null)
				prestmt.close();
		} catch (SQLException se) {
			log.error("close PreparedStatement error!", se);
		}
	}

	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException se) {
			log.error("close ResultSet error!", se);
		}
	}
	
	public static List<BaseMap> toList(ResultSet rs, boolean autoClose, ResultSetMapper mapper){
		List<BaseMap> datas = new ArrayList<BaseMap>();
		
		try {
			CaseInsensitiveMap<String, Object> rowMap = null;
			while(rs.next()){
				rowMap = toMap(rs, false, mapper);
				if(rowMap!=null)
					datas.add(rowMap);
			}
		} catch (Exception e) {
			throw LangUtils.asServiceException(e);
		} finally{
			if(autoClose)
				closeResultSet(rs);
		}
		return datas;
	}
	

	public static CaseInsensitiveMap toMap(ResultSet rs, boolean autoClose, ResultSetMapper mapper) {
		CaseInsensitiveMap rowMap = new CaseInsensitiveMap();
		try {
			rowMap = mapper.map(rs, rowMap);
		} catch (Exception e) {
			throw LangUtils.asServiceException(e);
		} finally{
			if(autoClose)
				closeResultSet(rs);
		}
		return rowMap;
	}
	
	public static List<Map> toList(ResultSet rs, boolean autoClose, String...names){
		List<Map> datas = new ArrayList<Map>();
		
		try {
			CaseInsensitiveMap<String, Object> rowMap = null;
			while(rs.next()){
				rowMap = toMap(rs, false, names);
				if(rowMap!=null)
					datas.add(rowMap);
			}
		} catch (Exception e) {
			handleDBException(e);
		} finally{
			if(autoClose)
				closeResultSet(rs);
		}
		return datas;
	}
	
	public static void handleDBException(Exception e){
		LangUtils.throwBaseException(e);
	}
	
	/****
	 * autoClose is false
	 * @param rs
	 * @param names
	 * @return
	 */
	public static CaseInsensitiveMap toMap(ResultSet rs, String...names) {
		return toMap(rs, false, names);
	}
	
	public static CaseInsensitiveMap toMap(ResultSet rs, boolean autoClose, String...names) {
		CaseInsensitiveMap rowMap = new CaseInsensitiveMap<String, Object>();
		try {
			if(names==null || names.length==0){
				Map<String, Integer> columnNames = getColumnMeta(rs);
				Object val = null;
				for(Map.Entry<String, Integer> colName : columnNames.entrySet()){
					try {
						int index = colName.getValue()+1;
//						System.out.println("index: " + index);
						int sqlType = getColumnSqlType(rs, index);
						val = DBUtils.getValueByFieldFromResultSet(colName.getKey(), SqlTypeFactory.getJavaType(sqlType), rs);
						rowMap.put(colName.getKey().toLowerCase(), val);
					} catch (Exception e) {
						throw new ServiceException("get value error : " + colName, e);
					}
				}
			}else{
				Collection<String> columnNames = null;
				columnNames = MyUtils.asList(names);
				Object val = null;
				for(String colName : columnNames){
					try {
						val = DBUtils.getResultSetValue(rs, colName);
						rowMap.put(colName.toLowerCase(), val);
					} catch (Exception e) {
						throw new ServiceException("get value error : " + colName, e);
					}
				}
			}
		} catch (Exception e) {
			handleDBException(e);
		} finally{
			if(autoClose)
				closeResultSet(rs);
		}
		return rowMap;
	}
	
	public static Map<String, Integer> getColumnMeta(ResultSet rs) throws SQLException{
		ResultSetMetaData rsMeta = rs.getMetaData();
		int colCount = rsMeta.getColumnCount();
		Map<String, Integer> column = new CaseInsensitiveMap<String, Integer>();
		for(int i=1; i<=colCount; i++){
			column.put(rsMeta.getColumnName(i), i-1);
		}
		return column;
	}
	
	public static String getColumnName(ResultSet rs, int colIndex) throws SQLException{
		ResultSetMetaData rsMeta = rs.getMetaData();
		return rsMeta.getColumnName(colIndex);
	}
	
	public static int getColumnSqlType(ResultSet rs, int columnIndex) throws SQLException{
		ResultSetMetaData rsMeta = rs.getMetaData();
		return rsMeta.getColumnType(columnIndex);
	}

	public static DBException asDBException(String msg){
		return asDBException(msg, null);
	}
	
	public static DBException asDBException(String msg, Exception e){
		if(e instanceof DBException)
			return (DBException) e;
		DBException dbe = null;
		if(msg==null)
			dbe = new DBException(e.getMessage(), e);
		else
			dbe = new DBException(msg, e);
		return dbe;
	}
	public static QueryException asQueryException(String msg){
		return asQueryException(msg, null);
	}
	
	public static QueryException asQueryException(String msg, Exception e){
		if(e instanceof QueryException)
			return (QueryException) e;
		QueryException qe = null;
		if(msg==null)
			qe = new QueryException(e.getMessage(), e);
		else
			qe = new QueryException(msg, e);
		return qe;
	}
	
	public static void throwDBException(String msg){
		throw asDBException(msg);
	}
	public static void throwDBException(String msg, Exception e){
		throw asDBException(msg, e);
	}
	
	public static void throwQueryException(String msg){
		throw asQueryException(msg);
	}
	public static void throwQueryException(String msg, Exception e){
		throw asQueryException(msg, e);
	}
	
	public static void main(String[] args){
		DBValueHanlder h = DBVALUE_HANDLERS.get(null);
		System.out.println("h:" + (h==NullHandler));
	}
	
}
