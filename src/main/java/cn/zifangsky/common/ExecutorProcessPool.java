package cn.zifangsky.common;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程处理类
 * 创建  createFixedThreadPool 和 createCachedThreadPool 线程池
 * ExecutorProcessPool.getInstance().executeByFixedThread( new Runnable(){ ... });
 * ExecutorProcessPool.getInstance().executeByCacheThread( new Runnable(){ ... });
 *
 * 由于 Executors工具类自定义的队列大小为Integer.MAX_VALUE, 当任务队列过多时，可能会出现OOM
 * ExecutorProcessPool.getInstance().executeByCustomThread( new Runnable(){ ... });
 */
@Slf4j
public class ExecutorProcessPool {

    

    private static ExecutorProcessPool pool = new ExecutorProcessPool();

    private ExecutorService cacheExcecutor;

    private ExecutorService fixedExcecutor;

    private ExecutorService customExcecutor;

    /**
     * 创建可缓存的线程池
     */
    private ExecutorProcessPool(){
        cacheExcecutor = ExecutorServiceFactory.getInstance().createCachedThreadPool();
        fixedExcecutor = ExecutorServiceFactory.getInstance().createFixedThreadPool(Runtime.getRuntime().availableProcessors() * 20);
        customExcecutor = ExecutorServiceFactory.getInstance().createCustomThreadPool();
        return;
    }

    /**
     * 返回线程池工具类实例
     * @return
     */
    public static ExecutorProcessPool getInstance() {
        return pool;
    }

    /**
     * 1.线程缓存空闲60后销毁
     * 3.线程数0~Integer.maxvalue
     * 2.队列SynchronousQueue 大小1
     * @param task
     */
    public void executeByCacheThread(Runnable task) {
        cacheExcecutor.execute(task);
        log("cache " , (ThreadPoolExecutor) cacheExcecutor);
    }

    /**
     * 1. 线程池大小 Runtime.getRuntime().availableProcessors() * 2
     * 2. 无界队列 Integer.maxvalue
     * @param task
     */
    public void executeByFixedThread(Runnable task) {
        fixedExcecutor.execute(task);
        log("fixed" , (ThreadPoolExecutor) fixedExcecutor);
    }

    /**
     * 1. 线程池核心数 Runtime.getRuntime().availableProcessors() + 1
     * 2. 最大线程数 Runtime.getRuntime().availableProcessors()*2 + 1
     * 3. 超过核心线程数小的线程 空闲5分钟销毁
     * 4. 有界队列，超过20*10000 , 中止接收任务
     * @param task
     */
    public void executeByCustomThread(Runnable task) {
        customExcecutor.execute(task);
        log("custom", (ThreadPoolExecutor) customExcecutor);
    }

    /**
     * 日志记录
     */
    public void log(String name, ThreadPoolExecutor tpe) {
        String status = "[Thread] "+ name +" activeCount: " + tpe.getActiveCount() + " ; CompletedTaskCount: " + tpe.getCompletedTaskCount() + " ; Queue Size: "+ tpe.getQueue().size() + "; taskCount: " + tpe.getTaskCount();
        log.debug(status );
    }

    /**
     * 停止所有任务进程
     */
    public static void shutdownAll() {
        pool.cacheExcecutor.shutdown();
        pool.fixedExcecutor.shutdown();
        pool.customExcecutor.shutdown();
    }

}