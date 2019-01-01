package com.cobla.concurrent.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author cobla
 * @version 1.0
 * @className CountTask
 * @date 2018/12/31 0031 20:36
 * @description 6.4.4使用Fork/Join框架，计算1+2+3+4<br/>
 * 对于使用Fork/Join框架计算，需要先了解两个东西<br/>
 * 1.需要定义任务分割的阈值<br/>
 * 2.因为是有结果的任务，所以需要继承RecursiveTask
 */
public class CountTask extends RecursiveTask<Integer> {

    //阈值
    private static final int THRESHOLD = 100;

    private int start;
    private int end;

    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    //执行具体的计算逻辑
    @Override
    protected Integer compute() {
        //首先进行判断，任务是否足够小，可以由当前的compute进行计算得到
        int sum = 0;
        boolean cancompulate = (end - start) <= THRESHOLD;
        if (cancompulate) {
            //进行具体的计算，这里因为是累加，所以是这样直接累加循环变量i
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            //根据具体情况，选择合适的切分方法
            //切分的值可以根据阈值来，也可以一分为2
            int middle = (end + start) / 2;
            CountTask lefttask = new CountTask(start, middle);
            CountTask righttask = new CountTask(middle + 1, end);
            //执行任务，使用fork()
            lefttask.fork();
            righttask.fork();
            //等待子任务完成，并接收结果
            int leftresult = lefttask.join();
            int rightresult = righttask.join();

            //合并子任务结果
            sum = leftresult+rightresult;
        }
        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //生成一个计算任务
        CountTask task = new CountTask(1, 10000);
        //执行任务
        ForkJoinTask<Integer> result = forkJoinPool.submit(task);

        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //检查是否fork中是否已经抛出异常或者被取消
        if (task.isCompletedNormally()) {
            // getException返回Throwable对象

            // 如果任务被取消则返回CancellationException
            // 如果任务没有完成或者没有抛出异常则返回null
            task.getException().printStackTrace();
        }
    }
}
