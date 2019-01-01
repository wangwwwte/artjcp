package com.cobla.concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author cobla
 * @version 1.0
 * @className AtomicIntegerFieldUpdaterTest
 * @date 2019/1/1 0001 11:05
 * @description 原子更新字段，比较麻烦<br/>
 * 1.需要调用newUpdater()<br/>
 * 2.需要属性是<em>public volatile</em>修饰的
 */
public class AtomicIntegerFieldUpdaterTest {
    //原子更新器，设置需要更新的对象类和对象的属性
    private static AtomicIntegerFieldUpdater<User> a = AtomicIntegerFieldUpdater.newUpdater(User.class, "old");

    public static void main(String[] args) {
        //柯南10岁
        User conan = new User("conan",10);
        //增长了1岁，但是返回的仍然是10
        System.out.println(a.getAndIncrement(conan));
        //得到现在柯南的年龄
        System.out.println(a.get(conan));
    }

    public static class User{
        private String name;
        public volatile int old;

        public User(String name, int old) {
            this.name = name;
            this.old = old;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
