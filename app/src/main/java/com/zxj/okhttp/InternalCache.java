package com.zxj.okhttp;

import java.io.IOException;

public interface InternalCache {

    Response get(Request request) throws IOException;

    CacheRequest put(Response response) throws IOException;

    void remove(Request request) throws IOException;

    void update(Response cached,Response network);

    void trackConditionalCacheHit();

    void trackResponse();
}
