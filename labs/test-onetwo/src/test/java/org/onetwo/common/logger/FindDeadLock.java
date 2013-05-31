package org.onetwo.common.logger;

import org.apache.log4j.Logger;

public class FindDeadLock {
	public static void main(String[] args) {
		new Thread(new Runnable() {
			public void run() {
				System.out.println("run");
				Logger.getLogger(getClass()).info(new FindDeadLock());
			}
		}).start();
		DeadLockingObject.getInstance();
	}

	public String toString() {
		/* now we are inside log4j, try to create a DeadLockingObject */
		DeadLockingObject.getInstance();
		return "Created DeadlockObject, returning string";
	}
}
