package com.zxj.okhttp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.zxj.okhttp.RealCall.AsyncCall;

public class Dispatcher {

    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;
    private Runnable idleCallback;

    private ExecutorService executorService;

    private final Deque<AsyncCall> readAsyncCalls = new ArrayDeque<>();

    private final Deque<AsyncCall> runningAsyncCalls = new ArrayDeque<>();

    private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>();

    public Dispatcher(){

    }

    public Dispatcher(ExecutorService executorService){
        this.executorService = executorService;
    }

    public synchronized void executed(RealCall call){
        runningSyncCalls.add(call);
    }

    void finished(RealCall call){
        finished(runningSyncCalls,call);
    }

    private <T> void finished(Deque<T> calls,T call){
        Runnable idleCallback;
        synchronized (this){
            if (!calls.remove(call)){
                throw new AssertionError("Call wasn`t in-flight!");
            }
            idleCallback = this.idleCallback;
        }

        //当请求结束了，将当前的任务从正在执行的任务队列中移除
        boolean isRunning = promoteAndExecute();


        if (!isRunning && idleCallback != null){
            idleCallback.run();
        }
    }

    public synchronized ExecutorService executorService(){
        if (executorService == null){
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60,
                TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    return thread;
                }
            });
        }
        return executorService;

    }

    private boolean promoteAndExecute(){
        assert (!Thread.holdsLock(this));

        //正在执行的任务
        List<AsyncCall> executableCalls = new ArrayList<>();
        boolean isRunning;
        synchronized (this){
            for (Iterator<AsyncCall> i = readAsyncCalls.iterator();i.hasNext();){
                AsyncCall asyncCall = i.next();

                //如果当前请求的异步任务队列大于等于64 , 那么就停止执行任务
                if (runningAsyncCalls.size() >= maxRequests) break;
                // 如果当前任务所请求的主机大于等于5 ，那么就暂时不执行该任务
                if (asyncCall.callsPerHost().get() >= maxRequestsPerHost) continue;

                i.remove();
                asyncCall.callsPerHost().incrementAndGet();
                executableCalls.add(asyncCall);
                runningAsyncCalls.add(asyncCall);
            }
            isRunning = runningAsyncCalls.size() > 0;
        }

        int size = executableCalls.size();
        for (int i=0; i < size; i++){
            AsyncCall asyncCall = executableCalls.get(i);
            asyncCall.executeOn(executorService());
        }
        return isRunning;
    }

    void enqueue(AsyncCall call){
      synchronized (this){
          readAsyncCalls.add(call);

          if (!call.get().forWebSocket){
              //判断是否请求网络
          }
          //处理异步请求
          promoteAndExecute();
      }
    }
}
