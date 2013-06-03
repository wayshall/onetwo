package org.onetwo.common.web.solr;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.SearchComponent;
import org.apache.solr.request.SolrQueryRequest;
import org.onetwo.common.web.utils.Tool;

import com.opensymphony.xwork2.ActionContext;

public class EncodeQueryComponent extends SearchComponent {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	public static final String COMPONENT_NAME = "encodeQueryComponent";

	@Override
	public void prepare(ResponseBuilder rb) throws IOException {
	    SolrQueryRequest req = rb.req;
	    SolrParams params = req.getParams();
	    ModifiableSolrParams mp = new ModifiableSolrParams(params);
	    setParamsFromActionContext(params, mp, CommonParams.Q, CommonParams.START, CommonParams.ROWS);
	    req.setParams(mp);
	}
	
	protected void setParamsFromActionContext(SolrParams params, ModifiableSolrParams mp, String... paramName){
		if(paramName==null || paramName.length<1 || ActionContext.getContext()==null)
			return ;
		Object val = null;
		for(String pn : paramName){
	    	val = ActionContext.getContext().getParameters().get(pn);
    		if(val == null || StringUtils.isBlank(val.toString()))
    			continue;
    		if(String[].class.isAssignableFrom(val.getClass())){
    			val = ((String[])val)[0];
    		}
    		if(CommonParams.Q.equals(pn))
    			val = Tool.getInstance().decode(val.toString());
    		mp.set(pn, val.toString());
			if(logger.isInfoEnabled()){
				logger.info("==========>>> set to solr context : ["+pn+", "+val+"]");
			}
		}
	}

	@Override
	public void process(ResponseBuilder rb) throws IOException {
	}

	@Override
	public String getDescription() {
		return "query";
	}

	@Override
	public String getVersion() {
		return "$Revision: 812246 $";
	}

	@Override
	public String getSourceId() {
		return "$Id: QueryComponent.java 812246 2009-09-07 18:28:16Z yonik $";
	}

	@Override
	public String getSource() {
		return "$URL: https://svn.apache.org/repos/asf/lucene/solr/branches/branch-1.4/src/java/org/apache/solr/handler/component/QueryComponent.java $";
	}

	@Override
	public URL[] getDocs() {
		return null;
	}
}
