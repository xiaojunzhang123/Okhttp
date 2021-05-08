package com.zxj.okhttp;

public interface Callback {

    void onResponse(Response response);

    void onFailure(Exception exception);
}
