package io.netty.example.echo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chen
 * @date 2020/11/21
 **/
public class Test {

    static class MyRunnable implements Runnable {

        public final Object lock;
        private String name;

        public MyRunnable(String name, Object lock) {
            this.lock = lock;
            this.name = name;
        }

        @Override
        public void run() {
            synchronized (lock) {
                try {
                    for (int i = 0; i < 1000000; i++) {
                        TimeUnit.SECONDS.sleep(1L);
                        System.out.println(name);
                        if (i == 10) {
                            lock.wait();
                            System.out.println("接触坟茔");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Object lock = new Object();
        MyRunnable chenqwwq = new MyRunnable("Chenqwwq", lock);
        new Thread(chenqwwq).start();
        MyRunnable chenqwwq1 = new MyRunnable("Chenqwwq1", lock);
        new Thread(chenqwwq1).start();
        TimeUnit.SECONDS.sleep(30L);
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
