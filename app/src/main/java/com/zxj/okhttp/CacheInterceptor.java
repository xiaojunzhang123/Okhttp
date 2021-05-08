package com.zxj.okhttp;

import java.io.IOException;

import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;

public class CacheInterceptor implements Interceptor{

    public CacheMgr cacheMgr;
    public Cache cache;

    public CacheInterceptor(CacheMgr cacheMgr) {
        this.cacheMgr = cacheMgr;
    }

    @Override
    @RequiresApi(api = VERSION_CODES.O)
    public Response intercept(Chain chain) {
        try {
            if (cache.internalCache.get(chain.request())!=null){
                return cache.internalCache.get(chain.request());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Response response = cacheMgr.getCache().get(chain.request().getUrl());
        if (response == null){
            response = chain.proceed(chain.request());
            cacheMgr.getCache().put(chain.request().getUrl(),response);
        }else {
            response.setCache(true);
        }
        return response;
    }
}
