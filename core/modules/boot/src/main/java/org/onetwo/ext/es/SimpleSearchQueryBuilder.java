package org.onetwo.ext.es;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.onetwo.common.utils.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;



public class SimpleSearchQueryBuilder {
	
	public static SimpleSearchQueryBuilder newBuilder(){
		return new SimpleSearchQueryBuilder();
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
	private SimpleBooleanQueryBuilder booleanQuery;
//	private boolean built = false;
	private NativeSearchQuery nativeSearchQuery;
	private List<Sort> sorts = Lists.newArrayList();

	public SimpleSearchQueryBuilder() {
		this(new NativeSearchQueryBuilder());
	}

	public SimpleSearchQueryBuilder(int pageNo, int pageSize) {
		this(new NativeSearchQueryBuilder());
		this.searchQueryBuilder.withPageable(new PageRequest(pageNo, pageSize));
	}

	public SimpleSearchQueryBuilder(NativeSearchQueryBuilder searchQueryBuilder) {
		super();
		this.searchQueryBuilder = searchQueryBuilder;
	}

	public SimpleBooleanQueryBuilder bool(){
		if(booleanQuery==null){
			booleanQuery = new SimpleBooleanQueryBuilder();
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

	public SimpleAggregationBuilder aggs() {
		return new SimpleAggregationBuilder();
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
		if(booleanQuery!=null){
			searchQueryBuilder.withFilter(booleanQuery.boolQuery);
		}
		this.nativeSearchQuery = searchQueryBuilder.build();
		this.sorts.forEach(s->this.nativeSearchQuery.addSort(s));
//		built = true;
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
	
	public <R> R doQuery(Function<NativeSearchQuery, R> func){
		NativeSearchQuery searchQuery = build(true);
		return func.apply(searchQuery);
	}
	
	public <R> R doQuery(ElasticsearchTemplate elasticsearchTemplate, ResultsExtractor<R> resultsExtractor){
		NativeSearchQuery searchQuery = build(true);
		return elasticsearchTemplate.query(searchQuery, resultsExtractor);
	}
	
	abstract public class ExtBaseQueryBuilder {
		public SimpleSearchQueryBuilder end(){
			return SimpleSearchQueryBuilder.this;
		}
	}
	
	public class SimpleBooleanQueryBuilder extends ExtBaseQueryBuilder {
		private BoolQueryBuilder boolQuery = boolQuery();
		
		public SimpleBooleanQueryBuilder mustTerm(String field, Object value){
			Assert.hasText(field);
			if(!StringUtils.isNullOrBlankString(value))
				boolQuery.must(termQuery(field, value));
			return this;
		}
		
		public SimpleBooleanQueryBuilder mustTerms(String field, Object... values){
			Assert.hasText(field);
			if(values!=null && values.length>0){
				List<Object> listValue = Lists.newArrayList(values);
				listValue.removeIf(Objects::isNull);
				if(!listValue.isEmpty()){
					boolQuery.must(QueryBuilders.termsQuery(field, listValue.toArray(new Object[0])));
				}
			}
			return this;
		}
		
		public SimpleBooleanQueryBuilder mustTerms(String field, Collection<?> values){
			Assert.hasText(field);
			if(values!=null && !values.isEmpty())
				boolQuery.must(QueryBuilders.termsQuery(field, values));
			return this;
		}
		
		public SimpleBooleanQueryBuilder mustTermOrMissing(String field, Object value){
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
		public SimpleBooleanQueryBuilder missing(String field){
			return mustNotExists(field);
		}
		public SimpleBooleanQueryBuilder mustNotExists(String field){
			Assert.hasText(field);
			boolQuery.mustNot(QueryBuilders.existsQuery(field));
			return this;
		}
		/***
		 * is not null
		 * @param field
		 * @return
		 */
		public SimpleBooleanQueryBuilder mustExists(String field){
			Assert.hasText(field);
			boolQuery.must(QueryBuilders.existsQuery(field));
			return this;
		}
		
		public SimpleBooleanQueryBuilder shouldTerms(String field, Collection<?> values){
			Assert.hasText(field);
			if(values!=null && !values.isEmpty())
				boolQuery.should(QueryBuilders.termsQuery(field, values));
			return this;
		}
		
		public SimpleBooleanQueryBuilder multiMustTerm(Object value, String... fields){
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

	public class SimpleAggregationBuilder extends ExtBaseQueryBuilder {
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
		public SimpleAggregationBuilder terms(String name, String field, Integer size) {
			TermsBuilder terms = AggregationBuilders.terms(name);
			if(size!=null){
				terms.field(field).size(size);
			}else{
				terms.field(field);
			}
			terms.order(Terms.Order.count(false));//COUNT_DESC
			SimpleSearchQueryBuilder.this.searchQueryBuilder.addAggregation(terms);
			return this;
		}
		
		public TermsBuilder termsBuilder(String name, String field) {
//			TermsBuilder terms = AggregationBuilders.terms(name);
			TermsBuilder terms = new TermsBuilder(name);
			terms.field(field);
			SimpleSearchQueryBuilder.this.searchQueryBuilder.addAggregation(terms);
			return terms;
		}
	}

}
