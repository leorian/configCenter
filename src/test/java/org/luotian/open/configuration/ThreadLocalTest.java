package org.luotian.open.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiezg@317hu.com on 2017/4/20 0020.
 */
public class ThreadLocalTest {
    final static ThreadLocal<Map<String, String>> mapThreadLocal = new ThreadLocal<Map<String, String>>();
    public static void main(String args[]) {

        Thread thread = new Thread(new Runnable() {
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put(Thread.currentThread().getName(), Thread.currentThread().getName());
                mapThreadLocal.set(map);
                System.out.println(Thread.currentThread().getName() + " = " + mapThreadLocal.get().toString());
            }
        });
        thread.start();
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put(Thread.currentThread().getName(), Thread.currentThread().getName());
                mapThreadLocal.set(map);
                System.out.println(Thread.currentThread().getName() + " = " + mapThreadLocal.get().toString());
            }
        });
        thread1.start();
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put(Thread.currentThread().getName(), Thread.currentThread().getName());
                mapThreadLocal.set(map);
                System.out.println(Thread.currentThread().getName() + " = " + mapThreadLocal.get().toString());
            }
        });
        thread2.start();

    }
}
