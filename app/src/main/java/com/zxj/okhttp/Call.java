package com.zxj.okhttp;


public interface Call {

     //同步请求
      Response execute();

      //异步请求
      void enqueue(Callback callback);

}
