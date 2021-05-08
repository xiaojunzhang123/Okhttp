package com.zxj.okhttp;

public class RetryAndFollowUpInterceptor implements Interceptor {

    private int retryCount = 0;

    @Override
    public Response intercept(Chain chain) {
        Request request = chain.request();
        while (true) {
            try {
                return chain.proceed(request);
            } catch (Exception e) {
                retryCount++;
            }

            if (retryCount > 3) {
                Response response = new Response();
                response.setResult("error");
                response.setUrl(chain.request().getUrl());
                return response;
            }
        }
    }
}
