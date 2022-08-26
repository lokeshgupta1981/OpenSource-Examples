package com.howtodoinjava.demo.webscrapper.executor;

import com.howtodoinjava.demo.webscrapper.UrlRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class JobSubmitter {
    public static List<Future<?>> futures = new ArrayList<Future<?>>();
    static BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>(25000);
    static BlockingThreadPoolExecutor executor = new BlockingThreadPoolExecutor(5,10, 10000, TimeUnit.MILLISECONDS, blockingQueue);
    static {
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler()
        {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
            {
                System.out.println("GstTask Rejected : " + ((FutureTask) r));
                try
                {
                    Thread.sleep(10000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                System.out.println("Lets add another time : " + ((FutureTask) r));
                Future<?> f = executor.submit(r);
                futures.add(f);
            }
        });
        // Let start all core threads initially
        executor.prestartAllCoreThreads();
    }

    public static void submitTask(UrlRecord entity) {
        Future<?> f = executor.submit(new ScrapTask(entity));
        futures.add(f);
    }
 }
