package com.hospicebangladesh.pms.http;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequest {

    private static final String TAG = "HttpRequest";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String BASE_URL = "http://103.16.75.42/pms/api/";

    public static void postRequest(String postUrl, String postBody, final HttpRequestCallBack httpRequestCallBack) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(BASE_URL+postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                httpRequestCallBack.onFail();
                Log.d(TAG, " onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, " onResponse");
                httpRequestCallBack.onSuccess(response);

            }
        });
    }


    public static void getRequest(String getUrl, final HttpRequestCallBack httpRequestCallBack) throws IOException {

        OkHttpClient client = new OkHttpClient();

       /* HttpUrl.Builder urlBuilder = HttpUrl.parse("https://httpbin.org/get").newBuilder();
                urlBuilder.addQueryParameter("website", "www.journaldev.com");
        urlBuilder.addQueryParameter("tutorials", "android");
        String geturl = urlBuilder.build().toString();*/


        Request request = new Request.Builder()
                .url(BASE_URL+getUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                httpRequestCallBack.onFail();
                Log.d(TAG, " onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                Log.d(TAG, " onResponse");
                httpRequestCallBack.onSuccess(response);


            }
        });
    }


}