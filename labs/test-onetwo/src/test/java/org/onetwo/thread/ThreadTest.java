package org.onetwo.thread;

public class ThreadTest extends Thread {
	public static void main(String[] args) {
		for (int i = 1; i <= 2; i++) {
			new ThreadTest().start();
		}
	}

	public void run() {
		System.out.print("1");
		yield();
		System.out.print("2");
	}
}
