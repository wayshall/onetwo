package org.onetwo.common.web.s2.tag.webtag;

import org.onetwo.common.utils.params.TagParamsMap;


public interface TagExecutor {
	
	public Object execute(TagParamsMap params);
	
	public boolean needMergeTemplate(TagParamsMap params);
	
	public String getTemplate(TagParamsMap params);

}
