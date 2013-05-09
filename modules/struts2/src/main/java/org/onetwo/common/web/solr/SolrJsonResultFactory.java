package org.onetwo.common.web.solr;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.json.JSONUtils;

@SuppressWarnings({ "serial" })
abstract public class SolrJsonResultFactory implements Serializable {
	
	public static SolrJsonResult createJsonResult(QueryResponseWriter responseWriter, SolrQueryRequest solrReq, SolrQueryResponse solrRsp, Iterator<Map.Entry<String, Object>> argsIterator){
		StringWriter out = new StringWriter();
		try {
			responseWriter.write(out, solrReq, solrRsp);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("build solr result error: " + e.getMessage());
		}
		
		String json = out.toString();
		JSONObject jsonDatas = JSONObject.fromObject(json, JSONUtils.JSON_CONFIG);
		
		SolrJsonResult result = new SolrJsonResult(jsonDatas, argsIterator);
		return result;
	}
	

}
