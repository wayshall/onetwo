package org.onetwo.ext.es;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.Suggest.Suggestion;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Entry.Option;
import org.elasticsearch.search.suggest.SuggestBuilder.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionFuzzyBuilder;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.onetwo.common.utils.StringUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

public class SimpleSuggestionBuilder {
	public static SimpleSuggestionBuilder from(String...indexs){
		return new SimpleSuggestionBuilder(indexs);
	}
	
	private SuggestionBuilder<?> suggestion;
	private List<String> indices;
	private String name;
	private String field;
	private String text;
	private Integer size;
	

	public <R> R get(ElasticsearchTemplate elasticsearchTemplate, Function<SuggestResponse, R> mapper){
		Assert.notNull(this.suggestion);
		Assert.notEmpty(indices);
		SuggestResponse response = elasticsearchTemplate.suggest(suggestion, this.indices.toArray(new String[0]));
		
		return mapper.apply(response);
	}
	
	public List<String> getTexts(ElasticsearchTemplate elasticsearchTemplate){
		return getOptions(elasticsearchTemplate).stream()
												.map(opt->opt.getText().string())
												.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public List<? extends Option> getOptions(ElasticsearchTemplate elasticsearchTemplate){
		if(StringUtils.isBlank(text)){
			return Collections.emptyList();
		}
		return get(elasticsearchTemplate, response->{
			Suggestion<?> suggestion = response.getSuggest().getSuggestion(name);
			if(suggestion==null || suggestion.getEntries().isEmpty()){
				return Collections.emptyList();
			}
			Suggestion.Entry<? extends Option> entry = suggestion.getEntries().get(0);
			List<? extends Option> options = entry.getOptions();
			return options;
		});
	}
	
	private SimpleSuggestionBuilder(String... indices) {
		super();
		this.indices = Lists.newArrayList(indices);
	}

	public SimpleSuggestionBuilder name(String name) {
		this.name = name;
		return this;
	}
	public SimpleSuggestionBuilder field(String field){
		this.field = field;
		return this;
	}
	public SimpleSuggestionBuilder text(String text){
		this.text = text;
		return this;
	}
	//term
	public SimpleSuggestionBuilder term(){
		return term(null);
	}
	public SimpleSuggestionBuilder term(Consumer<TermSuggestionBuilder> consumer){
		TermSuggestionBuilder term = new TermSuggestionBuilder(name).field(field).text(text);
		build(term, consumer);
		return this;
	}
	
	//phrase
	public SimpleSuggestionBuilder phrase(){
		return phrase(null);
	}
	public SimpleSuggestionBuilder phrase(Consumer<PhraseSuggestionBuilder> consumer){
		PhraseSuggestionBuilder phrase = new PhraseSuggestionBuilder(name).field(field).text(text);
		build(phrase, consumer);
		return this;
	}
	
	//completion
	public SimpleSuggestionBuilder completion(){
		return completion(null);
	}
	public SimpleSuggestionBuilder completion(Consumer<CompletionSuggestionBuilder> consumer){
		CompletionSuggestionBuilder completion = new CompletionSuggestionBuilder(name).field(field).text(text);
		build(completion, consumer);
		return this;
	}
	
	//completionFuzzy
	public SimpleSuggestionBuilder completionFuzzy(){
		return completionFuzzy(null);
	}
	public SimpleSuggestionBuilder completionFuzzy(Consumer<CompletionSuggestionFuzzyBuilder> consumer){
		CompletionSuggestionFuzzyBuilder completion = new CompletionSuggestionFuzzyBuilder(name).field(field).text(text);
		build(completion, consumer);
		return this;
	}
	
	private <T extends SuggestionBuilder<?>> void build(T suggest, Consumer<T> consumer){
		if(size!=null){
			suggest.size(size);
		}
		if(consumer!=null){
			consumer.accept(suggest);
		}
		this.suggestion = suggest;
	}
	
}
