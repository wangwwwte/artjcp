package com.cobla.concurrent.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author cobla
 * @version 1.0
 * @className Cache
 * @date 2018/12/26 0026 20:20
 * @description 5-16读写锁使用<br/>
 * 这里吐槽，名字取得太奇怪，而且在项目中写多读少的调度器情况下r.lock()容易导致调度器挂了。<br/>
 * 一般使用trylock不停的重试，当时测试以为是线程被阻塞或者挂起，但是下面的情况却没有发生这种事情，暂时不能重现。<br/>
 * 根据visualvm的显示，当加快频率后可以明显的看到线程的执行，大部分线程都在驻留阶段，但是少部分在运行<br/>
 * 需要注意的是写线程不一定运行是互斥的，只是操作临界区是互斥的，因此可以看到偶尔写线程出现了同时启动的情况
 */
public class Cache {
    static Map<String,Object> map = new HashMap<String, Object>();
    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    static Lock r = rwl.readLock();
    static Lock w = rwl.writeLock();
    //获取一个key
    public static final Object get(String key){
        r.lock();
        try {
            return map.get(key);
        }finally {
            r.unlock();
        }
    }

    //写入
    public static final Object put(String key, Object value) {
        w.lock();
        try {
            return map.put(key, value);
        }finally {
            w.unlock();
        }
    }

    //clear All
    public static final void clear() {
        w.lock();
        try {
            map.clear();
        }finally {
            w.unlock();
        }
    }

    public static void main(String[] args) {
        //run 2 write thread
        //run 8 read thread
        //look at the thread which is suspend
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                for (; ; ) {
                    Cache.put("1",Thread.currentThread());
                    /*try {
                        sleep(100);
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/
                }
            }).start();
        }
        for (int i = 5; i < 8; i++) {
            new Thread(()->{
                for (; ; ) {
                    Cache.put("2",Thread.currentThread());
                    /*try {
                        sleep(100);
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/
                }
            }).start();
        }
        /*for (int i = 0; i < 2; i++) {
            new Thread(()->{
                for (; ; ) {
                    System.out.println(Cache.get("1"));
                    try {
                        sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }*/
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(2);
        service.scheduleWithFixedDelay(()->{
            System.out.println(Cache.get("1"));
            System.out.println(Thread.currentThread().getName());
        },0,300, TimeUnit.NANOSECONDS);
        service.scheduleWithFixedDelay(()->{
            System.out.println(Cache.get("2"));
            System.out.println(Thread.currentThread().getName());
        },0,300, TimeUnit.SECONDS);
    }

}
