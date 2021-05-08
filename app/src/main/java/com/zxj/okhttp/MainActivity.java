package com.zxj.okhttp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url("www.baidu.com").build();

        Call call = okHttpClient.newCall(request);
        //call.enqueue(new Callback() {
        //    @Override
        //    public void onResponse(Response response) {
        //
        //    }
        //
        //    @Override
        //    public void onFailure(Exception exception) {
        //
        //    }
        //});

        call.execute();
    }
}