package org.onetwo.ext.es;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.HasAggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.support.AggregationPath;
import org.onetwo.common.reflect.ReflectUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;



public class SimpleSearchQueryBuilder {
//	private static final Logger logger = LoggerFactory.getLogger(SimpleSearchQueryBuilder.class);

	public static SimpleSearchQueryBuilder newBuilder(){
		return new SimpleSearchQueryBuilder();
	}
	public static SimpleSearchQueryBuilder from(String index, String type){
		return new SimpleSearchQueryBuilder().addIndices(index).addTypes(type);
	}
	
	/***
	 * 
	 * @param pageNo from 0
	 * @param pageSize
	 * @return
	 */
	public static SimpleSearchQueryBuilder newBuilder(int pageNo, int pageSize){
		return new SimpleSearchQueryBuilder(pageNo, pageSize);
	}

	private NativeSearchQueryBuilder searchQueryBuilder;
	private QueryBuilder queryBuilder;
	private SimpleBooleanQueryBuilder<SimpleSearchQueryBuilder> booleanQuery;
//	private boolean built = false;
	private NativeSearchQuery nativeSearchQuery;
	private List<Sort> sorts = Lists.newArrayList();
	

	private String[] includeSources;
	private String[] excludeSources;
	
	private List<String> indices = Lists.newArrayList();
	private List<String> types = Lists.newArrayList();

	private SimpleSearchQueryBuilder() {
		this(new NativeSearchQueryBuilder());
	}

	public SimpleSearchQueryBuilder(int pageNo, int pageSize) {
		this(new NativeSearchQueryBuilder());
		withPageable(pageNo, pageSize);
	}
	
	public SimpleSearchQueryBuilder addIndices(String... indeces){
		Collections.addAll(this.indices, indeces);
		return this;
	}
	
	public SimpleSearchQueryBuilder addTypes(String... types){
		Collections.addAll(this.types, types);
		return this;
	}
	
	final public SimpleSearchQueryBuilder withPageable(int pageNo, int pageSize){
		this.searchQueryBuilder.withPageable(new PageRequest(pageNo, pageSize));
		return this;
	}

	public SimpleSearchQueryBuilder(NativeSearchQueryBuilder searchQueryBuilder) {
		super();
		this.searchQueryBuilder = searchQueryBuilder;
	}

	public SimpleSearchQueryBuilder includeSources(String... includeSources) {
		this.includeSources = includeSources;
		return this;
	}

	public SimpleSearchQueryBuilder excludeSources(String... excludeSources) {
		this.excludeSources = excludeSources;
		return this;
	}

	public SimpleBooleanQueryBuilder<SimpleSearchQueryBuilder> bool(){
		if(booleanQuery==null){
			booleanQuery = new SimpleBooleanQueryBuilder<>(this);
		}
		return booleanQuery;
	}
	
	public SimpleSearchQueryBuilder boolTerm(String field, Object value){
		bool().mustTerm(field, value);
		return this;
	}

	public SimpleSearchQueryBuilder match(String text, String...fields){
		return matchAnalyzer(false, text, null, fields);
	}
	public SimpleSearchQueryBuilder matchAnalyzer(boolean useFilterIfQueryExists, String text, String analyzer, String...fields){
		if(StringUtils.isBlank(text) || fields==null || fields.length==0){
			return this;
		}
		if(queryBuilder!=null && useFilterIfQueryExists){
			bool().multiMustTerm(text, fields);
			return this;
		}
		
		if(fields.length==1){
			queryBuilder = matchQuery(fields[0], text).analyzer(analyzer);
		}else{
			queryBuilder = multiMatchQuery(text, fields).analyzer(analyzer);
		}
		return this;
	}

	
	public SimpleSearchQueryBuilder matchAll(){
		MatchAllQueryBuilder q = matchAllQuery();
		queryBuilder = q;
		return this;
	}
	
	public MatchAllQueryBuilder matchAllBuilder(){
		MatchAllQueryBuilder q = matchAllQuery();
		queryBuilder = q;
		return q;
	}
	
	public <T extends QueryBuilder> T query(T queryBuilder){
		this.queryBuilder = queryBuilder;
		return queryBuilder;
	}
	
	public MatchQueryBuilder matchBuilder(String text, String...fields){
		MatchQueryBuilder q = matchQuery(text, fields);
		queryBuilder = q;
		return q;
	}
	
	public MultiMatchQueryBuilder multiMatchBuilder(String text, String...fields){
		MultiMatchQueryBuilder q = multiMatchQuery(text, fields);
		queryBuilder = q;
		return q;
	}

	public SimpleSearchQueryBuilder orderByAsc(String...fields){
		return order(Direction.ASC, fields);
	}
	public SimpleSearchQueryBuilder orderByDesc(String...fields){
		return order(Direction.DESC, fields);
	}
	public SimpleSearchQueryBuilder order(Direction direct, String...fields){
		if(ArrayUtils.isEmpty(fields))
			return this;
		Sort sort = new Sort(direct, fields);
		this.sorts.add(sort);
		return this;
	}
	
	public SimpleSearchQueryBuilder sort(Sort sort){
		if(sort==null)
			return this;
		this.sorts.add(sort);
		return this;
	}

	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> aggs(String name, String field) {
		return aggs(name, field, null);
	}

	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> aggs(String name, String field, Integer size) {
		SimpleAggregationBuilder<SimpleSearchQueryBuilder> aggsBuilder = new SimpleAggregationBuilder<>(this, name, field, size);
		SimpleSearchQueryBuilder.this.searchQueryBuilder.addAggregation(aggsBuilder.aggsBuilder);
		return aggsBuilder;
	}
	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> aggsNested(String name, String path) {
		NestedBuilder nested = AggregationBuilders.nested(name).path(path);
		SimpleAggregationBuilder<SimpleSearchQueryBuilder> aggsBuilder = new SimpleAggregationBuilder<>(this, nested);
		SimpleSearchQueryBuilder.this.searchQueryBuilder.addAggregation(aggsBuilder.aggsBuilder);
		return aggsBuilder;
	}
	
	public NativeSearchQuery build(boolean matchAllIfQueryNotExists){
		if(nativeSearchQuery!=null){
			return nativeSearchQuery;
		}
		
		if(queryBuilder!=null){
			searchQueryBuilder.withQuery(queryBuilder);
		}else if(matchAllIfQueryNotExists){
			searchQueryBuilder.withQuery(matchAllQuery());
		}
		if(booleanQuery!=null && booleanQuery.hasClauses()){
			booleanQuery.build();
			searchQueryBuilder.withFilter(booleanQuery.boolQuery);
		}
		this.nativeSearchQuery = searchQueryBuilder.build();
		this.nativeSearchQuery.addIndices(this.indices.toArray(new String[0]));
		this.nativeSearchQuery.addTypes(this.types.toArray(new String[0]));
		this.nativeSearchQuery.addSourceFilter(new FetchSourceFilter(includeSources, excludeSources));
		this.sorts.forEach(s->this.nativeSearchQuery.addSort(s));
//		built = true;
		return nativeSearchQuery;
	}
	
	public NativeSearchQuery getNativeSearchQuery() {
		return nativeSearchQuery;
	}

	public <T> Page<T> queryForPage(ElasticsearchTemplate elasticsearchTemplate, Class<T> clazz){
		NativeSearchQuery searchQuery = build(true);
		Page<T> page = elasticsearchTemplate.queryForPage(searchQuery, clazz);
		return page;
	}
	
	public Aggregations queryAggs(ElasticsearchTemplate elasticsearchTemplate){
		return doQuery(elasticsearchTemplate, response->{
			/*Terms agg = response.getAggregations().get("hots");
			List<Bucket> buckets = agg.getBuckets();
			Object key = buckets.get(0).getKey();
			Object doc = buckets.get(0).getDocCount();*/
			return response.getAggregations();
		});
	}
	
	public QueryResult doQueryResult(ElasticsearchTemplate elasticsearchTemplate){
		return doQuery(elasticsearchTemplate, response->{
			return new QueryResult(elasticsearchTemplate, this, response);
		});
	}
	
	public <R> R doQuery(Function<NativeSearchQuery, R> func){
		NativeSearchQuery searchQuery = build(true);
		return func.apply(searchQuery);
	}
	
	public <R> R doQuery(ElasticsearchTemplate elasticsearchTemplate, ResultsExtractor<R> resultsExtractor){
		NativeSearchQuery searchQuery = build(true);
		return elasticsearchTemplate.query(searchQuery, resultsExtractor);
	}

	static public class QueryResult {
		final private SimpleSearchQueryBuilder queryBuilder;
		final private SearchResponse searchResponse;
		private AggregationsResult aggregationsResult;
		private final SearchResultMapper mapper;
		public QueryResult(ElasticsearchTemplate elasticsearchTemplate, SimpleSearchQueryBuilder queryBuilder, SearchResponse response) {
			super();
			this.queryBuilder = queryBuilder;
			this.searchResponse = response;
			this.aggregationsResult = new AggregationsResult(this.searchResponse.getAggregations());
			this.mapper = new DefaultResultMapper(elasticsearchTemplate.getElasticsearchConverter().getMappingContext());
		}
		
		public <T> Page<T> getPage(Class<T> clazz){
			return mapper.mapResults(searchResponse, clazz, queryBuilder.getNativeSearchQuery().getPageable());
		}
		
		public AggregationsResult getAggregationsResult(){
			return aggregationsResult;
		}

	}

    @SuppressWarnings("unchecked")
    static public class AggregationsResult {

	    static public List<? extends Bucket> getBuckets(Aggregation agg) {
	    	if(agg instanceof MultiBucketsAggregation){
	    		return ((MultiBucketsAggregation)agg).getBuckets();
	    	}else{
	    		return ImmutableList.of();
	    	}
	    }
	    
    	private Aggregations aggregations;

		public AggregationsResult(Aggregations aggregations) {
			super();
			this.aggregations = aggregations;
		}

	    public <T extends Aggregation> T getAggregation(String name) {
	    	this.checkAggs();
	        return aggregations.get(name);
	    }

	    public Terms getTerms(String name) {
	        return (Terms)getAggregation(name);
	    }

		public <T extends Aggregation> T getAggregationByPath(String path) {
	    	this.checkAggs();
	    	List<String> paths = AggregationPath.parse(path).getPathElementsAsStringList();
	    	T agg = aggregations.get(paths.get(0));
	    	for (int i = 1; i < paths.size(); i++) {
	    		String attr = paths.get(i);
	    		if(agg instanceof HasAggregations){
	    			HasAggregations hasagg = (HasAggregations) agg;
	    			agg = hasagg.getAggregations().get(attr);
	    		}else if(agg instanceof MultiBucketsAggregation){
	    			MultiBucketsAggregation magg = (MultiBucketsAggregation) agg;
	    			if(magg.getBuckets().isEmpty()){
	    				return agg;
	    			}
	    			Bucket bucket = magg.getBuckets().get(0);
	    			agg = bucket.getAggregations().get(attr);
	    		}else{
	    			break;
	    		}
			}
	    	return agg;
	    }

	    public List<? extends Bucket> getBucketsByPath(String path) {
	    	this.checkAggs();
	    	Aggregation val = getAggregationByPath(path);
	    	return getBuckets(val);
	    }
	    
	    public <T> BucketMappingObjectBuilder<T, AggregationsResult> createBucketsMapping(String key, Class<T> targetClass, String keyField) {
	    	this.checkAggs();
//	    	Aggregation agg = getAggregationByPath(key);
	    	return new BucketMappingObjectBuilder<>(this, this, key, targetClass, keyField);
	    }
	    
	    /****
	     * 
	     * @param path
	     * @return 返回的数组，里面的值可能是数组，深度对应路径深度
	     */
	    public Object[] getRawKeysByPath(String path) {
	    	this.checkAggs();
	    	return (Object[])aggregations.getProperty(path+"._key");
	    }
	    public Object[] getKeysByPath(String path) {
	    	this.checkAggs();
		    Object[] keys = getRawKeysByPath(path);
		    if(keys==null || keys.length==0){
			    return null;
		    }else{
		        int size = AggregationPath.parse(path).getPathElementsAsStringList().size();
		        for (int i = 0; i < size; i++) {
		    	    if(i!=size-1){
		    		    keys = (Object[])keys[0];
		    	    }
		        }
		        return keys;
		    }
	    }

	    /****
	     * 
	     * @param path
	     * @return 
	     */
	    public <T> T getKeyByPath(String path) {
	    	this.checkAggs();
		    Object[] keys = getKeysByPath(path);
		    if(keys==null || keys.length==0){
			    return null;
		    }else{
		        return (T)keys[0];
		    }
	    }

	    public Nested getNestedAggregation(String name) {
	    	this.checkAggs();
	    	Nested nested = aggregations.get(name);
	    	return nested;
	    }

	    private void checkAggs() {
	    	if(aggregations==null){
	    		throw new RuntimeException("aggs not found!");
	    	}
	    }
    }
    
    public static class BucketMappingObjectBuilder<T, P> {
    	private BucketMapping<T> mapping;
//    	private List<? extends Bucket> buckets;
    	private AggregationsResult aggResult;
    	private P parent;
    	private String key;
//    	private final Aggregation aggregation;
    	
    	public BucketMappingObjectBuilder(P parent, AggregationsResult aggResult, String key, Class<T> targetClass, String keyField) {
			super();
			this.parent = parent;
			this.key = key;
			this.mapping = new BucketMapping<T>(targetClass, keyField);
			this.aggResult = aggResult;
//			this.aggregation = aggregation;
//			this.buckets = ar.getBucketsByPath(key);
		}

    	public P end(){
    		return this.parent;
    	}
    	/***
    	 * @param path
    	 * @param property
    	 * @return
    	 */
		public BucketMappingObjectBuilder<T, P> mapKey(String path, String property){
    		this.mapping.map(path, property);
    		return this;
    	}
		
		public <SUB> BucketMappingObjectBuilder<SUB, BucketMappingObjectBuilder<T, P>> mapBuckets(String key, String property, Class<SUB> targetClass, String keyField){
			BucketMappingObjectBuilder<SUB, BucketMappingObjectBuilder<T, P>> builder = new BucketMappingObjectBuilder<>(this, null, key, targetClass, keyField);
			this.mapping.map(property, builder);
    		return builder;
    	}

		public List<T> buildTargetList(){
			return this.buildTargetList(aggResult);
		}

		public List<T> buildTargetList(AggregationsResult aggResult){
			Assert.notNull(aggResult);
			List<? extends Bucket> buckets = aggResult.getBucketsByPath(key);
			if(buckets==null)
				return Collections.emptyList();
			return buckets.stream().map(b->mapping.buildTarget(b)).collect(Collectors.toList());
		}
    }
    
    public static class BucketMapping<T> {
//    	private String key;
    	private Class<T> targetClass;
    	private String keyProperty;
    	private Map<String, Object> fieldMappings = Maps.newHashMap();
    	
    	public BucketMapping(Class<T> targetClass, String keyField) {
			super();
//			this.key = key;
			this.targetClass = targetClass;
			this.keyProperty = keyField;
		}

		public BucketMapping<T> map(String path, Object property){
    		this.fieldMappings.put(path, property);
    		return this;
    	}

		public T buildTarget(Bucket bucket){
			T obj = ReflectUtils.newInstance(targetClass);
			BeanWrapper bw = newBeanWrapper(obj);
			bw.setPropertyValue(keyProperty, bucket.getKey());
			AggregationsResult ar = new AggregationsResult(bucket.getAggregations());
			this.fieldMappings.forEach((path, prop)->{
				if(prop instanceof BucketMappingObjectBuilder){
					BucketMappingObjectBuilder<?, ?> b = (BucketMappingObjectBuilder<?, ?>)prop;
					List<?> values = b.buildTargetList(ar);
					bw.setPropertyValue(path, values);
				}else{
					/*Object[] value = ar.getKeysByPath(path);
					SimpleSearchQueryBuilder.logger.info("values:"+ArrayUtils.toString(value));*/
					Object val = ar.getKeyByPath(path);
					bw.setPropertyValue(prop.toString(), val);
				}
			});
			return obj;
		}

		public BeanWrapper newBeanWrapper(Object obj){
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(obj);
			bw.setAutoGrowNestedPaths(true);
			return bw;
		}
    }
	
	abstract public class ExtBaseQueryBuilder<PB> {
		protected PB parentBuilder;
		
		public ExtBaseQueryBuilder(PB parentBuilder) {
			super();
			this.parentBuilder = parentBuilder;
		}

		public PB end(){
			return parentBuilder;
		}
	}
	
	public class SimpleNestedQueryBuilder<PB> extends ExtBaseQueryBuilder<PB> {
		final private SimpleBooleanQueryBuilder<SimpleNestedQueryBuilder<PB>> boolQuery = new SimpleBooleanQueryBuilder<>(this);
		final private String path;
		public SimpleNestedQueryBuilder(PB parentBuilder, String path) {
			super(parentBuilder);
			this.path = path;
		}
		public SimpleBooleanQueryBuilder<SimpleNestedQueryBuilder<PB>> bool() {
			return boolQuery;
		}
		public String getPath() {
			return path;
		}

	}
	
	public class SimpleBooleanQueryBuilder<PB> extends ExtBaseQueryBuilder<PB> {
		private BoolQueryBuilder boolQuery = boolQuery();
		private List<SimpleNestedQueryBuilder<SimpleBooleanQueryBuilder<PB>>> nestedQuerys = Lists.newArrayList();
		
		public SimpleBooleanQueryBuilder(PB parentBuilder) {
			super(parentBuilder);
		}
		
		public boolean hasClauses() {
			if(boolQuery.hasClauses()){
				return true;
			}
			return this.nestedQuerys.size()>0;
		}

		private void build(){
			this.nestedQuerys.stream()
								.filter(nested->nested.boolQuery.hasClauses())
								.forEach(nested->{
									must(QueryBuilders.nestedQuery(nested.getPath(), nested.bool().boolQuery));
								});
		}

		public SimpleBooleanQueryBuilder<PB> mustTerm(String field, Object value){
			Assert.hasText(field);
			if(!org.onetwo.common.utils.StringUtils.isNullOrBlankString(value))
				must(termQuery(field, value));
			return this;
		}

		public SimpleNestedQueryBuilder<SimpleBooleanQueryBuilder<PB>> nested(String path){
			SimpleNestedQueryBuilder<SimpleBooleanQueryBuilder<PB>> nestedBuilder = new SimpleNestedQueryBuilder<>(this, path);
			nestedQuerys.add(nestedBuilder);
			return nestedBuilder;
		}

		/*public SimpleBooleanQueryBuilder<PB> mustNestedTerm(String path, String field, Object value){
			if(!CommonUtils.isNullOrBlankString(value)){
				TermQueryBuilder termQuery = QueryBuilders.termQuery(field, value);
				must(QueryBuilders.nestedQuery(path, termQuery));
			}
			return this;
		}
		
		public SimpleBooleanQueryBuilder<PB> mustNestedTerms(String path, String field, Object... values){
			Assert.hasText(field);
			if(values!=null && values.length>0){
				List<Object> listValue = Lists.newArrayList(values);
				listValue.removeIf(Objects::isNull);
				if(!listValue.isEmpty()){
					TermsQueryBuilder termQuery = QueryBuilders.termsQuery(field, listValue.toArray(new Object[0]));
					must(QueryBuilders.nestedQuery(path, termQuery));
				}
			}
			return this;
		}*/
		
		public SimpleBooleanQueryBuilder<PB> mustTerms(String field, Object... values){
			Assert.hasText(field);
			if(values!=null && values.length>0){
				List<Object> listValue = Lists.newArrayList(values);
				listValue.removeIf(Objects::isNull);
				if(!listValue.isEmpty()){
					must(QueryBuilders.termsQuery(field, listValue.toArray(new Object[0])));
				}
			}
			return this;
		}
		
		public SimpleBooleanQueryBuilder<PB> mustTerms(String field, Collection<?> values){
			Assert.hasText(field);
			if(values!=null && !values.isEmpty())
				must(QueryBuilders.termsQuery(field, values));
			return this;
		}
		
		public SimpleBooleanQueryBuilder<PB> mustTermOrMissing(String field, Object value){
			Assert.hasText(field);
			if(value==null){
				return missing(field);
			}else{
				return mustTerm(field, value);
			}
		}

		/****
		 * is null
		 * @param field
		 * @return
		 */
		public SimpleBooleanQueryBuilder<PB> missing(String field){
			return mustNotExists(field);
		}
		public SimpleBooleanQueryBuilder<PB> mustNotExists(String field){
			Assert.hasText(field);
			boolQuery.mustNot(QueryBuilders.existsQuery(field));
			return this;
		}
		/***
		 * is not null
		 * @param field
		 * @return
		 */
		public SimpleBooleanQueryBuilder<PB> mustExists(String field){
			Assert.hasText(field);
			boolQuery.must(QueryBuilders.existsQuery(field));
			return this;
		}
		
		public SimpleBooleanQueryBuilder<PB> shouldTerms(String field, Collection<?> values){
			Assert.hasText(field);
			if(values!=null && !values.isEmpty())
				boolQuery.should(QueryBuilders.termsQuery(field, values));
			return this;
		}
		
		public SimpleBooleanQueryBuilder<PB> multiMustTerm(Object value, String... fields){
			if(ArrayUtils.isEmpty(fields))
				return this;
//			Stream.of(fields).forEach(f->mustTerm(f, value));
			boolQuery.must(QueryBuilders.multiMatchQuery(value, fields));
			return this;
		}
		
		public RangeQueryBuilder rangeBuilder(String name){
			RangeQueryBuilder range = new RangeQueryBuilder(name);
			boolQuery.must(range);
			return range;
		}
		
		public <T extends QueryBuilder> T must(T queryBuilder){
			Assert.notNull(queryBuilder);
			boolQuery.must(queryBuilder);
			return queryBuilder;
		}
		
		public <T extends QueryBuilder> T should(T queryBuilder){
			Assert.notNull(queryBuilder);
			boolQuery.should(queryBuilder);
			return queryBuilder;
		}

	}

	public class SimpleAggregationBuilder<PB> extends ExtBaseQueryBuilder<PB> {
		protected AggregationBuilder<?> aggsBuilder;
		
		public SimpleAggregationBuilder(PB parentBuilder, AggregationBuilder<?> aggTerms) {
			super(parentBuilder);
			this.aggsBuilder = aggTerms;
		}
		
		public SimpleSearchQueryBuilder endAllAggs(){
			return SimpleSearchQueryBuilder.this;
		}

		/***
		 * 
		"aggs": {
	        "hots": {
	            "terms": {
	                "field": "keyword"
	            }
	        }
		 * @param name
		 * @param field
		 * @return
		 */
		public SimpleAggregationBuilder(PB parentBuilder, String name, String field, Integer size) {
			super(parentBuilder);
			TermsBuilder terms = AggregationBuilders.terms(name);
			if(size!=null){
				terms.field(field).size(size);
			}else{
				terms.field(field);
			}
			terms.order(Terms.Order.count(false));//COUNT_DESC
			this.aggsBuilder = terms;
		}

		/*public SimpleAggregationBuilder<PB> aggs(String name, String field) {
			return aggs(name, field, null);
		}

		public SimpleAggregationBuilder<PB> aggs(String name, String field, Integer size) {
			SimpleSearchQueryBuilder.this.aggs(name, field, size);
			return this;
		}

		public SimpleAggregationBuilder<PB> aggsNested(String name, String path) {
			SimpleSearchQueryBuilder.this.aggsNested(name, path);
			return this;
		}*/

		public SimpleAggregationBuilder<PB> subAggsNested(String name, String path) {
			NestedBuilder nested = AggregationBuilders.nested(name).path(path);
			SimpleAggregationBuilder<PB> subAggs = new SimpleAggregationBuilder<>(this.parentBuilder, nested);
			this.aggsBuilder.subAggregation(subAggs.aggsBuilder);
			return this;
		}

		public SimpleAggregationBuilder<SimpleAggregationBuilder<PB>> subAggs(String name, String field) {
			return subAggs(name, field, null);
		}

		public SimpleAggregationBuilder<SimpleAggregationBuilder<PB>> subAggs(String name, String field, Integer size) {
			SimpleAggregationBuilder<SimpleAggregationBuilder<PB>> subAggs = new SimpleAggregationBuilder<>(this, name, field, size);
			this.aggsBuilder.subAggregation(subAggs.aggsBuilder);
			return subAggs;
		}
		
	}

}
