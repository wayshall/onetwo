package org.example.app.model.member.entity;

import org.onetwo.common.fish.event.JFishEntityFieldListener;
import org.onetwo.common.fish.event.JFishEntityListener;

public class TestEntityListener implements JFishEntityListener, JFishEntityFieldListener {

	@Override
	public Object beforeFieldInsert(String fieldName, Object value) {
		System.out.println("=============>>>>beforeFieldInsert["+fieldName+"]: "+value);
		return value;
	}

	@Override
	public Object beforeFieldUpdate(String fieldName, Object value) {
		System.out.println("=============>>>>beforeFieldUpdate["+fieldName+"]: "+value);
		return value;
	}

	
	@Override
	public void beforeInsert(Object entity) {
		System.out.println(this.getClass()+"=============>>>>beforeInsert ");
		
	}

	@Override
	public void afterInsert(Object entity) {
		System.out.println(this.getClass()+"=============>>>>afterInsert ");
		
	}

	@Override
	public void beforeUpdate(Object entity) {
		System.out.println(this.getClass()+"=============>>>>beforeUpdate ");
	}

	@Override
	public void afterUpdate(Object entity) {
		System.out.println(this.getClass()+"=============>>>>afterUpdate ");
		
	}


}
