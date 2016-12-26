package org.onetwo.common.utils.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ParamUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.VerySimpleStartMatcher;

/****
 * CasualMap
 * @author way
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ParamMap extends CollectionMap<Object, Object>{
	
	public ParamMap(){super();};

	public ParamMap(Map<Object, Collection<Object>> map){
		super(map);
	}
	
	public ParamMap(String paramStr){
		this(paramStr, null);
	}
	public ParamMap(String paramStr, Map<Object, Collection<Object>> map){
		super(map);
		this.putEntryByString(paramStr);
	}
	
	final protected void putEntryByString(String paramStr){
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
	
	public ParamMap subtract(Map<Object, Collection<Object>> map){
		return (ParamMap) CUtils.subtract(this, map, false);
	}
	
	public ParamMap addWithFilter(Map<Object, Collection<Object>> map, String...prefixs){
		if(map==null || map.isEmpty())
			return this;
		for(Map.Entry<Object, Collection<Object>> entry : (Set<Map.Entry<Object, Collection<Object>>>)map.entrySet()){
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
	

	public ParamMap filter(String...patterns){
		LangUtils.filterMap(this, patterns);
		return this;
	}
	

	
	public ParamMap addMapWithFilter(Map map, String...prefixs){
		if(map==null || map.isEmpty())
			return this;
		for(Map.Entry entry : (Set<Map.Entry>)map.entrySet()){
			if(matchPrefix(entry.getKey().toString(), prefixs))
				continue;
			Object val = entry.getValue();
			Object[] values = null;
			if(val instanceof Collection){
				values = (Object[])((Collection<Object>)val).toArray();
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
		/*StringBuilder sb = new StringBuilder();
		int index = 0;
		for(Map.Entry<Object, Collection<Object>> entry : (Set<Map.Entry<Object, Collection<Object>>>)entrySet()){
			if(entry.getValue()==null)
				continue;
			Collection values = entry.getValue();
			for(Object value : values){
				if(index!=0)
					sb.append("&");
				sb.append(entry.getKey()).append("=").append(value.toString());
				index++;
			}
		}
		return sb.toString();*/
		return ParamUtils.mapToParamString(this);
	}
	
}
