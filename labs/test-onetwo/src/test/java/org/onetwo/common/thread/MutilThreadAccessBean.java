package org.onetwo.common.thread;

public class MutilThreadAccessBean {
	
	private Object prop = new Object();
	
	public void doInThread(){
		System.out.println("start.......");
		synchronized (prop) {
			System.out.println(Thread.currentThread().getId()+":doInThread");
			System.out.println("dosomething.......");
		}
		System.out.println("end.......");
	}
	
	public synchronized void doInThreadWithSyn(){
		System.out.println("start.......");
		System.out.println(Thread.currentThread().getId()+":doInThread");
		System.out.println("end.......");
	}

}
