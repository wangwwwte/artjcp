package com.cobla.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cobla
 * @version 1.0
 * @className AtomicIntegerTest
 * @date 2019/1/1 0001 10:29
 * @description 原子操作类，基本类型
 */
public class AtomicIntegerTest {
    static AtomicInteger a = new AtomicInteger(1);

    public static void main(String[] args) {
        //返回旧值，并且自增
        System.out.println(a.getAndIncrement());
        //返回新值，并且自增
        System.out.println(a.incrementAndGet());
        //返回当前值
        System.out.println(a.get());
        System.out.println(a.getAndAdd(4));
        System.out.println(a.get());
    }
}
