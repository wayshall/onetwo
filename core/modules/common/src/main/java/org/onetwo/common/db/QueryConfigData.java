package org.onetwo.common.db;

import java.util.List;

import org.onetwo.common.utils.LangUtils;

public class QueryConfigData {
	
	private List<String> likeQueryFields;;

	
	public boolean isLikeQueryField(String name){
		if(LangUtils.isEmpty(likeQueryFields))
			return false;
		return likeQueryFields.contains(name);
	}

	public void setLikeQueryFields(List<String> likeQueryFields) {
		this.likeQueryFields = likeQueryFields;
	}

}
