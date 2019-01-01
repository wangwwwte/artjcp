package com.cobla.concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author cobla
 * @version 1.0
 * @className AtomicIntegerArrayTest
 * @date 2019/1/1 0001 10:40
 * @description
 */
public class AtomicIntegerArrayTest {
    static int[] value = new int[]{1,2,3};
    static AtomicIntegerArray a = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        System.out.println(a.getAndSet(1, 6));
        System.out.println(a.incrementAndGet(0));
        a.lazySet(0,6);
        System.out.println(a.get(0));
        System.out.println(a.getAndIncrement(2));
        //注意，atomic是copy一份的，所以原数组不会变
        System.out.println(value[0]);
    }

}
