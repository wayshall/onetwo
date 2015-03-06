package org.onetwo.common.utils.map;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.VerySimpleStartMatcher;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CasualMap extends ListMap<Object, Object>{
	
	public CasualMap(){super();};

	public CasualMap(Map<Object, List<Object>> map){
		super(map);
	}
	
	public CasualMap(String paramStr){
		this.putEntryByString(paramStr);
	}
	
	protected void putEntryByString(String paramStr){
		if(StringUtils.isBlank(paramStr))
			return ;
		
		int paramStart = paramStr.indexOf('?');
		if(paramStart!=-1){
			paramStr = paramStr.substring(paramStart+1);
		}
		String[] params = StringUtils.split(paramStr, "&");
		if(LangUtils.isEmpty(params)) return;
		
		for(String param : params){
			String[] pv = StringUtils.split(param, "=");
			
			if(pv.length < 2){
				putElement(pv[0], "");
			}else{
				putElement(pv[0], pv[1]);
			}
		}
	}
	
	public CasualMap subtract(Map<Object, List<Object>> map){
		return (CasualMap) CUtils.subtract(this, map, true);
	}
	
	public CasualMap addWithFilter(Map<Object, List<Object>> map, String...prefixs){
		if(map==null || map.isEmpty())
			return this;
		for(Map.Entry<Object, List<Object>> entry : (Set<Map.Entry<Object, List<Object>>>)map.entrySet()){
			if(matchPrefix(entry.getKey().toString(), prefixs))
				continue;
			for(Object v : entry.getValue()){
				putElement(entry.getKey(), v);
			}
		}
		return this;
	}
	
	protected boolean matchPrefix(String key, String... prefixs){
		for(String prefix : prefixs){
			VerySimpleStartMatcher match = VerySimpleStartMatcher.create(prefix);
			if(match.match(key)){
				return true;
			}
		}
		return false;
	}
	

	public CasualMap filter(String...patterns){
		LangUtils.filterMap(this, patterns);
		return this;
	}
	

	
	public CasualMap addMapWithFilter(Map map, String...prefixs){
		if(map==null || map.isEmpty())
			return this;
		for(Map.Entry entry : (Set<Map.Entry>)map.entrySet()){
			if(matchPrefix(entry.getKey().toString(), prefixs))
				continue;
			Object val = entry.getValue();
			Object[] values = null;
			if(val instanceof List){
				values = (Object[])((List<Object>)val).toArray();
			}else if(val instanceof Object[]){
				values = (Object[]) val;
			}else{
				values = new Object[]{val};
			}
			for(Object v : values){
				putElement(entry.getKey(), v);
			}
		}
		return this;
	}
	
	public String toParamString(){
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(Map.Entry<Object, List<Object>> entry : (Set<Map.Entry<Object, List<Object>>>)entrySet()){
			if(entry.getValue()==null)
				continue;
			List values = entry.getValue();
			for(Object value : values){
				if(index!=0)
					sb.append("&");
				sb.append(entry.getKey()).append("=").append(value.toString());
				index++;
			}
		}
		return sb.toString();
	}
	
}
