package com.cobla.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author cobla
 * @version 1.0
 * @className AtomicReferenceTest
 * @date 2019/1/1 0001 10:48
 * @description 原子更新引用
 */
public class AtomicReferenceTest {
    public static AtomicReference<User> atomicReference = new AtomicReference<>();

    public static void main(String[] args) {
        //柯南10岁
        User conan = new User("conan",10);
        atomicReference.set(conan);
        //变成新一，18岁
        User shinichi = new User("shinici", 18);
        atomicReference.compareAndSet(conan, shinichi);
        System.out.println(atomicReference.get().getName());
        System.out.println(atomicReference.get().getOld());
    }

    static class User{
        private String name;
        private int old;

        public User(String name, int old) {
            this.name = name;
            this.old = old;
        }

        public String getName() {
            return name;
        }

        public int getOld() {
            return old;
        }
    }
}
