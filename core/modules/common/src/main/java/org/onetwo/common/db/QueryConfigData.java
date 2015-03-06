package org.onetwo.common.db;

import java.util.List;

import org.onetwo.common.utils.LangUtils;

public class QueryConfigData {

	public static final QueryConfigData EMPTY_CONFIG = new QueryConfigData(true){

		public void setLikeQueryFields(List<String> likeQueryFields) {
			throw new UnsupportedOperationException();
		}
	};
	
	private List<String> likeQueryFields;
	private final boolean statful;

	public QueryConfigData(boolean statful) {
		super();
		this.statful = statful;
	}

	public boolean isLikeQueryField(String name){
		if(LangUtils.isEmpty(likeQueryFields))
			return false;
		return likeQueryFields.contains(name);
	}

	public void setLikeQueryFields(List<String> likeQueryFields) {
		this.likeQueryFields = likeQueryFields;
	}

	public boolean isStatful() {
		return statful;
	}

}
