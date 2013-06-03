package org.onetwo.common.db;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.db.sqlext.SQLSymbolManagerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

/*********
 * 提供简易有明确api的查询构造器
 * 
 * @author wayshall
 *
 */
public class QueryBuilderImpl implements QueryBuilder {
	
	public static class SubQueryBuilder extends QueryBuilderImpl {

		public SubQueryBuilder() {
			super();
		}
		
	}

	/*public static QueryBuilderImpl where(){
		QueryBuilderImpl q = new QueryBuilderImpl(null);
		return q;
	}*/

	public static QueryBuilder from(Class<?> entityClass){
		QueryBuilder q = new QueryBuilderImpl(entityClass);
		return q;
	}

	public static SubQueryBuilder sub(){
		SubQueryBuilder q = new SubQueryBuilder();
		return q;
	}
	
	protected String alias;
	protected Map<Object, Object> params = new LinkedHashMap<Object, Object>();
	protected Class<?> entityClass;
	protected List<QueryBuilderJoin> leftJoins = LangUtils.newArrayList();
//	private SQLSymbolManager sqlSymbolManager = SQLSymbolManagerFactory.getInstance().getJdbc();
//	private ExtQuery extQuery;
	
//	private List<SQField> fields = new ArrayList<SQField>();
	

	protected QueryBuilderImpl(){
	}
	protected QueryBuilderImpl(Class<?> entityClass){
		this.entityClass = entityClass;
		this.alias = StringUtils.uncapitalize(entityClass.getSimpleName());
	}
	
	@Override
	public <T> T as(Class<T> queryClass){
		return (T) this;
	}
	@Override
	public Class<?> getEntityClass() {
		return entityClass;
	}

	@Override
	public QueryBuilder debug(){
		this.params.put(K.DEBUG, true);
		return this;
	}
	
	@Override
	public QueryBuilder or(QueryBuilder subQuery){
		this.checkSubQuery(subQuery);
		this.params.put(K.OR, subQuery.getParams());
		return this;
	}
	
	protected void checkSubQuery(QueryBuilder subQuery){
		if(!(subQuery instanceof SubQueryBuilder)){
			LangUtils.throwBaseException("please use SQuery.sub() method to create sub query .");
		}
	}
	
	@Override
	public QueryBuilder and(QueryBuilder subQuery){
		this.checkSubQuery(subQuery);
		this.params.put(K.AND, subQuery.getParams());
		return this;
	}
	
	@Override
	public QueryBuilder ignoreIfNull(){
		this.params.put(K.IF_NULL, K.IfNull.Ignore);
		return this;
	}
	
	@Override
	public QueryBuilder throwIfNull(){
		this.params.put(K.IF_NULL, K.IfNull.Throw);
		return this;
	}
	
	@Override
	public QueryBuilder calmIfNull(){
		this.params.put(K.IF_NULL, K.IfNull.Calm);
		return this;
	}
	
	@Override
	public DefaultQueryBuilderField field(String...fields){
		return new DefaultQueryBuilderField(this, fields);
	}
	
	@Override
	public QueryBuilder select(String...fields){
		this.params.put(K.SELECT, fields);
		return this;
	}
	
	@Override
	public QueryBuilder limit(int first, int size){
		this.params.put(K.FIRST_RESULT, first);
		this.params.put(K.MAX_RESULTS, size);
		return this;
	}
	
	@Override
	public QueryBuilder asc(String...fields){
		this.params.put(K.ASC, fields);
		return this;
	}
	
	@Override
	public QueryBuilder desc(String...fields){
		this.params.put(K.DESC, fields);
		return this;
	}
	
	@Override
	public QueryBuilder distinct(String...fields){
		this.params.put(K.DISTINCT, fields);
		return this;
	}

	@Override
	public QueryBuilder addField(QueryBuilderField field){
		this.params.put(field.getOPFields(), field.getValues());
		return this;
	}

	@Override
	public QueryBuilderJoin leftJoin(String table, String alias){
		QueryBuilderJoin join = new QueryBuilderJoin(this, table, alias);
		leftJoins.add(join);
		return join;
	}

	@Override
	public Map<Object, Object> getParams() {
		return params;
	}
	
	protected SQLSymbolManager getSQLSymbolManager(){
		SQLSymbolManager symbolManager = SQLSymbolManagerFactory.getInstance().getJdbc();
		return symbolManager;
	}
	
	protected String buildLeftJoin(){
		if(LangUtils.isEmpty(leftJoins))
			return "";
		StringBuilder leftJoinSql = new StringBuilder();
		int index = 0;
		for(QueryBuilderJoin join : leftJoins){
			if(index!=0)
				leftJoinSql.append(" ");
			leftJoinSql.append("left join ").append(join.toSql());
			index++;
		}
		return leftJoinSql.toString();
	}
	
	@Override
	public JFishQueryValue build(){
		String leftJoinSql = buildLeftJoin();
		if(StringUtils.isNotBlank(leftJoinSql)){
			params.put(K.SQL_JOIN, RawSqlWrapper.wrap(leftJoinSql));
		}
		ExtQuery extQuery = null;//new ExtQueryImpl(entityClass, null, params, getSQLSymbolManager());
		extQuery = getSQLSymbolManager().createQuery(entityClass, alias, params);
		extQuery.build();
		
		JFishQueryValue qv = JFishQueryValue.create(getSQLSymbolManager().getPlaceHolder(), extQuery.getSql());
		qv.setResultClass(extQuery.getEntityClass());
		if(extQuery.getParamsValue().isList()){
			qv.setValue(extQuery.getParamsValue().asList());
		}else{
			qv.setValue(extQuery.getParamsValue().asMap());
		}
		
		return qv;
	}

	@Override
	public <T> T one() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> List<T> list() {
		throw new UnsupportedOperationException();
	}

	public String getAlias() {
		return alias;
	}
	
}
