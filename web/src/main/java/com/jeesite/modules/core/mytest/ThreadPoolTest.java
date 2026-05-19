package com.jeesite.modules.core.mytest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;

public class ThreadPoolTest {
    public static void main(String[] args) {
        // 你原来的线程池（不变）
        ExecutorService pool = new ThreadPoolExecutor(
                5,                  // 核心线程 IO密集型2N_CPU或者更大,CPU密集型N_CPU数+1,防止某个线程因为缺页中断或GC被暂停时，CPU空闲，多一个线程可以补上
                8,                  // 最大线程 CPU*2，当队列满了，且线程数<maxnumPoolSize时创建新线
                1L,                 // 空闲时间
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),  //ArrayBlockingQueue有界队列防止OOM, 2~5*核心线程，核心线程数=5，单个任务处理时间=100ms,允许挤压5秒，队列大小=5*0.1*(5/0.1)=25
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略 AbortPolicy直接抛出异常适合需要快速失败的场景，CallerRunsPolicy调用者运行策略（最安全），DiscardPolicy不处理直接丢弃，DiscardOldestPolicy丢弃最老的请求
        );

        // ==============================================
        // 在这里循环提交任务，就能启动多个线程！
        // ==============================================
        for (int i = 1; i <= 10; i++) { // 提交 10 个任务
            int taskNum = i;
            pool.execute(() -> {
                System.out.println("任务" + taskNum + " 正在执行，线程名：" + Thread.currentThread().getName());
            });
        }
    }
}