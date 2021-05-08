package com.zxj.okhttp;

import java.util.Map;

public class CacheMgr {

    private Map<String , Response> cache;

    public Map<String, Response> getCache() {
        return cache;
    }

    public void setCache(Map<String, Response> cache) {
        this.cache = cache;
    }
}
