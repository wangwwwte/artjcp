package com.cobla.concurrent.test;

import java.util.concurrent.locks.Lock;

import static java.lang.Thread.sleep;

/**
 * @ClassName TwinsLockTest
 * @Author cobla
 * @Date 2018/12/25 0025 20:00
 * @Version 1.0
 * @Description TODO
 */
public class TwinsLockTest {

    public static void main(String[] args) {
        final Lock lock = new TwinsLock();
        class Worker extends Thread {
            public void run(){
                while(true){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock.unlock();
                    /*lock.lock();
                    try {
                        sleep(1000);
                        System.out.println(Thread.currentThread().getName());
                        sleep(1000);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        lock.unlock();
                    }*/
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            Worker w = new Worker();
            w.setDaemon(true);
            w.start();
        }
        for (int i = 0; i < 10; i++) {
            try {
                sleep(1000);
                System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
