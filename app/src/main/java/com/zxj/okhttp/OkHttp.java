package com.zxj.okhttp;

import java.util.ArrayList;
import java.util.List;

public class OkHttp {

    private Dispatcher dispatcher = new Dispatcher();
    private List<Interceptor> interceptors = new ArrayList<>();
    private CacheMgr cacheMgr = new CacheMgr();

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public CacheMgr getCacheMgr() {
        return cacheMgr;
    }

    public void setCacheMgr(CacheMgr cacheMgr) {
        this.cacheMgr = cacheMgr;
    }
}
