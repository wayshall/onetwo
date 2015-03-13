package org.onetwo;

import org.onetwo.common.profiling.TimeCounter;

import test.entity.UserEntity;



public class Test {

	public static void main(String[] args) throws Exception {
		TimeCounter t = new TimeCounter(Test.class);
		t.start();
		UserEntity user = new UserEntity();
		for (int i = 0; i < 500000; i++) {
			user.setId(Long.valueOf(i));
		}
		t.stop();
	}
	
	
}
