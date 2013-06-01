package org.onetwo.common.spring.mcache;

import java.io.Serializable;

import org.onetwo.common.cache.FlushCache;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class FlushCacheModel {

	public static FlushCacheModel create(FlushCache flushCache, Serializable key) {
		FlushCacheModel f = new FlushCacheModel(flushCache.group(), key);
//		f.setUseKeyHashCode(flushCache.useKeyHashCode());
//		f.setFlushAllOfGroup(flushCache.flushAllOfGroup());
		return f;
	}

	private String[] groups;
	private Serializable key;
//	private boolean useKeyHashCode;
//	private boolean flushAllOfGroup;

	public FlushCacheModel(String[] groups, Serializable key) {
		super();
		this.groups = groups;
		this.key = key;
	}

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	public Serializable getKey() {
		return key;
	}

	public void setKey(Serializable key) {
		this.key = key;
	}
	
	/*public boolean isUseKeyHashCode() {
		return useKeyHashCode;
	}

	public void setUseKeyHashCode(boolean useKeyHashCode) {
		this.useKeyHashCode = useKeyHashCode;
	}*/

	public boolean isValidKey(){
		if(key instanceof String){
			return StringUtils.isNotBlank(key.toString());
		}else{
			return key!=null;
		}
	}
	
	public String toString(){
		return "FlushCacheModel {groups: "+LangUtils.toString(groups)+ ", key:"+key+"}";
	}

}
