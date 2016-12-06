package org.onetwo.ext.es;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

public class SimpleSuggestionBuilderTest {
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
//	@Test
	public void testSuggest(){
		String keyword = "test";
		List<String> texts = SimpleSuggestionBuilder.from("indexName")
														.name("searchCompletion")
														.field("autoCompletion")
														.text(keyword)
														.completionFuzzy()
														.getTexts(elasticsearchTemplate);
		assertThat(texts, contains(""));
	}

}
