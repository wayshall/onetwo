package org.onetwo.common.db.sqlext;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.QueryField;
import org.onetwo.common.db.SelectExtQuery;

/***
 * sql操作符管理
 * 
 * @author weishao
 *
 */
public interface SQLSymbolManager {
	public static final char SPLIT_SYMBOL = QueryField.SPLIT_SYMBOL;
	
	public static class FieldOP {
		public static final String like = "like";
		public static final String not_like = "not like";
		public static final String eq = "=";
		public static final String gt = ">";
		public static final String ge = ">=";
		public static final String lt = "<";
		public static final String le = "<=";
		public static final String neq = "!=";
		public static final String neq2 = "<>";
		public static final String in = "in";
		public static final String not_in = "not in";
		public static final String date_in = "date in";
		public static final String is_null = "is null";

		public static final String in(String name){
			return qstr(name, in);
		}
		public static final String notIn(String name){
			return qstr(name, not_in);
		}
		public static final String dateIn(String name){
			return qstr(name, date_in);
		}
		public static final String qstr(String name, String op){
			return name + QueryField.SPLIT_SYMBOL + op;
		}
	}

	public SQLDialet getSqlDialet();

	public SQLSymbolManager register(String symbol, HqlSymbolParser parser);
	public SQLSymbolManager register(HqlSymbolParser parser);
	
	public HqlSymbolParser getHqlSymbolParser(String symbol);
//	public String createHql(Map<Object, Object> properties, List<Object> values) ;
	public PlaceHolder getPlaceHolder();
	
	public ExtQuery createDeleteQuery(Class<?> entityClass, Map<Object, Object> properties);
	
	public SelectExtQuery createSelectQuery(Class<?> entityClass, Map<Object, Object> properties);
	public SelectExtQuery createSelectQuery(Class<?> entityClass, String alias, Map<Object, Object> properties);
	
	public void setListeners(List<ExtQueryListener> listeners);
	
}
