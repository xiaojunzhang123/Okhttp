package com.zxj.okhttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.RemoteException;

public class RealCall implements Call{

    private OkHttpClient client;
    private Request request;
    private boolean executed;
    public boolean forWebSocket;

    public RealCall(OkHttpClient client,Request originalRequest,boolean forWebSocket){
         this.client = client;
         this.request = originalRequest;
         this.forWebSocket = forWebSocket;
    }

    static RealCall newRealCall(OkHttpClient client,Request originalRequest,boolean forWebSocket){
        RealCall call = new RealCall(client,originalRequest,forWebSocket);
        return call;
    }

    @Override
    public Response execute() {
        synchronized (this){
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }

        try {
            //因为这个是同步请求操作，所以executed 只是把RealCall对象放入到runningSyncCalls 堆中，表示正在进行
            client.dispatcher().executed(this);
            //所有的拦截器都在这里执行，OkHttp 的核心就是拦截器，所有的操作都是以拦截器的形式（责任链模式），例如Dns解析，socket连接，tls连接，缓存，网络请求，自定义拦截器等等
            return getResponseWithInterceptorChain();
        }finally {
            //把RealCall对象从runningSyncCalls 堆中删除，并调用promoteAndExecute() ,来执行已经准备好的异步操作
            client.dispatcher().finished(this);
        }
    }

    @Override
    public void enqueue(Callback callback) {
        synchronized (this){
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }

        //用回调接口创建一个AsyncCall对象，下面介绍AsyncCall
        //enqueue 只是把该请求对象，放入readyAsyncCalls堆中，然后调用promoteAndExecute()
        client.dispatcher().enqueue(new AsyncCall(callback));
    }

    public Response getResponseWithInterceptorChain(){
        List interceptors = new ArrayList<Interceptor>();
        interceptors.addAll(client.interceptors());
        interceptors.add(new RetryAndFollowUpInterceptor());
        interceptors.add(new CacheInterceptor(client.internalCache()));
        interceptors.add(new CallServerInterceptor());

        RealInterceptorChain chain = new RealInterceptorChain(interceptors,0,request);
        return chain.proceed(request);
    }


    final class AsyncCall extends NamedRunnable {

        private Callback responseCallback;
        private volatile AtomicInteger callsPerHost = new AtomicInteger(0);

       public AsyncCall(Callback responseCallback){
              super("OkHttp ");
              this.responseCallback = responseCallback;
        }

       public AtomicInteger callsPerHost() {
           return callsPerHost;
       }

       RealCall get(){
           return RealCall.this;
       }

       void executeOn(ExecutorService executorService){
           assert (!Thread.holdsLock(client.dispatcher()));
           boolean success = false;
           try {
               // 通过线程池，来调用AsyncCall，它是继承自Runnable，真正的实现在run函数中
               //开始执行线程，进而执行execute() 方法
               executorService.execute(this);
               success = true;
           } catch (RejectedExecutionException e){
               responseCallback.onFailure(new InterruptedException());
           } finally {
               if (!success){
                   client.dispatcher().finished(RealCall.this);
               }
           }
       }

       @Override
       protected void execute() {
           try {
               Response response =  getResponseWithInterceptorChain();
               responseCallback.onResponse(response);
           } catch (Exception ioException){
               responseCallback.onFailure(new RemoteException());
           } finally {
               //从请求队列中移除请求
               client.dispatcher().finished(RealCall.this);
           }
       }
   }
}
