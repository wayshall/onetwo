package org.onetwo.common.db.sql;

import org.onetwo.common.utils.SToken;

public class QueryToken extends SToken {
	
	public QueryToken(String name, int index) {
		super(name, index);
		this.endStrIndex = index;
	}

	private int tokenIndex;
	private int endStrIndex;

	public int getTokenIndex() {
		return tokenIndex;
	}

	public void setTokenIndex(int tokenIndex) {
		this.tokenIndex = tokenIndex;
	}

	public int getEndStrIndex() {
		return endStrIndex;
	}

	public void setEndStrIndex(int endStrIndex) {
		this.endStrIndex = endStrIndex;
	}
	
}
