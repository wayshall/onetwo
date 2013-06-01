package org.onetwo.common.utils.params;

import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;

@SuppressWarnings("unchecked")
abstract public class ParameterUtils {
 
	public static String appendParamString(String url, String paramStr){
		if(StringUtils.isBlank(paramStr))
			return url;
		if(url.indexOf('?')==-1)
			url = url + "?";
		if(paramStr.startsWith("?"))
			paramStr = paramStr.substring(1);
		if(url.endsWith("?")){
			if(paramStr.startsWith("&"))
				paramStr = paramStr.substring(1);
		}
		else if(url.endsWith("&")){
			if(paramStr.startsWith("&"))
				paramStr = paramStr.substring(1);
		}else{
			if(!paramStr.startsWith("&"))
				url += "&";
		}
		url = url+paramStr;
		if(url.endsWith("&"))
			url = url.substring(0, url.length()-1);
		return url;
	}
	public static String toParamsString(Map params){
		return toParamsString(params, null, null);
	}
	
	public static String toParamsString(Map params, Transformer keyTransformer, Transformer valueTransformer){
		if(params==null || params.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		Object value = null;
		String name = null;
		for(Map.Entry entry : (Set<Map.Entry>)params.entrySet()){
			name = (String)entry.getKey();
			value = entry.getValue();
			if(name==null || value==null)
				continue;
			
			if(keyTransformer!=null)
				name = (String)keyTransformer.transform(name);
			if(valueTransformer!=null)
				value = valueTransformer.transform(value);
			
			if(!first)
				sb.append("&");
			if(value.getClass().isArray()){
				for(Object val : (Object[]) value){
					if(!first)
						sb.append("&");
					sb.append(name).append("=").append(val);
					first = false;
				}
			}else{
				sb.append(name).append("=").append(value.toString());
				first = false;
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		String url = "asdfs.do";
		String paramStr = "?xx=yyy&zzz=test&";
		System.out.println(appendParamString(url, paramStr));
	}
}
