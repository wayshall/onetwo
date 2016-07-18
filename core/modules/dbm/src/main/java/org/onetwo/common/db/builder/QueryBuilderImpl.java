package org.onetwo.common.db.builder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.InnerBaseEntityManager;
import org.onetwo.common.db.RawSqlWrapper;
import org.onetwo.common.db.sqlext.ExtQuery;
import org.onetwo.common.db.sqlext.ExtQuery.K;
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

	/*public static QueryBuilder<QueryBuilderImpl> from(Class<?> entityClass){
		return QueryBuilderCreator.from(entityClass);
	}*/

	public static SubQueryBuilder sub(){
		SubQueryBuilder q = new SubQueryBuilder();
		return q;
	}

	protected InnerBaseEntityManager baseEntityManager;
	protected String alias;
	protected Map<Object, Object> params = new LinkedHashMap<Object, Object>();
	protected Class<?> entityClass;
	protected List<QueryBuilderJoin> leftJoins = LangUtils.newArrayList();
//	private SQLSymbolManager sqlSymbolManager = SQLSymbolManagerFactory.getInstance().getJdbc();
//	private ExtQuery extQuery;
	
//	private List<SQField> fields = new ArrayList<SQField>();
//	private ExtQuery extQuery;
	
	protected QueryBuilderImpl(){
	}

	protected QueryBuilderImpl(InnerBaseEntityManager baseEntityManager, Class<?> entityClass){
		this.entityClass = entityClass;
		this.alias = StringUtils.uncapitalize(entityClass.getSimpleName());
		this.baseEntityManager = baseEntityManager;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T as(Class<T> queryClass){
		return (T) this;
	}
	@Override
	public Class<?> getEntityClass() {
		return entityClass;
	}
	
	protected QueryBuilderImpl self(){
		return (QueryBuilderImpl)this;
	}
	
	public WhereCauseBuilder where(){
		return new DefaultWhereCauseBuilder(this);
	}

	/*@Override
	public QueryBuilderImpl debug(){
		this.params.put(K.DEBUG, true);
		return self();
	}
	
	@Override
	public QueryBuilderImpl or(QueryBuilder subQuery){
		this.checkSubQuery(subQuery);
		this.params.put(K.OR, subQuery.getParams());
		return self();
	}*/
	
	protected void checkSubQuery(QueryBuilder subQuery){
		if(!(subQuery instanceof SubQueryBuilder)){
			LangUtils.throwBaseException("please use SQuery.sub() method to create sub query .");
		}
	}
	
	/*@Override
	public QueryBuilderImpl and(QueryBuilder subQuery){
		this.checkSubQuery(subQuery);
		this.params.put(K.AND, subQuery.getParams());
		return self();
	}
	
	@Override
	public QueryBuilderImpl ignoreIfNull(){
		this.params.put(K.IF_NULL, K.IfNull.Ignore);
		return self();
	}
	
	@Override
	public QueryBuilderImpl throwIfNull(){
		this.params.put(K.IF_NULL, K.IfNull.Throw);
		return self();
	}
	
	@Override
	public QueryBuilderImpl calmIfNull(){
		this.params.put(K.IF_NULL, K.IfNull.Calm);
		return self();
	}*/
	
	/*@Override
	public DefaultQueryBuilderField field(String...fields){
//		this.throwIfHasBuild();
		return new DefaultQueryBuilderField(this, fields);
	}*/
	
	@Override
	public QueryBuilderImpl select(String...fields){
		this.params.put(K.SELECT, fields);
		return self();
	}
	
	@Override
	public QueryBuilderImpl limit(int first, int size){
		this.params.put(K.FIRST_RESULT, first);
		this.params.put(K.MAX_RESULTS, size);
		return self();
	}
	
	@Override
	public QueryBuilderImpl asc(String...fields){
		this.params.put(K.ASC, fields);
		return self();
	}
	
	@Override
	public QueryBuilderImpl desc(String...fields){
		this.params.put(K.DESC, fields);
		return self();
	}
	
	@Override
	public QueryBuilderImpl distinct(String...fields){
		this.params.put(K.DISTINCT, fields);
		return self();
	}

	/*@Override
	public QueryBuilderImpl addField(QueryBuilderField field){
		this.params.put(field.getOPFields(), field.getValues());
		return self();
	}*/

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
	public QueryAction toQuery(){
//		this.throwIfHasBuild();
		return createQueryAction();
	}
	
	/*public ParamValues getParamValues(){
		return extQuery.getParamsValue();
	}
	
	public String getSql(){
		return extQuery.getSql();
	}*/
	
	protected QueryAction createQueryAction(){
		String leftJoinSql = buildLeftJoin();
		if(StringUtils.isNotBlank(leftJoinSql)){
			params.put(K.SQL_JOIN, RawSqlWrapper.wrap(leftJoinSql));
		}
		/*ExtQuery extQuery = null;//new ExtQueryImpl(entityClass, null, params, getSQLSymbolManager());
		extQuery = createExtQuery(entityClass, alias, params);
		extQuery.build();*/
		
		QueryActionImpl queryAction = new QueryActionImpl(baseEntityManager, entityClass, leftJoinSql, params);
		
		/*JFishQueryValue qv = JFishQueryValue.create(getSQLSymbolManager().getPlaceHolder(), extQuery.getSql());
		qv.setResultClass(extQuery.getEntityClass());
		if(extQuery.getParamsValue().isList()){
			qv.setValue(extQuery.getParamsValue().asList());
		}else{
			qv.setValue(extQuery.getParamsValue().asMap());
		}*/
		
		return queryAction;
	}
	
	protected ExtQuery createExtQuery(Class<?> entityClass, String alias, Map<Object, Object> properties){
		return getSQLSymbolManager().createSelectQuery(entityClass, alias, properties);
	}

	public String getAlias() {
		return alias;
	}

	/*protected void throwIfHasBuild(){
		if(extQuery!=null){
			throw new UnsupportedOperationException("query has build!");
		}
	}*/

	/*public ExtQuery getExtQuery() {
		throwIfHasNotBuild();
		return extQuery;
	}

	protected void throwIfHasNotBuild(){
		if(extQuery==null){
			throw new UnsupportedOperationException("query has not build!");
		}
	}*/
	
}
