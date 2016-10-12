package org.onetwo.common.db.sqlext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Assert;


/***
 * SqlSymbolManager的抽象实现
 * @author weishao
 *
 */
public class DefaultSQLSymbolManagerImpl implements SQLSymbolManager {
	
	public static SQLSymbolManager create(){
//		DefaultSQLSymbolManagerImpl sql = new DefaultSQLSymbolManagerImpl(new DefaultSQLDialetImpl(), PlaceHolder.POSITION);
		DefaultSQLSymbolManagerImpl sql = new DefaultSQLSymbolManagerImpl(new DefaultExtQueryDialetImpl());
		return sql;
	}
	
	protected Map<String, HqlSymbolParser> parser;
	private ExtQueryDialet sqlDialet;
//	private PlaceHolder placeHolder;
	
	protected List<ExtQueryListener> listeners;
	

	/*public DefaultSQLSymbolManagerImpl(SQLDialet sqlDialet) {
		this(sqlDialet, ParamValues.PlaceHolder.NAMED);
	}*/

	public DefaultSQLSymbolManagerImpl(ExtQueryDialet sqlDialet) {
		parser = new HashMap<String, HqlSymbolParser>();
		this.sqlDialet = sqlDialet;
		this.initParser();
	}

	public ExtQueryDialet getSqlDialet() {
		return sqlDialet;
	}

	/*public PlaceHolder getPlaceHolder() {
		return placeHolder;
	}*/

	@Override
	public ExtQuery createDeleteQuery(Class<?> entityClass, Map<Object, Object> properties) {
		ExtQuery q = new DeleteExtQueryImpl(entityClass, null, properties, this, this.listeners);
		q.initQuery();
		return q;
	}
	@Override
	public SelectExtQuery createSelectQuery(Class<?> entityClass, Map<Object, Object> properties) {
		return createSelectQuery(entityClass, null, properties);
	}

	@Override
	public SelectExtQuery createSelectQuery(Class<?> entityClass, String alias, Map<Object, Object> properties){
		SelectExtQuery q = new SelectExtQueryImpl(entityClass, alias, properties, this, this.listeners);
		q.initQuery();
		return q;
	}

	/***
	 * 注册所有查询接口所支持的操作符
	 * @return
	 */
	public SQLSymbolManager initParser() {
		register(new CommonSQLSymbolParser(this, FieldOP.eq))
		.register(new BooleanValueSQLSymbolParser(this, FieldOP.is_null, "is null", "is not null"))
		.register(new CommonSQLSymbolParser(this, FieldOP.gt))
		.register(new CommonSQLSymbolParser(this, FieldOP.ge))
		.register(new CommonSQLSymbolParser(this, FieldOP.lt))
		.register(new CommonSQLSymbolParser(this, FieldOP.le))
		.register(new CommonSQLSymbolParser(this, FieldOP.neq))
		.register(new CommonSQLSymbolParser(this, FieldOP.neq2))
//		.register(new CommonSQLSymbolParser(this, FieldOP.like, true))
//		.register(new CommonSQLSymbolParser(this, FieldOP.not_like, true))
		.register(new LikeSQLSymbolParser(this, FieldOP.like))
		.register(new LikeSQLSymbolParser(this, FieldOP.like2, FieldOP.like))
		.register(new LikeSQLSymbolParser(this, FieldOP.not_like))
		.register(new LikeSQLSymbolParser(this, FieldOP.not_like2, FieldOP.not_like))
		.register(new InSymbolParser(this, FieldOP.in))
		.register(new InSymbolParser(this, FieldOP.not_in))
		.register(new DateRangeSymbolParser(this, FieldOP.date_in));
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
		Assert.notNull(parser);
		this.parser.put(symbol, parser);
		return this;
	}

	@Override
	public SQLSymbolManager register(HqlSymbolParser parser) {
		Assert.notNull(parser);
		this.parser.put(parser.getMappedOperator(), parser);
		return this;
	}

	public void setListeners(List<ExtQueryListener> listeners) {
		this.listeners = listeners;
	}

	protected List<ExtQueryListener> getListeners() {
		return listeners;
	}

}
