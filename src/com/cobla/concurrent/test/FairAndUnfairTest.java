package com.cobla.concurrent.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cobla
 * @version 1.0
 * @className FairAndUnfairTest
 * @date 2018/12/25 0025 22:29
 * @description
 */
public class FairAndUnfairTest {
    private static ReentrantLock2 fairLock = new ReentrantLock2(true);
    private static ReentrantLock2 unfairLock = new ReentrantLock2(false);
    private static CountDownLatch count = new CountDownLatch(5);

    @Test
    public void fair(){
        testLock(fairLock);
    }


    @Test
    public void unfair(){
        testLock(unfairLock);
    }


    private void testLock(ReentrantLock2 lock) {
        for (int i = 0; i < 5; i++) {
            Job job = new Job(lock);
            job.setDaemon(true);
            System.out.println("start");
            job.start();
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Job job = new Job(fairLock);
        job.start();

    }

    private static class Job extends Thread{
        private ReentrantLock2 lock;

        public Job(ReentrantLock2 lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            int i = 10;
            while(i-- >= 0){
                lock.lock();
                System.out.println(Thread.currentThread().getName());
                System.out.println(this.lock.getQueuedThreads());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    private static class ReentrantLock2 extends ReentrantLock{
        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        /**
         * @Author cobla
         * @Date 2018/12/25 0025 22:32
         * @Param []
         * @return java.util.Collection<java.lang.Thread>
         * @Description 主要是为了逆转排序方式
         */
        @Override
        public Collection<Thread> getQueuedThreads() {
            List<Thread> threads = new ArrayList<>(super.getQueuedThreads());
            Collections.reverse(threads);
            return threads;
        }
    }
}
