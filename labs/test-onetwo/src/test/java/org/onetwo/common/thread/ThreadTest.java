package org.onetwo.common.thread;

import org.onetwo.common.utils.LangUtils;

public class ThreadTest {
	
	public static void main(String[] args){
		final MutilThreadAccessBean m = new MutilThreadAccessBean();
		new Thread(){
			public void run(){
				m.doInThread();
			}
		}.start();
		
		LangUtils.await(3);

		new Thread(){
			public void run(){
				m.doInThread();
			}
		}.start();
		
	}

}
