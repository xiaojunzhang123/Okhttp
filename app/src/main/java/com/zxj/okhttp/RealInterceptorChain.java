package com.zxj.okhttp;

import java.util.List;

public class RealInterceptorChain implements Interceptor.Chain{

    private List<Interceptor> interceptors;
    private int index;
    private Request request;

    public RealInterceptorChain(List<Interceptor> interceptors, int index,
                                Request request) {
        this.interceptors = interceptors;
        this.index = index;
        this.request = request;
    }

    @Override
    public Response proceed(Request request) {
        //责任链模式开启的起点
        RealInterceptorChain next = new RealInterceptorChain(interceptors,++index,request);
        Interceptor interceptor = interceptors.get(index);
        return interceptor.intercept(next);
    }

    @Override
    public Request request() {
        return request;
    }
}
