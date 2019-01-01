package com.cobla.concurrent.queue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author cobla
 * @version 1.0
 * @className QueueTest
 * @date 2018/12/31 0031 17:00
 * @description 队列测试
 */
public class QueueTest {
    public static void main(String[] args) {
        ArrayBlockingQueue<Integer> intqueue = new ArrayBlockingQueue<Integer>(20);

        intqueue.remove();
    }
}
