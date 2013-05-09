package org.onetwo.common.db.sqlext;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.ExtQueryImpl;
import org.onetwo.common.db.ParamValues;
import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.exception.ServiceException;


/***
 * SqlSymbolManager的抽象实现
 * @author weishao
 *
 */
public class DefaultSQLSymbolManagerImpl implements SQLSymbolManager {
	
	public static SQLSymbolManager create(){
		DefaultSQLSymbolManagerImpl sql = new DefaultSQLSymbolManagerImpl(new DefaultSQLDialetImpl(), PlaceHolder.POSITION);
		return sql;
	}
	
	protected Map<String, HqlSymbolParser> parser;
	private SQLDialet sqlDialet;
	private PlaceHolder placeHolder;
	

	public DefaultSQLSymbolManagerImpl(SQLDialet sqlDialet) {
		this(sqlDialet, ParamValues.PlaceHolder.NAMED);
	}

	public DefaultSQLSymbolManagerImpl(SQLDialet sqlDialet, PlaceHolder placeHolder) {
		parser = new HashMap<String, HqlSymbolParser>();
		this.sqlDialet = sqlDialet;
		this.placeHolder = placeHolder;
		this.initParser();
	}

	public SQLDialet getSqlDialet() {
		return sqlDialet;
	}

	public PlaceHolder getPlaceHolder() {
		return placeHolder;
	}

	
	@Override
	public ExtQuery createQuery(Class<?> entityClass, Map<Object, Object> properties) {
		return createQuery(entityClass, null, properties);
	}

	@Override
	public ExtQuery createQuery(Class<?> entityClass, String alias, Map<Object, Object> properties){
		ExtQuery q = new ExtQueryImpl(entityClass, alias, properties, this);
		return q;
	}

	/***
	 * 注册所有查询接口所支持的操作符
	 * @return
	 */
	public SQLSymbolManager initParser() {
		register(FieldOP.eq, new CommonSQLSymbolParser(this, "=", "is"))
		.register(FieldOP.is_null, new BooleanValueSQLSymbolParser(this, "is null", "is not null"))
		.register(FieldOP.gt, new CommonSQLSymbolParser(this, ">"))
		.register(FieldOP.ge, new CommonSQLSymbolParser(this, ">="))
		.register(FieldOP.lt, new CommonSQLSymbolParser(this, "<"))
		.register(FieldOP.le, new CommonSQLSymbolParser(this, "<="))
		.register(FieldOP.neq, new CommonSQLSymbolParser(this, "<>", "is not"))
		.register(FieldOP.neq2, new CommonSQLSymbolParser(this, "<>", "is not"))
		.register(FieldOP.like, new CommonSQLSymbolParser(this, "like", true))
		.register(FieldOP.not_like, new CommonSQLSymbolParser(this, "not like", true))
		.register(FieldOP.in, new InSymbolParser(this, "in"))
		.register(FieldOP.not_in, new InSymbolParser(this, "not in"))
		.register(FieldOP.date_in, new DateRangeSymbolParser(this));
		return this;
	}

	public HqlSymbolParser getHqlSymbolParser(String symbol) {
		HqlSymbolParser parser = this.parser.get(symbol);
		if (parser == null)
			throw new ServiceException("do not support symbol : [" + symbol+"]");
		return parser;
	}

	/****
	 * 注册操作符和对应的解释类
	 */
	public SQLSymbolManager register(String symbol, HqlSymbolParser parser) {
		this.parser.put(symbol, parser);
		return this;
	}

}
