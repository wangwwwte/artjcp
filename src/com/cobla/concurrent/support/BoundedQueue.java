package com.cobla.concurrent.support;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cobla
 * @version 1.0
 * @className BoundedQueue
 * @date 2018/12/31 0031 16:19
 * @description 5-21 ndiction的使用案例，通过一个有界队列
 */
public class BoundedQueue<T> {
    private Object[] items;
    //插入下标，删除下标，数组当前元素个数
    private int addIndex, removeIndex, count;
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public BoundedQueue(int size) {
        items = new Object[size];
    }

    public void add(T t) throws InterruptedException {
        lock.lock();
        try {
            //满等，等待者在notFull上，等待不满条件成立
            while (count == items.length) {
                System.out.println("空啦");
                notFull.await();
            }
            items[addIndex] = t;
            //循环队列
            if (++addIndex == items.length) {
                addIndex = 0;
            }
            //这才是计数
            ++count;
            //来一个就可以唤醒消费者了
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }

    public T remove() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0){
                System.out.println("空啦");
                notEmpty.await();
            }
            T t = (T) items[removeIndex++];
            if (removeIndex == items.length) {
                removeIndex = 0;
            }
            --count;
            notFull.signal();
            return t;
        }finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        final BoundedQueue<Integer> queue = new BoundedQueue<>(2);
        final Random ran = new Random();
        new Thread(()->{
            for (; ; ) {

                try {
                    queue.add(ran.nextInt());
                    TimeUnit.SECONDS.sleep(ran.nextInt(4));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
            for (; ; ) {

                try {
                    System.out.println(queue.remove());
                    TimeUnit.SECONDS.sleep(ran.nextInt(3));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
    分析：由于这里的睡眠时间不固定，因此可以发现，add线程睡得时间久一点的时候，remove线程的输出也会变的比较久。
         如果还希望看到更详细的过程，则可以在await()上方打印内容看看。
     */
}
