package com.zxj.okhttp;

import java.util.List;

public class OkHttpClient {

    private Dispatcher dispatcher;
    private OkHttp okHttp;


    public OkHttpClient(){
       this(new Builder());
    }

     OkHttpClient(Builder builder){
        this.dispatcher = builder.dispatcher;
        this.okHttp = builder.okHttp;
    }

    public Call newCall(Request request){
        return RealCall.newRealCall(this,request,false);
    }

    public Dispatcher dispatcher(){
       return okHttp.getDispatcher();
    }

    public List<Interceptor> interceptors(){
        return okHttp.getInterceptors();
    }

    public CacheMgr internalCache(){
        return okHttp.getCacheMgr();
    }

    public void addInterceptor(Interceptor interceptor){
        okHttp.getInterceptors().add(interceptor);
    }


    public static class Builder {

        private OkHttp okHttp;
        Dispatcher dispatcher;

        public Builder(){
            this.dispatcher = new Dispatcher();
            this.okHttp = new OkHttp();
        }

        public OkHttpClient build(){
            return new OkHttpClient(this);
        }
    }
}
