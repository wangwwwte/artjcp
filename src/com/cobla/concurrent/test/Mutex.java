package com.cobla.concurrent.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName Mutex
 * @Author cobla
 * @Date 2018/12/23 0023 22:05
 * @Version 1.0
 * @Description 代码清单5-2<br/>
 * <strong>独占锁</strong>实现示例<br/>
 * 独占锁同一时刻只能有一个线程访问，当前获取持锁线程释放了锁之后，后继线程才能尝试获取锁
 */
public class Mutex implements Lock {
    /**
     * @ClassName Sync
     * @Author cobla
     * @Date 2018/12/23 0023 22:05
     * @Version 1.0
     * @Description 同步组件Mutex的同步器，静态内部类实现方式<br/>
     * 主要用途是提供具体的资源策略，而Mutex则提供统一而简单的对外访问接口
     */
    public static class Sync extends AbstractQueuedSynchronizer {
        /**
         * @Author cobla
         * @Date 2018/12/23 0023 22:18
         * @Param [acquires]
         * @return boolean
         * @Description 多个线程可能并发访问该接口，所以需要cas操作state值<br/>
         * setExclusiveOwnerThread(thread)意味着thread成为了锁的持有者<br/>
         * 因此setExclusiveOwnerThread才是实际上的持锁操作
         */
        @Override
        protected boolean tryAcquire(int acquires) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                System.out.printf("%s 持锁成功\n",Thread.currentThread().getName());
                return true;
            }
            return false;
        }
        /**
         * @Author cobla
         * @Date 2018/12/23 0023 22:40
         * @Param [arg]
         * @return boolean
         * @Description 因为Mutex是互斥锁，在同一时刻最多只有一个线程持锁，所以不需要cas操作
         */
        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            System.out.printf("%s 解锁成功\n",Thread.currentThread().getName());
            setState(0);
            return true;
        }
        /**
         * @Author cobla
         * @Date 2018/12/23 0023 22:15
         * @Param []
         * @return boolean
         * @Description 是否处于独占状态<br/>
         * 是意味着当前有一个线程持锁，不是意味着没有线程持锁
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        /**
         * @Author cobla
         * @Date 2018/12/23 0023 22:44
         * @Param []
         * @return java.util.concurrent.locks.Condition
         * @Description 返回一个Condition，我也不知道是做什么用的
         */
        Condition newcondition(){return new ConditionObject();}

    }

    private final Sync sync = new Sync();
    @Override
    public void lock() {
        sync.tryAcquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.tryRelease(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newcondition();
    }

    public static void main(String[] args) {
        final Mutex mutex = new Mutex();
        List<Thread> threadList = new ArrayList<>(10);
        for (int i = 0; i < 3; i++) {
            threadList.add(new Thread(() ->{
                while (true) {
                    System.out.printf("%s 持锁前\n",Thread.currentThread().getName());
                    mutex.lock();
                    System.out.printf("%s 持锁后\n",Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        System.out.printf("%s 解锁前\n",Thread.currentThread().getName());
                        mutex.unlock();
                        System.out.printf("%s 解锁后\n",Thread.currentThread().getName());
                    }
                }
            }));
        }
        for (Thread thread : threadList) {
            thread.start();
        }
    }
    
}
