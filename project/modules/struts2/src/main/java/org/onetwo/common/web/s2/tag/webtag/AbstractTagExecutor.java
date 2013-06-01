package org.onetwo.common.web.s2.tag.webtag;

import org.apache.log4j.Logger;
import org.onetwo.common.utils.params.TagParamsMap;

abstract public class AbstractTagExecutor implements TagExecutor {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	@Override
	public String getTemplate(TagParamsMap params) {
		return null;
	}

	@Override
	public boolean needMergeTemplate(TagParamsMap params) {
		return false;
	}

}
