package org.onetwo.common.utils.map;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.VerySimpleStartMatcher;
import org.onetwo.common.utils.list.L;

@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
public class CasualMap extends IncreaseMap{
	
	public CasualMap(){super();};

	public CasualMap(Map map){
		super(map);
	}
	
	public CasualMap(String paramStr){
		this.putEntryByString(paramStr);
	}
	
	protected void putEntryByString(String paramStr){
		if(StringUtils.isBlank(paramStr))
			return ;
		
		String[] params = StringUtils.split(paramStr, "&");
		if(LangUtils.isEmpty(params)) return;
		
		for(String param : params){
			String[] pv = StringUtils.split(param, "=");
			
			if(pv.length < 2){
				increasePut(pv[0], "");
			}else{
				increasePut(pv[0], pv[1]);
			}
		}
	}
	
	public CasualMap subtract(Map map){
		return (CasualMap) CUtils.subtract(this, map, true);
	}
	
	public CasualMap addWithout(Map map, String...prefixs){
		if(map==null || map.isEmpty())
			return this;
		for(Map.Entry entry : (Set<Map.Entry>)map.entrySet()){
			if(matchPrefix(entry.getKey().toString(), prefixs))
				continue;
			put(entry.getKey(), entry.getValue());
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
	

	
	public CasualMap addHttpParameterWithout(Map map, String...prefixs){
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
			if(values.length==1)
				put(entry.getKey(), values[0]);
			else
				put(entry.getKey(), values);
		}
		return this;
	}
	
	public String toParamString(){
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(Map.Entry entry : (Set<Map.Entry>)entrySet()){
			if(entry.getValue()==null)
				continue;
			List values = L.tolist(entry.getValue(), true);
			for(Object value : values){
				if(index!=0)
					sb.append("&");
				sb.append(entry.getKey()).append("=").append(value.toString());
				index++;
			}
		}
		return sb.toString();
	}
	
	public Map diffence(Map map){
		if(map==null || map.isEmpty())
			return Collections.EMPTY_MAP;
		CasualMap rs = new CasualMap();
		Object myValue=null;
		for(Map.Entry entry : (Set<Map.Entry>)map.entrySet()){
			if(!containsKey(entry.getKey()))
				continue;
			myValue = this.get(entry.getKey());
			if(myValue==null){
				if(entry.getValue()!=null){
					rs.put(entry.getKey(), myValue);
					rs.increasePut(entry.getKey(), entry.getValue());
				}
			}else{
				if(!myValue.equals(entry.getValue())){
					rs.put(entry.getKey(), myValue);
					rs.increasePut(entry.getKey(), entry.getValue());
				}
			}
		}
		return rs;
	}
}
