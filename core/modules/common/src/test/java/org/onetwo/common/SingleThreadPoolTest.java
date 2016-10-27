package org.onetwo.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadPoolTest {
    public static void main(String[] args) {
        for (int i = 0; i < 2000; i++) {
            newSingleThreadPool();
        }
    }
    private static void newSingleThreadPool() {
    	ExecutorService executor = Executors.newSingleThreadExecutor();
    	executor.submit(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = new byte[1024 * 1024 * 4];
                System.out.println(Thread.currentThread().getName());
            }
        });
    }
}
