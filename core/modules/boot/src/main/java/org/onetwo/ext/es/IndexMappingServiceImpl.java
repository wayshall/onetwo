package org.onetwo.ext.es;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.ext.es.ESUtils.EsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;

@Service
public class IndexMappingServiceImpl {
	
	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	private RestTemplate restTemplate;
	@Value("${es.cluster.nodes}")
	private String esNodes;
	
	private Map<String, String> indexFileMapping = Maps.newHashMap();


	public IndexMappingServiceImpl map(String indexName, String mappingFilePath){
		this.indexFileMapping.put(indexName, mappingFilePath);
		return this;
	}
	public boolean deleteType(String indexName, String typeName){
		return doWithEsNode(node->{
			String url = buildTypeUrl(node, indexName, typeName);
			deleteByUrl(url);
			return true;
		});
	}

	public boolean deleteIndex(String indexName){
		return doWithEsNode(node->{
			String url = buildIndexUrl(node, indexName);
			deleteByUrl(url);
			return true;
		});
	}
	
	public boolean rebuildIndex(String indexName){
		return doWithEsNode(node->{
			return rebuildIndex(node, indexName);
		});
	}
	
	public boolean doWithEsNode(Function<EsNode, Boolean> action){
		Assert.hasText(esNodes);
		List<EsNode> nodes = ESUtils.parseEsNodes(esNodes);
		return nodes.stream().anyMatch(node->{
			try {
				return action.apply(node);
			} catch (Exception e) {
				logger.error("execute by["+node.getHost()+"] error:", e);
				return false;
			}
		});
	}

	private String buildIndexUrl(EsNode node, String indexName){
		String url = "http://%s:9200/%s";
		url = String.format(url, node.getHost(),  indexName);
		return url;
	}
	private String buildTypeUrl(EsNode node, String indexName, String typeName){
		String url = "http://%s:9200/%s/%s";
		url = String.format(url, node.getHost(),  indexName, typeName);
		return url;
	}
	
	private boolean deleteByUrl(String indexUrl){
		try {
			logger.info("delete : {}", indexUrl);
			this.restTemplate.delete(indexUrl);
			return true;
		} catch (Exception e) {
			logger.error("delete error, indexUrl: "+indexUrl);
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean rebuildIndex(EsNode node, String indexName){
		Assert.notNull(node);
		
		String url = buildIndexUrl(node, indexName);
		this.deleteByUrl(url);
		
		Map<Object, Object> setttings = readIndexMapping(indexName);
		logger.info("create index with mappting: {}", setttings);
		Map<String, Object> res = this.restTemplate.postForObject(url, setttings, Map.class);
		logger.info("create type result: {}", res);
		if(res!=null && Boolean.TRUE.equals(res.get("acknowledged"))){
			return true;
		}
		return false;
	}

	protected Map<Object, Object> readIndexMapping(String indexName){
		String path = this.indexFileMapping.get(indexName);
		if(StringUtils.isBlank(path)){
			path = indexName;
		}
		return ESUtils.readMapping("mapping/"+path+".mapping.json");
	}

}
