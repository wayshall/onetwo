package org.onetwo.common.thread;

public class ThreadBean extends Thread {

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getId());
	}

}
