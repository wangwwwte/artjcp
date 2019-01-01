package com.cobla.concurrent.delay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author cobla
 * @version 1.0
 * @className DelayTask
 * @date 2018/12/31 0031 17:47
 * @description
 */
public class DelayTask implements Runnable {

    private int id;
    private DelayQueue<DelayEvent> queue;

    public DelayTask(int id, DelayQueue<DelayEvent> queue) {
        super();
        this.id = id;
        this.queue = queue;
    }

    public static final Random random = new Random();

    /**
     * @Author cobla
     * @Date 2018/12/31 0031 17:50
     * @Param []
     * @return void
     * @Description 这里只是为了创建id * 1000 + now date 的deadline时间的100个DelayEvent而已
     */
    @Override
    public void run() {
        Date deadline = new Date();
//        deadline.setTime(deadline.getTime() + id * 1000);
//        deadline.setTime(deadline.getTime() + 1000+ random.nextInt(3000));
        System.out.println("Thread "+ id+" "+deadline);
        for (int i = 0; i < 100; i++) {
            Date dead = new Date();
            dead.setTime(deadline.getTime() + 1000+ random.nextInt(3)*1000);
            DelayEvent event = new DelayEvent(dead);
            queue.add(event);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        DelayQueue<DelayEvent> queue = new DelayQueue<>();
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DelayTask task = new DelayTask(i + 1, queue);
            threadList.add(new Thread(task));
        }
        for (Thread thread : threadList) {
            thread.start();
        }
        for (int i = 0; i < 5; i++) {
            try {
                threadList.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        do {
            //统计当前到期的元素个数
            int count = 0;
            DelayEvent e;
            do {
                e = queue.poll();
                if (e != null) {
                    ++count;
                }
            } while (e != null);
            System.out.println("At " + new Date() + " you have read " + count + " event");
            TimeUnit.MILLISECONDS.sleep(500);
        } while (queue.size() > 0);

    }
}
