package org.onetwo.common.web.solr;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.util.NamedList;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SolrJsonResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7130973721415187659L;

	public static final Logger logger = Logger.getLogger(SolrJsonResult.class);

	private JSONObject responseHeader;
	private JSONObject response;
	private JSONObject highlighting;
	private Map<String, Object> args = new HashMap<String, Object>();
	
	public SolrJsonResult(JSONObject jsonDatas){
		this(jsonDatas, null);
	}
	
	public SolrJsonResult(JSONObject jsonDatas, Iterator<Map.Entry<String, Object>> argsIterator){
		this.responseHeader = jsonDatas.getJSONObject("responseHeader");
		this.response = jsonDatas.getJSONObject("response");
		this.highlighting = jsonDatas.getJSONObject("highlighting");
		
		if(argsIterator!=null){
			while(argsIterator.hasNext()){
				Map.Entry<String, Object> entry = argsIterator.next();
				this.args.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public boolean isHighLighting(){
		return this.highlighting!=null && !this.highlighting.isEmpty() && !this.highlighting.isNullObject();
	}
	
	public Long getNumFound(){
		try {
			return this.response.getLong("numFound");
		} catch (Exception e) {
		}
		return 0l;
	}
	
	public int getStart(){
		int istart = 0;
		try {
			String start = this.responseHeader.getJSONObject("params").getString(CommonParams.START);
			istart = Integer.parseInt(start);
		} catch (Exception e) {
			logger.error(e);
			istart = (Integer)getArgsValue(CommonParams.START, Integer.valueOf(0));
		}
		return istart;
	}
	
	protected Object getArgsValue(String key, Object def){
		if(args==null)
			return null;
		Object val = null;
		try {
			val = ((NamedList)args.get("defaults")).get(key);
			if(val==null)
				val = args.get(key);
		} catch (Exception e) {
		}finally{
			if(val==null)
				val =def;
		}
		return val;
	}
	
	public int getRows(){
		int irows = 0;
		try {
			String rows = this.responseHeader.getJSONObject("params").getString(CommonParams.ROWS);
			irows = Integer.parseInt(rows);
		} catch (Exception e) {
			logger.error(e);
			irows = (Integer)getArgsValue(CommonParams.ROWS, Integer.valueOf(15));
		}
		return irows;
	}
	
	public int getPageNo(){
		return this.getStart()/this.getRows()+1;
	}
	
	public int getTotalPages(){
		int totalCount = getNumFound().intValue();
		int pageSize = getRows();
		if (totalCount < 0)
			return -1;

		int count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	public boolean isHasNext() {
		return (getPageNo() + 1 <= getTotalPages());
	}
	
	public boolean isHasPre() {
		return (getPageNo() - 1 >= 1);
	}
	
	public int getNextStart(){
		int istart = this.getStart()+this.getRows();
		return istart;
	}
	
	public int getPreStart(){
		int istart = this.getStart()-this.getRows();
		if(istart<0)
			istart = 0;
		return istart;
	}

	
	public Long getQtime(){
		return this.responseHeader.getLong("QTime");
	}
	
	public Long getStatus(){
		return this.response.getLong("status");
	}
	
	public String getQueryString(){
		return this.responseHeader.getJSONObject("params").getString(CommonParams.Q);
	}
	
	protected List getDocs(){
		return this.response.getJSONArray("docs");
	}
	
	public List getList(){
		if(!isHighLighting())
			return getDocs();
		return this.getHighlightList();
	}
	
	public List getHighlightList(){
		List<JSONObject> list = getDocs();
		if(list==null || list.isEmpty())
			return list;
		String idkey = null;
		JSONObject hi = null;
		for(JSONObject json : list){
			idkey = json.getString("id");
			hi = this.highlighting.getJSONObject(idkey);
			if(hi!=null && !hi.isEmpty()){
				String hkey = null;
				for(Map.Entry<String, JSONArray> entry: (Set<Map.Entry<String, JSONArray>>)hi.entrySet()){
					hkey = entry.getKey();
					json.put(hkey, entry.getValue().get(0));
				}
			}else{
				Object value = null;
				for(Map.Entry entry: (Set<Map.Entry>)json.entrySet()){
					value = entry.getValue();
					if(value instanceof JSONArray){
						entry.setValue(((JSONArray)value).get(0));
					}
				}
			}
		}
		return list;
	}

	public String toString(){
		return this.responseHeader.toString();
	}
}
