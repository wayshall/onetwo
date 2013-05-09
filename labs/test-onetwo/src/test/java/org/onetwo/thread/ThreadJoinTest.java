package org.onetwo.thread;

public class ThreadJoinTest extends Thread {
	public static void main(String[] args) throws InterruptedException {
		ThreadJoinTest t = new ThreadJoinTest();
		t.start();
		t.join();//等待线程结束
		System.out.println("finish!");
	}

	@Override
	public void run() {
		System.out.println("thread test");
	}
	
}
