package com.zxj.okhttp;

public class CallServerInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) {
        Response response = new Response();
        response.setBody("body");
        response.setHeader(chain.request().getHeader());
        response.setResult("ok");
        response.setUrl(chain.request().getUrl());
        return response;
    }
}
