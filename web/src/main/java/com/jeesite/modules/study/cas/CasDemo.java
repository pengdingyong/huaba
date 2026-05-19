package com.jeesite.modules.study.cas;
import java.util.concurrent.atomic.AtomicInteger;

public class CasDemo {
    // 原子类底层就是 CAS
    private static final AtomicInteger count = new AtomicInteger(0);

    public static void increment() {
        // 自旋 + CAS
        int oldValue;
        int newValue;
        do {
            // 1. 获取当前值
            oldValue = count.get();
            System.out.println(Thread.currentThread().getName()+"=oldValue="+oldValue);
            // 2. 计算新值
            newValue = oldValue + 1;
            // 3. CAS 比较并替换：旧值预期=当前值才更新
            System.out.println(Thread.currentThread().getName()+"=newValue="+newValue);
        } while (!count.compareAndSet(oldValue, newValue));
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(CasDemo::increment);
        Thread t2 = new Thread(CasDemo::increment);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(count.get()); // 结果一定是 2
    }
}
