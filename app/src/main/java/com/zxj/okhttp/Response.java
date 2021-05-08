package com.zxj.okhttp;

public class Response {

    private boolean isCache = false;
    private String url;
    private String header;
    private String body;
    private String result;

    public Response() {
    }

    public Response(boolean isCache, String url, String header, String body, String result) {
        this.isCache = isCache;
        this.url = url;
        this.header = header;
        this.body = body;
        this.result = result;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
