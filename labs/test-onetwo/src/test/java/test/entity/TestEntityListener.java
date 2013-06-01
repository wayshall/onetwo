package test.entity;

import org.onetwo.common.fish.event.JFishEntityListener;

public class TestEntityListener implements JFishEntityListener {

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
