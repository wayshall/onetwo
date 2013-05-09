package org.onetwo.common.web.solr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.servlet.DirectSolrConnection;
import org.apache.solr.servlet.SolrRequestParsers;
import org.apache.solr.servlet.cache.HttpCacheHeaderUtil;
import org.apache.solr.servlet.cache.Method;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.web.utils.Tool;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SolrContext {
	
	public static SolrContext instance;
	private static Map<String, String> opMapper = new HashMap<String, String>();
//	private static Map<String, String> actionMapper = new HashMap<String, String>();
	
	static {
		
		opMapper.put("+", "");
		opMapper.put("-", "");
		opMapper.put("&&", "");
		opMapper.put("||", "");
		opMapper.put("!", "");
		opMapper.put("(", "");
		opMapper.put(")", "");
		opMapper.put("{", "");
		opMapper.put("}", "");
		opMapper.put("[", "");
		opMapper.put("]", "");
		opMapper.put("^", "");
		opMapper.put("\"", "");
		opMapper.put("~", "");
		opMapper.put("*", "");
		opMapper.put("?", "");
		opMapper.put(":", "");
	}
	
	public static SolrContext init(CoreContainer cores, Map<SolrConfig, SolrRequestParsers> parsers){
		instance = new SolrContext(cores, parsers);
		return instance;
	}
	
	public static SolrContext getInstance() {
		if(instance==null)
			throw new ServiceException("no solr context");
		return instance;
	}
	
	public static boolean isInit(){
		return instance!=null;
	}
	
	protected CoreContainer cores;
	protected final Map<SolrConfig, SolrRequestParsers> parsers;
	protected DirectSolrConnection connection;

	private SolrContext(CoreContainer cores, Map<SolrConfig, SolrRequestParsers> parsers) {
		super();
		this.cores = cores;
		this.parsers = parsers;
		if(cores!=null)
			this.connection = new DirectSolrConnection(cores.getCore(""));
	}

	public CoreContainer getCores() {
		return cores;
	}

	public Map<SolrConfig, SolrRequestParsers> getParsers() {
		return parsers;
	}
	
	public SolrRequestParsers getParser(SolrCore core){
		final SolrConfig config = core.getSolrConfig();
		SolrRequestParsers parser = null;

		if (core != null) {
			parser = getParsers().get(config);
			if (parser == null) {
				parser = new SolrRequestParsers(config);
				getParsers().put(config, parser);
			}
		}
		return parser;
	}
	
	public SolrQueryRequest getSolrQueryRequest(SolrCore core, String path, HttpServletRequest request){
		SolrQueryRequest solrReq = null;
		try {
			solrReq = getParser(core).parse(core, path, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("搜索出错了：" + e.getMessage());
		}
		return solrReq;
	}
	
	public SolrRequestHandler getHandler(SolrQueryRequest solrReq){
		SolrRequestHandler handler = null;
		
		String qt = solrReq.getParams().get(CommonParams.QT);
		handler = solrReq.getCore().getRequestHandler(qt);
		
		return handler;
	}
	
	protected String filterWord(String q){
		if(StringUtils.isBlank(q))
			return q;
		StringBuilder newStr = new StringBuilder();
		char[] chars = q.trim().toCharArray();
		String ch;
		for(int i = 0; i<chars.length; i++){
			ch = String.valueOf(chars[i]);
			if(opMapper.containsKey(ch)){
				if(i==0){
					String v = opMapper.get(ch);
					if(StringUtils.isNotBlank(v))
						newStr.append(v);
					else
						newStr.append("\\"+ch);
				}else
					newStr.append("");
			}else{
				newStr.append(ch);
			}
		}
		return newStr.toString();
	}
	
	public SolrJsonResult search(HttpServletRequest request, HttpServletResponse response, String q){
		if(StringUtils.isBlank(q))
			throw new ServiceException("搜索的关键字不能为空!");

		q = filterWord(q);
		if(StringUtils.isBlank(q))
			return null;
		
		q = Tool.getInstance().decode(q);
		if(ActionContext.getContext()!=null)
			ActionContext.getContext().getParameters().put(CommonParams.Q, q);
		SolrCore core = SolrContext.getInstance().getCores().getCore("");
		SolrRequestHandler handler = null;
		SolrQueryRequest solrReq = null;
		
		final SolrConfig config = core.getSolrConfig();
		String path = request.getServletPath();
		solrReq = getSolrQueryRequest(core, path, request);
		handler = getHandler(solrReq);
		
		if(handler==null)
			throw new ServiceException("no handler!");
		 
		SolrJsonResult result = null;
		try {
			if (config.getHttpCachingConfig().isNever304() || !HttpCacheHeaderUtil.doCacheHeaderValidation(solrReq, request, Method.GET, response)) {
				SolrQueryResponse solrRsp = new SolrQueryResponse();
				solrReq.getCore().execute(handler, solrReq, solrRsp);
				HttpCacheHeaderUtil.checkHttpCachingVeto(solrRsp, response, Method.GET);
				QueryResponseWriter responseWriter = core.getQueryResponseWriter(solrReq);

				Iterator args = ((RequestHandlerBase) handler).getInitArgs().iterator();
				if (solrRsp.getException() != null) {
					throw new ServiceException(solrRsp.getException());
				} else {
					result = SolrJsonResultFactory.createJsonResult(responseWriter, solrReq, solrRsp, args);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("搜索出错了：" + e.getMessage());
		}
		return result;
	}
	
	public String deleteById(String id){
		StringBuilder body = new StringBuilder();
		String result = null;
		try {
			body.append("<delete><id>").append(id).append("</id></delete>");
			result = this.connection.request("/update/?commit=true&optimize=true", body.toString());
		} catch (Exception e) {
			throw new ServiceException("delete index error : "+e.getMessage(), e);
		}
		return result;
	}

	public String save(String...strings){
		Map params = MyUtils.convertParamMap((Object[])strings);
		return save(params);
	}
	
	public String save(Map<String, String> params){
		if(params==null || params.isEmpty())
			return null;
		StringBuilder body = new StringBuilder();
		String result = null;
		try {
			body.append("<add><doc>");
			for(Map.Entry<String, String> entry : params.entrySet()){
				body.append("<field name=\"").append(entry.getKey()).append("\">");
				body.append(entry.getValue());
				body.append("</field>");
			}
			body.append("</doc></add>");
			result = this.connection.request("/update/?commit=true&optimize=true", body.toString());
		} catch (Exception e) {
			throw new ServiceException("delete index error : "+e.getMessage(), e);
		}
		return result;
	}
	

	public static void main(String[] args) throws Exception {
	    new SolrContext(null, null).save("id", "sdf", "bb", "sfs");
	    new SolrContext(null, null).deleteById("23r");
	}
}
