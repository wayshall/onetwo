package org.onetwo.common.db.generator.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
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
import java.util.List;
import java.util.Map;

import org.onetwo.common.date.DateUtil;
import org.onetwo.common.db.generator.mapping.ResultSetMapper;
import org.onetwo.common.db.generator.mapping.SimpleMetaMapping;
import org.onetwo.common.db.generator.mapping.SqlTypeMapping;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unchecked")
public class DBUtils {
	private static final Logger log = LoggerFactory.getLogger(DBUtils.class);

	private static final SqlTypeMapping SQL_TYPE_MAPPING = new SimpleMetaMapping();

	private DBUtils(){
	}
	
	public static final int TYPE_UNKNOW = SqlTypeMapping.TYPE_UNKNOW;
	
	
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
	
	/*public static void setPstmParameter(PreparedStatement preparedStatement, List values){
		int index = 0;
		for(Object value : values){
			DBUtils.setPstmParameter(preparedStatement, index+1, value);
			index++;
		}
	}*/
	
	/*public static void setPstmParameter(PreparedStatement pstm, int index, Object value){
		try {
			setPstmParameter(pstm, index, value, SQL_TYPE_MAPPING.getType(value));
		} catch (Exception e) {
			throw new ServiceException("setPstmParameter error : " + e.getMessage(), e);
		}
	}*/
	

	public static void setPstmParameterWithoutSqlType(PreparedStatement pstm, int index, Object value) {
		try {
			_setPstmParameterWithoutSqlType(pstm, index, value);
		} catch (Exception e) {
			throw new BaseException("set sql type error.", e);
		}
		
	}
	private static void _setPstmParameterWithoutSqlType(PreparedStatement pstm, int index, Object value) throws SQLException{
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
		if (requiredType == null) {
			return getResultSetValue(rs, colName);
		}
		if(requiredType.isArray())
			requiredType = requiredType.getComponentType();

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
			throw new BaseException("set bean property ["+colName+"] error!", e);
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
			throw new BaseException("set bean property ["+colName+"] error!", e);
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
	
	public static <T> List<T> toList(ResultSet rs, boolean autoClose, ResultSetMapper<T> mapper){
		List<T> datas = new ArrayList<>();
		
		try {
			T rowMap = null;
			while(rs.next()){
				rowMap = mapper.map(rs);
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
	

	public static Map<String, Object> toMap(ResultSet rs, boolean autoClose, ResultSetMapper<Map<String, Object>> mapper) {
		Map<String, Object> rowMap = null;
		try {
			rowMap = mapper.map(rs);
		} catch (Exception e) {
			handleDBException(e);
		} finally{
			if(autoClose)
				closeResultSet(rs);
		}
		return rowMap;
	}
	
	public static List<Map<String, Object>> toList(ResultSet rs, boolean autoClose, String...names){
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		try {
			Map<String, Object> rowMap = null;
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
		if(RuntimeException.class.isInstance(e)){
			throw (RuntimeException) e;
		}else if(SQLException.class.isInstance(e)){
			throw new BaseException("sql error.", e);
		}else{
			throw new BaseException("db process error.", e);
		}
	}
	
	/****
	 * autoClose is false
	 * @param rs
	 * @param names
	 * @return
	 */
	public static Map<String, Object> toMap(ResultSet rs, String...names) {
		return toMap(rs, true, names);
	}
	public static Map<String, Object> nextRowToMap(ResultSet rs, String...names) throws SQLException {
		if(rs.next()){
			return toMap(rs, true, names);
		}else{
			return Collections.EMPTY_MAP;
		}
	}
	
	public static Map<String, Object> toMap(ResultSet rs, boolean autoClose, String...names) {
		Map<String, Object> rowMap = new CaseInsensitiveMap<String, Object>();
		try {
			if(names==null || names.length==0){
				Map<String, Integer> columnNames = getColumnMeta(rs);
				Object val = null;
				for(Map.Entry<String, Integer> colName : columnNames.entrySet()){
					try {
						int index = colName.getValue()+1;
//						System.out.println("index: " + index);
						int sqlType = getColumnSqlType(rs, index);
						val = DBUtils.getValueByFieldFromResultSet(colName.getKey(), SQL_TYPE_MAPPING.getJavaType(sqlType), rs);
						rowMap.put(colName.getKey().toLowerCase(), val);
					} catch (Exception e) {
						throw new ServiceException("get value error : " + colName, e);
					}
				}
			}else{
				Collection<String> columnNames = CUtils.aslist(names);
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

	
}
