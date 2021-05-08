package com.zxj.okhttp;

public class Request {

    private String url;
    private String header;

    public String getUrl() {
        return url;
    }

    public String getHeader() {
        return header;
    }

    public Request(){
        this(new Builder());
    }

    public Request(Builder builder){
        this.url = builder.url;
        this.header = builder.header;
    }

    public static class Builder {

        private String url;
        private String header;

        public Builder(){
            url = "";
            header = "";
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder header(String header){
            this.header = header;
            return this;
        }

        public Request build(){
            return new Request(this);
        }

    }
}
