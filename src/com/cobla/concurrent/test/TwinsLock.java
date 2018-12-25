package com.cobla.concurrent.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName TwinsLock
 * @Author cobla
 * @Date 2018/12/24 0024 23:27
 * @Version 1.0
 * @Description 资源数只有两个，共享式访问
 */
public class TwinsLock implements Lock {

    private final Sync sync = new Sync(2);

    private static final class Sync extends AbstractQueuedSynchronizer{
        public Sync(int state) {
            if(state < 0)
                throw new IllegalArgumentException("state must large zero.");
            setState(state);
        }

        @Override
        protected int tryAcquireShared(int reduceCount) {
            for (;;){
                int oldCount = getState();
                int newCount = oldCount - reduceCount;
                if(newCount < 0 || compareAndSetState(oldCount,newCount))
                    return newCount;
            }
        }

        @Override
        protected boolean tryReleaseShared(int addCount) {
            System.out.println(getState());
            for (;;){
                int oldCount = getState();
                int newCount = oldCount + addCount;
                if(compareAndSetState(oldCount, newCount))
                    return true;
            }
        }
    }
    @Override
    public void lock() {
        sync.tryAcquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.tryAcquireShared(1);
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        sync.tryReleaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

}
