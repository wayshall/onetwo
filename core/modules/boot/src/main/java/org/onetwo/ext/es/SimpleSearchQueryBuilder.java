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
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.HasAggregations;
import org.elasticsearch.search.aggregations.ValuesSourceAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedBuilder;
import org.elasticsearch.search.aggregations.bucket.range.RangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.ValuesSourceMetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.AggregationPath;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder.SuggestionBuilder;
import org.onetwo.common.reflect.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(SimpleSearchQueryBuilder.class);

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
	
	private boolean matchAllIfQueryNotExists = true;
	
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
	
	public void setMatchAllIfQueryNotExists(boolean matchAllIfQueryNotExists) {
		this.matchAllIfQueryNotExists = matchAllIfQueryNotExists;
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

	public BoolQueryBuilder retriveBoolQuery(){
		return bool().boolQuery;
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
	public GeoDistanceSortExtBuilder geoDistanceSort(String fieldName){
		GeoDistanceSortExtBuilder geosort = new GeoDistanceSortExtBuilder(fieldName);
		withSortBuilder(geosort);
		return geosort;
	}
	
	public SimpleSearchQueryBuilder order(Direction direct, String...fields){
		if(ArrayUtils.isEmpty(fields))
			return this;
		Sort sort = new Sort(direct, fields);
		this.sorts.add(sort);
		return this;
	}

	public SimpleSearchQueryBuilder when(boolean condition, Consumer<SimpleSearchQueryBuilder> consumer){
		if(condition){
			consumer.accept(this);
		}
		return this;
	}
	
	public SimpleSearchQueryBuilder sort(Sort sort){
		if(sort==null)
			return this;
		this.sorts.add(sort);
		return this;
	}
	
	public SimpleSearchQueryBuilder withSortBuilder(SortBuilder sortBuilder){
		if(sortBuilder==null)
			return this;
		this.searchQueryBuilder.withSort(sortBuilder);
		return this;
	}
	
	public SimpleSearchQueryBuilder withScriptSort(String script, SortOrder order){
		if(StringUtils.isBlank(script))
			return this;
		Script sortScript = new Script(script);
		SortBuilder sortBuilder = SortBuilders.scriptSort(sortScript, "number");
		sortBuilder.order(order);
		this.searchQueryBuilder.withSort(sortBuilder);
		return this;
	}

	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> agg(String name, String field) {
		return agg(name, field, null);
	}

	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> max(String name) {
		return agg(AggregationBuilders.max(name));
	}

	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> min(String name) {
		return agg(AggregationBuilders.min(name));
	}

	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> aggRanges(String name, String field, List<RangeQueryer> ranges) {
		RangeBuilder rangeBuilder = AggregationBuilders.range(name);
		ranges.stream().forEach(r->{
			if(!r.isValid()){
				return ;
			}
			if(r.getFrom()==null){
				rangeBuilder.addUnboundedTo(r.getKey(), r.getTo());
			}else if(r.getTo()==null){
				rangeBuilder.addUnboundedFrom(r.getKey(), r.getFrom());
			}else{
				rangeBuilder.addRange(r.getKey(), r.getFrom(), r.getTo());
			}
		});
		return agg(rangeBuilder).field(field);
	}
	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> extendedStats(String name) {
		return agg(AggregationBuilders.extendedStats(name));
	}

	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> aggFilters(String name) {
		FiltersAggregationBuilder filtersAgg = AggregationBuilders.filters(name);
		return agg(filtersAgg);
	}

	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> aggFilter(String name) {
		FilterAggregationBuilder filterAgg = AggregationBuilders.filter(name);
		return agg(filterAgg);
	}
	
	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> agg(String name, String field, Integer size) {
		SimpleAggregationBuilder<SimpleSearchQueryBuilder> aggsBuilder = new SimpleAggregationBuilder<>(this, name, field, size);
		this.searchQueryBuilder.addAggregation(aggsBuilder.aggsBuilder);
		return aggsBuilder;
	}

	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> agg(AbstractAggregationBuilder aggsBuilder) {
		SimpleAggregationBuilder<SimpleSearchQueryBuilder> simpleBuilder = new SimpleAggregationBuilder<>(this, aggsBuilder);
		this.searchQueryBuilder.addAggregation(simpleBuilder.aggsBuilder);
		return simpleBuilder;
	}
	public SimpleAggregationBuilder<SimpleSearchQueryBuilder> aggNested(String name, String path) {
		NestedBuilder nested = AggregationBuilders.nested(name).path(path);
		SimpleAggregationBuilder<SimpleSearchQueryBuilder> aggsBuilder = new SimpleAggregationBuilder<>(this, nested);
		this.searchQueryBuilder.addAggregation(aggsBuilder.aggsBuilder);
		return aggsBuilder;
	}
	
	@SuppressWarnings("unchecked")
	public SimpleSearchQueryBuilder forceClearAggregations(){
		List<AbstractAggregationBuilder> aggregationBuilders = (List<AbstractAggregationBuilder>)ReflectUtils.getFieldValue(searchQueryBuilder, "aggregationBuilders");
		if(aggregationBuilders!=null){
			aggregationBuilders.clear();
		}
		return this;
	}
	
	public NativeSearchQuery build(boolean rebuild){
		if(!rebuild && nativeSearchQuery!=null){
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

	public <T> List<T> queryForList(ElasticsearchTemplate elasticsearchTemplate, Class<T> clazz){
		NativeSearchQuery searchQuery = build(true);
		List<T> page = elasticsearchTemplate.queryForList(searchQuery, clazz);
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
	
	public SuggestResponse querySuggest(ElasticsearchTemplate elasticsearchTemplate, SuggestionBuilder<?> suggestion){
		SuggestResponse reponse = elasticsearchTemplate.suggest(suggestion, this.indices.toArray(new String[0]));
		return reponse;
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
	    	List<String> paths = AggregationPath.parse(name).getPathElementsAsStringList();
	    	if(paths.size()==1){
	    		return aggregations.get(name);
	    	}else{
	    		return getAggregationByPath(paths);
	    	}
	    }

	    public Terms getTerms(String name) {
	        return (Terms)getAggregation(name);
	    }

	    public <T> T mapAggregation(String name, Class<T> clazz) {
	    	return mapAggregation(name, agg->{
	    		T target = ReflectUtils.newInstance(clazz);
	    		ReflectUtils.copy(target, agg);
	    		return target;
	    	});
	    }
	    public <T> List<T> mapBuckets(String name, Class<T> clazz) {
	    	Aggregation agg = getAggregation(name);
	    	List<? extends Bucket> buckets = getBuckets(agg);
	    	List<T> datas = buckets.stream().map(bucket->{
	    		T target = ReflectUtils.newInstance(clazz);
	    		ReflectUtils.copy(target, (Bucket)bucket);
	    		return target;
	    	})
	    	.collect(Collectors.toList());
	    	return datas;
	    }
	    public <T, A extends Aggregation> T mapAggregation(String name, Function<A, T> mapper) {
	    	A aggr = getAggregation(name);
	    	if(aggr==null){
	    		return null;
	    	}
	    	return mapper.apply(aggr);
	    }
	   /* public <T> T mapExtendedStats(String name, Function<InternalExtendedStats, T> mapper) {
	    	return mapAggregation(name, InternalExtendedStats.class, mapper);
	    }*/

	    public <T extends Aggregation> T getAggregationByPath(String path) {
	    	List<String> paths = AggregationPath.parse(path).getPathElementsAsStringList();
	    	return getAggregationByPath(paths);
	    }
		public <T extends Aggregation> T getAggregationByPath(List<String> paths) {
	    	this.checkAggs();
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
	    public Optional<Object[]> getRawKeysByPath(String path) {
	    	this.checkAggs();
	    	try {
		    	return Optional.ofNullable((Object[])aggregations.getProperty(path+"._key"));
			} catch (IllegalArgumentException e) {
				logger.error("getRawKeysByPath error. path: {}, msg: {}", path, e.getMessage());
				return Optional.empty();
			}
	    }
	    /****
	     * path1>path2
	     * @param path
	     * @return 返回的数组，里面的值可能是数组，深度对应路径深度
	     */
	    public Optional<Object[]> getKeysByPath(String path) {
	    	this.checkAggs();
	    	Optional<Object[]> keysOpt = getRawKeysByPath(path);
		    if(!keysOpt.isPresent() || keysOpt.get().length==0){
			    return Optional.empty();
		    }

	    	Object[] keys = keysOpt.get();
	    	//return if not nested array
	    	if(!keys[0].getClass().isArray()){
	    		return keysOpt;
	    	}
	        int size = AggregationPath.parse(path).getPathElementsAsStringList().size();
	        for (int i = 0; i < size; i++) {
	    	    if(i!=size-1){
	    		    keys = (Object[])keys[0];
	    	    }
	        }
	        return Optional.of(keys);
	    }

	    /****
	     * 
	     * @param path
	     * @return 
	     */
	    public <T> Optional<T> getKeyByPath(String path) {
	    	this.checkAggs();
		    /*Object[] keys = getKeysByPath(path);
		    if(keys==null || keys.length==0){
			    return null;
		    }else{
		        return (T)keys[0];
		    }*/
//		    return getKeysByPath(path).map(keys->(T)keys[0]).orElse(null);
	    	return getKeysByPath(path).map(keys->(T)keys[0]);
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
    
    /****
     * 
  "aggregations": {
    "salePriceRange": {
      "buckets": [
        {
          "key": "*-39.0",
          "to": 39,
          "to_as_string": "39.0",
          "doc_count": 10
        },
        {
          "key": "40.0-99.0",
          "from": 40,
          "from_as_string": "40.0",
          "to": 99,
          "to_as_string": "99.0",
          "doc_count": 0
        }
      ]
    }
  }
     * @author way
     *
     * @param <T>
     * @param <P>
     */
    public static class BucketMappingObjectBuilder<T, P> {
    	private BucketMapping<T> mapping;
//    	private List<? extends Bucket> buckets;
    	private AggregationsResult aggResult;
    	private P parent;
    	private String key;
//    	private final Aggregation aggregation;
    	
    	public BucketMappingObjectBuilder(P parent, AggregationsResult aggResult, String aggName, Class<T> targetClass, String keyField) {
			super();
			this.parent = parent;
			this.key = aggName;
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
	class GeoDistanceSortExtBuilder extends GeoDistanceSortBuilder {
		GeoDistanceSortExtBuilder(String fieldName){
			super(fieldName);
		}
		public SimpleSearchQueryBuilder end(){
			return SimpleSearchQueryBuilder.this;
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

		private Supplier<Boolean> conditionSupplier;
		
		private List<SimpleBooleanQueryBuilder<SimpleBooleanQueryBuilder<PB>>> orBoolQuerys = Lists.newArrayList();
		private List<SimpleBooleanQueryBuilder<SimpleBooleanQueryBuilder<PB>>> andBoolQuerys = Lists.newArrayList();
		private List<SimpleBooleanQueryBuilder<SimpleBooleanQueryBuilder<PB>>> notBoolQuerys = Lists.newArrayList();
		
		public SimpleBooleanQueryBuilder(PB parentBuilder) {
			super(parentBuilder);
		}

		public SimpleBooleanQueryBuilder<PB> withCondition(Supplier<Boolean> condition) {
			this.conditionSupplier = condition;
			return this;
		}

		public PB end(){
			andBoolQuerys.forEach(b->{
				boolQuery.must(b.boolQuery);
			});
			orBoolQuerys.forEach(b->{
				boolQuery.should(b.boolQuery);
			});
			notBoolQuerys.forEach(b->{
				boolQuery.mustNot(b.boolQuery);
			});
			return parentBuilder;
		}

		public SimpleBooleanQueryBuilder<PB> endCondition() {
			this.conditionSupplier = null;
			return this;
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
									nested.bool().build();
									must(QueryBuilders.nestedQuery(nested.getPath(), nested.bool().boolQuery));
								});
		}

		public SimpleBooleanQueryBuilder<PB> mustTerm(String field, Object value){
			Assert.hasText(field);
			if(!org.onetwo.common.utils.StringUtils.isNullOrBlankString(value))
				must(termQuery(field, value));
			return this;
		}

		public SimpleBooleanQueryBuilder<PB> with(Consumer<SimpleBooleanQueryBuilder<PB>> consumer){
			Assert.notNull(consumer);
			consumer.accept(this);
			return this;
		}
		
		public SimpleBooleanQueryBuilder<SimpleBooleanQueryBuilder<PB>> or(){
			SimpleBooleanQueryBuilder<SimpleBooleanQueryBuilder<PB>> simpleBooleanQuery = new SimpleBooleanQueryBuilder<>(this);
			orBoolQuerys.add(simpleBooleanQuery);
			return simpleBooleanQuery;
		}

		public SimpleBooleanQueryBuilder<SimpleBooleanQueryBuilder<PB>> and(){
			SimpleBooleanQueryBuilder<SimpleBooleanQueryBuilder<PB>> simpleBooleanQuery = new SimpleBooleanQueryBuilder<>(this);
			andBoolQuerys.add(simpleBooleanQuery);
			return simpleBooleanQuery;
		}

		public SimpleBooleanQueryBuilder<SimpleBooleanQueryBuilder<PB>> not(){
			SimpleBooleanQueryBuilder<SimpleBooleanQueryBuilder<PB>> simpleBooleanQuery = new SimpleBooleanQueryBuilder<>(this);
			notBoolQuerys.add(simpleBooleanQuery);
			return simpleBooleanQuery;
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

		public SimpleBooleanQueryBuilder<PB> doTerms(String field, Consumer<TermsQueryBuilder> consumer, Object... values){
			Assert.hasText(field);
			if(values==null || values.length==0){
				return this;
			}
			List<Object> listValue = Lists.newArrayList(values);
			listValue.removeIf(Objects::isNull);
			if(listValue.isEmpty()){
				return this;
			}
			TermsQueryBuilder termQueryBuilder = null;
			if(listValue.get(0) instanceof Collection){
				Collection<?> colValue = (Collection<?>) listValue.get(0);
				if(!colValue.isEmpty()){
					termQueryBuilder = QueryBuilders.termsQuery(field, colValue);
				}
			}else{
				termQueryBuilder = QueryBuilders.termsQuery(field, listValue.toArray(new Object[0]));
			}
//					mustNot(termQueryBuilder);
			consumer.accept(termQueryBuilder);
			return this;
		}
		

		public SimpleBooleanQueryBuilder<PB> mustNotTerms(String field, Object... values){
			return doTerms(field, q->mustNot(q), values);
		}
		
		public SimpleBooleanQueryBuilder<PB> mustTerms(String field, Object... values){
			/*Assert.hasText(field);
			if(values!=null && values.length>0){
				List<Object> listValue = Lists.newArrayList(values);
				listValue.removeIf(Objects::isNull);
				if(!listValue.isEmpty()){
					must(QueryBuilders.termsQuery(field, listValue.toArray(new Object[0])));
				}
			}*/
			return doTerms(field, q->must(q), values);
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
				return mustMissing(field);
			}else{
				return mustTerm(field, value);
			}
		}

		/***
		 * or is not null
		 * @param field
		 * @return
		 */
		public SimpleBooleanQueryBuilder<PB> shouldExists(String field){
			Assert.hasText(field);
			should(QueryBuilders.existsQuery(field));
			return this;
		}
		/****
		 * and is null
		 * @param field
		 * @return
		 */
		public SimpleBooleanQueryBuilder<PB> mustMissing(String field){
			return mustNotExists(field);
		}
		public SimpleBooleanQueryBuilder<PB> mustIsNull(String field){
			return mustNotExists(field);
		}
		public SimpleBooleanQueryBuilder<PB> mustIsNotNull(String field){
			return mustExists(field);
		}
		
		/***
		 * and is null
		 * @param field
		 * @return
		 */
		public SimpleBooleanQueryBuilder<PB> mustNotExists(String field){
			Assert.hasText(field);
//			boolQuery.mustNot(QueryBuilders.existsQuery(field));
			mustNot(QueryBuilders.existsQuery(field));
			return this;
		}
		/***
		 * and is not null
		 * @param field
		 * @return
		 */
		public SimpleBooleanQueryBuilder<PB> mustExists(String field){
			Assert.hasText(field);
			must(QueryBuilders.existsQuery(field));
			return this;
		}
		
/*		public SimpleBooleanQueryBuilder<PB> shouldTerms(String field, Collection<?> values){
			Assert.hasText(field);
			if(values!=null && !values.isEmpty()){
				should(QueryBuilders.termsQuery(field, values));
//				boolQuery.should(QueryBuilders.termsQuery(field, values));
			}
			return this;
		}
*/		
		public SimpleBooleanQueryBuilder<PB> shouldTerms(String field, Object... values){
			Assert.hasText(field);
			if(values!=null && values.length>0){
				if(values.length==1 && values[0] instanceof Collection){
					Collection<?> col = (Collection<?>)values[0];
					if(!col.isEmpty()){
						should(QueryBuilders.termsQuery(field, col));
					}
				}else{
					should(QueryBuilders.termsQuery(field, values));
				}
			}
			return this;
		}
		
		public SimpleBooleanQueryBuilder<PB> multiMustTerm(Object value, String... fields){
			if(ArrayUtils.isEmpty(fields))
				return this;
//			Stream.of(fields).forEach(f->mustTerm(f, value));
//			boolQuery.must(QueryBuilders.multiMatchQuery(value, fields));
			must(QueryBuilders.multiMatchQuery(value, fields));
			return this;
		}
		
		public SimpleBooleanQueryBuilder<PB> minShouldMatch(int minimumNumberShouldMatch){
			boolQuery.minimumNumberShouldMatch(minimumNumberShouldMatch);
			return this;
		}
		
		public SimpleBooleanQueryBuilder<PB> range(String name, Object from, Object to){
			if(from==null && to==null){
				return this;
			}
			return rangeBuilder(name, range->range.from(from).to(to));
		}
		public SimpleBooleanQueryBuilder<PB> rangeBuilder(String name, Function<RangeQueryBuilder, RangeQueryBuilder> func){
			RangeQueryBuilder range = new RangeQueryBuilder(name);
			range = func.apply(range);
			if(range!=null){
				must(range);
			}
			return this;
		}
		
		public <T extends QueryBuilder> T must(T queryBuilder){
			Assert.notNull(queryBuilder);
//			boolQuery.must(queryBuilder);
			addToBoolQuery(()->boolQuery.must(queryBuilder));
			return queryBuilder;
		}
		
		public <T extends QueryBuilder> T mustNot(T queryBuilder){
			Assert.notNull(queryBuilder);
//			boolQuery.mustNot(queryBuilder);
			addToBoolQuery(()->boolQuery.mustNot(queryBuilder));
			return queryBuilder;
		}
		
		public <T extends QueryBuilder> T should(T queryBuilder){
			Assert.notNull(queryBuilder);
//			boolQuery.should(queryBuilder);
			addToBoolQuery(()->boolQuery.should(queryBuilder));
			return queryBuilder;
		}
		
		private <T> void addToBoolQuery(Runnable runnable){
			if(conditionSupplier==null || conditionSupplier.get().booleanValue()){
				runnable.run();
			}
		}

	}

	public class SimpleAggregationBuilder<PB> extends ExtBaseQueryBuilder<PB> {
		protected AbstractAggregationBuilder aggsBuilder;

		public SimpleAggregationBuilder(PB parentBuilder, AbstractAggregationBuilder aggTerms) {
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
			this(parentBuilder, AggregationBuilders.terms(name), field, size);
		}
		public SimpleAggregationBuilder(PB parentBuilder, TermsBuilder terms, String field, Integer size) {
			super(parentBuilder);
			if(size!=null){
				terms.size(size);
			}
			if(StringUtils.isNotBlank(field)){
				terms.field(field);
			}
			terms.order(Terms.Order.count(false));//COUNT_DESC
			this.aggsBuilder = terms;
		}
		
		public SimpleAggregationBuilder<PB> field(String field){
			if(this.aggsBuilder instanceof ValuesSourceMetricsAggregationBuilder){
				((ValuesSourceMetricsAggregationBuilder<?>)this.aggsBuilder).field(field);
			}else if(this.aggsBuilder instanceof ValuesSourceAggregationBuilder){
				((ValuesSourceAggregationBuilder<?>)this.aggsBuilder).field(field);
			}else if(this.aggsBuilder instanceof TermsBuilder){
				asTermsBuilder().field(field);
			}else{
				ReflectUtils.setFieldValue(this.aggsBuilder, "field", field);
			}
			return this;
		}
		
		public SimpleAggregationBuilder<PB> size(int size){
			asTermsBuilder().size(size);
			return this;
		}
		private TermsBuilder asTermsBuilder(){
			return (TermsBuilder) this.aggsBuilder;
		}
		private AggregationBuilder<?> asAggregationBuilder(){
			return (AggregationBuilder<?>) this.aggsBuilder;
		}
		private FiltersAggregationBuilder asFiltersAggregationBuilder(){
			return (FiltersAggregationBuilder)this.aggsBuilder;
		}
		private FilterAggregationBuilder asFilterAggregationBuilder(){
			return (FilterAggregationBuilder)this.aggsBuilder;
		}
		
		public SimpleAggregationBuilder<PB> filter(QueryBuilder filter){
			if(filter==null){
				return this;
			}
			if(aggsBuilder instanceof FiltersAggregationBuilder){
				asFiltersAggregationBuilder().filter(filter);
			}else if(aggsBuilder instanceof FilterAggregationBuilder){
				asFilterAggregationBuilder().filter(filter);
			}else{
				throw new UnsupportedOperationException();
			}
			return this;
		}
		
		public SimpleAggregationBuilder<PB> minDocCount(Long minDocCount){
			if(minDocCount!=null){
				this.asTermsBuilder().minDocCount(minDocCount);
			}
			return this;
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
			this.asAggregationBuilder().subAggregation(subAggs.aggsBuilder);
			return this;
		}

		public SimpleAggregationBuilder<SimpleAggregationBuilder<PB>> subAgg(String name, String field) {
			return subAgg(name, field, null);
		}

		public SimpleAggregationBuilder<SimpleAggregationBuilder<PB>> subAgg(String name, String field, Integer size) {
			SimpleAggregationBuilder<SimpleAggregationBuilder<PB>> subAggs = new SimpleAggregationBuilder<>(this, name, field, size);
			this.asAggregationBuilder().subAggregation(subAggs.aggsBuilder);
			return subAggs;
		}

		public SimpleAggregationBuilder<SimpleAggregationBuilder<PB>> subAgg(AbstractAggregationBuilder aggsBuilder) {
			SimpleAggregationBuilder<SimpleAggregationBuilder<PB>> subAggs = new SimpleAggregationBuilder<>(this, aggsBuilder);
			this.asAggregationBuilder().subAggregation(subAggs.aggsBuilder);
			return subAggs;
		}
		
		public SimpleAggregationBuilder<SimpleAggregationBuilder<PB>> subAggExtendedStats(String name) {
			return subAgg(AggregationBuilders.extendedStats(name));
		}
		
		
	}
	
	public static class RangeQueryer {
        private String key;
        private Double from;
        private Double to;
        
        public RangeQueryer() {
			super();
		}
		public RangeQueryer(Number from, Number to) {
			this(null, from, to);
		}
		public RangeQueryer(String key, Number from, Number to) {
			super();
			this.key = key;
			this.from = (from==null?null:from.doubleValue());
			this.to = (to==null?null:to.doubleValue());
		}
		public boolean isValid(){
        	return this.from!=null || this.to!=null;
        }
		public String getKey() {
			if(key==null){
				return (from==null?0:from.longValue())+"-"+(to==null || Double.isInfinite(to)?"":to.longValue());
			}
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public Double getFrom() {
			return from;
		}
		public void setFrom(Double from) {
			this.from = from;
		}
		public Double getTo() {
			return to;
		}
		public void setTo(Double to) {
			this.to = to;
		}
		@Override
		public String toString() {
			return "[key=" + key + ", from=" + from + ", to=" + to
					+ "]";
		}
	}
	
	public static class RangeResult extends RangeQueryer {
		private int docCount;
        public RangeResult() {
			super();
		}
		public RangeResult(Number from, Number to) {
			super(from, to);
		}
		public int getDocCount() {
			return docCount;
		}
		public void setDocCount(int docCount) {
			this.docCount = docCount;
		}
		@Override
		public String toString() {
			return "[key=" + getKey() + ", from=" + getFrom() + ", to=" + getTo()
					+ ", docCount=" + docCount + "]";
		}
	}

}
