package org.onetwo.common.web.s2.tag.webtag;

import java.util.List;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.params.TagParamsMap;

@SuppressWarnings("unchecked")
abstract public class DataListTagExecutor extends AbstractTagExecutor {
	
	public Object execute(TagParamsMap params){
		try {
			return getDataList(params.getFirstResult(), params.getMaxResults(), params);
		} catch (Exception e) {
			String msg = "获取数据出错！";
			logger.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	abstract public List getDataList(int firstResult, int maxResults, TagParamsMap params);

}
