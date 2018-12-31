package com.cobla.concurrent.support;

import org.junit.Test;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author cobla
 * @version 1.0
 * @className LockTest
 * @date 2018/12/31 0031 14:53
 * @description LockSupport的Demo
 */
public class LockTest {


    /**
     * @Author cobla
     * @Date 2018/12/31 0031 14:56
     * @Param []
     * @return void
     * @Description 基本使用方法，prak()和unpark(ThreadObj)<br/>
     * 一个<em>线程A</em>调用prak()进行自我阻塞，另一个线程调用unpark(A)唤醒线程A<br/>
     */
    @Test
    public void notify_Basic(){
        Thread A = new Thread(()->{
            int sum = 0;
            for (int i = 0; i < 100000; i++) {
                sum+=i;
            }
            LockSupport.park();
            System.out.println(sum);
        });
        A.start();
        System.out.println("主线程唤醒A线程");
        LockSupport.unpark(A);
        /*
         * 说明：
         *      1.被阻塞线程调用LockSupport.park()这个静态方法即可
         *      2.唤醒线程调用LockSupport.unpark(A);唤醒指定线程
         *      3.唤醒线程不需要在同步代码块中执行unpark()操作
         */
    }

    @Test
    public void notify_deadline(){
        Thread A = new Thread(()->{
            int sum = 0;
            for (int i = 0; i < 100000; i++) {
                sum+=i;
            }
            Calendar c = Calendar.getInstance();
            System.out.println(c.getTimeInMillis());
            c.add(Calendar.SECOND,4);
            LockSupport.parkUntil(c.getTimeInMillis());
            System.out.println(c.getTimeInMillis());
            System.out.println(sum);
        });
        A.start();
//        LockSupport.unpark(A);
        try {
            A.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*
         * 说明：
         *      1546240815175
         *      1546240819175
         *      704982704
         *      当没有调用unpark时，线程等待到deadline时间自动返回
         */
    }

    @Test
    public void notify_timeout(){
        Thread A = new Thread(()->{
            int sum = 0;
            for (int i = 0; i < 100000; i++) {
                sum+=i;
            }
            long nanos = TimeUnit.SECONDS.toNanos(3);
            System.out.println(10L^9L);
            System.out.println(Integer.MAX_VALUE);
            LockSupport.parkNanos(3000000000L);
            System.out.println(nanos);
            System.out.println(sum);
        });
        A.start();
//        LockSupport.unpark(A);
        try {
            A.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*
         * 说明：
         *      阻塞，带超时返回，需要注意的是最好使用TimeUnit提供的方法生成nanos时间
         *      理由：第一，不容易算错位数，第二，如果是表达式生成的时间，存在不准确的问题，原因看上面的表达式输出的内容
         *      计算会出错
         */
    }

}
