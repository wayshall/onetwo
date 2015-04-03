package org.onetwo.java8;

public class Test88 {

	public static void main(String[] args){
		runit(new Runnable() {
			@Override
			public void run() {
				System.out.println("inner run");
			}
		});
		System.out.println("===========");
		runit(()->{
			System.out.println("lambda run");
		});
	}
	
	public static void runit(Runnable runable){
		runable.run();
	}
}
