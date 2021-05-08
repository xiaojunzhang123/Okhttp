package com.zxj.okhttp;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.Base64;

import android.graphics.Bitmap;
import android.os.Build.VERSION_CODES;
import android.util.LruCache;
import androidx.annotation.RequiresApi;
import com.jakewharton.disklrucache.DiskLruCache;

public class Cache implements Closeable, Flushable {

    private static final int VERSION = 201105;
    private static final int ENTRY_METADATA = 0;
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;

    final InternalCache internalCache = new InternalCache() {
        @Override
        @RequiresApi(api = VERSION_CODES.O)
        public Response get(Request request) throws IOException {
            return Cache.this.get(request);
        }

        @Override
        public CacheRequest put(Response response) throws IOException {
            return Cache.this.put(response);
        }

        @Override
        public void remove(Request request) throws IOException {
            Cache.this.remove(request);
        }

        @Override
        public void update(Response cached, Response network) {
             Cache.this.update(cached,network);
        }

        @Override
        public void trackConditionalCacheHit() {

        }

        @Override
        public void trackResponse() {

        }
    };

     DiskLruCache cache;

    public Cache(File directory, long maxSize) {
    }

    Cache(File directory, long maxSize, FileSystem fileSystem) {
        try {
            this.cache = DiskLruCache.open(directory, VERSION, ENTRY_COUNT, maxSize);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void update(Response cached, Response network) {

    }

    private void remove(Request request) {

    }

    private CacheRequest put(Response response) {
        return null;
    }

    @RequiresApi(api = VERSION_CODES.O)
    private Response get(Request request) {
        String key = key(request.getUrl());
        return null;
    }

    @RequiresApi(api = VERSION_CODES.O)
    public static String key(String requestUrl){
        return Base64.getDecoder().decode(requestUrl).toString();
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }
}
