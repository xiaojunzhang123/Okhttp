package com.zxj.okhttp;

import java.io.IOException;

public interface CacheRequest {

    Sink body() throws IOException;

    void abort();

}
