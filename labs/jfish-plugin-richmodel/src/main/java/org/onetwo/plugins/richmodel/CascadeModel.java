package org.onetwo.plugins.richmodel;

import java.util.List;

import org.onetwo.common.fish.JFishQueryBuilder;

public class CascadeModel {
	
	private JFishQueryBuilder queryBuilder;

	public CascadeModel(JFishQueryBuilder queryBuilder) {
		super();
		this.queryBuilder = queryBuilder;
	}
	
	public <T> List<T> list(){
		return queryBuilder.list();
	}
	
	public <T> T one(){
		return queryBuilder.one();
	}
	
	public JFishQueryBuilder where(){
		return queryBuilder;
	}

}
