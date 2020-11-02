package io.netty.example.echo;

import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author chen
 * @date 2020/10/14
 **/
public class Main {
    public static void main(String[] args) {
        GlobalEventExecutor.INSTANCE.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i < 10;i++){
                    System.out.println("当前的i为:"+i);
                }
            }
        });
    }
}
