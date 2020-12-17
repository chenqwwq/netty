package io.netty.example.echo;

import io.netty.util.concurrent.FastThreadLocal;

/**
 * @author chen
 * @date 2020/11/21
 **/
public class Test {
    static class MyThread extends Thread {
        public MyThread(Runnable target) {
            super(target);
        }
    }

    public static void main(String[] args) {
        FastThreadLocal<String> stringFastThreadLocal = new FastThreadLocal<String>();
        FastThreadLocal<String> q = new FastThreadLocal<String>();
        FastThreadLocal<String> w = new FastThreadLocal<String>();
        FastThreadLocal<String> e = new FastThreadLocal<String>();

        MyFast<String> stringMyFast = new MyFast<String>();
        System.out.println("HElloWorld");

    }


    static class MyFast<E> extends FastThreadLocal<E> {
        @Override
        protected E initialValue() throws Exception {
            return super.initialValue();
        }
    }
}
