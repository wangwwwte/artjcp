package com.cobla.concurrent.test;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author cobla
 * @version 1.0
 * @className ProcessData
 * @date 2018/12/26 0026 22:06
 * @description 5-19锁降级的案例，测不明白锁降级能测试些什么，反正能降级，没有报什么奇奇怪怪的错误
 */
public class ProcessData {
    private volatile boolean update=false;
    public static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public static int data = 0;
    public void processData(){
        for (; ; ) {

            lock.readLock().lock();
            if(!update){//数据未更新，则开启写锁，更新数据
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    if (!update) {
                        Random random = new Random();
                        data = random.nextInt();
                        System.out.println("更新了数据"+data);
                        update = true;
                    }
                    lock.readLock().lock();
                }finally {
                    lock.writeLock().unlock();
                }
            }
            try {
                System.out.println("读取了数据"+data);
                update=false;
            }finally {
                lock.readLock().unlock();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            new Thread(()->{
                ProcessData data = new ProcessData();
                data.processData();
            }).start();

        }
    }
}
