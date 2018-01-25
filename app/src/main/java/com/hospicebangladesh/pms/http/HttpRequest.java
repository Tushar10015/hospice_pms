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

    public static void postRequest(String postUrl, String postBody,final HttpRequestCallBack httpRequestCallBack) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
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


    public static  void getRequest(String getUrl) throws IOException {

        OkHttpClient client = new OkHttpClient();

       /* HttpUrl.Builder urlBuilder = HttpUrl.parse("https://httpbin.org/get").newBuilder();
                urlBuilder.addQueryParameter("website", "www.journaldev.com");
        urlBuilder.addQueryParameter("tutorials", "android");
        String geturl = urlBuilder.build().toString();*/


        Request request = new Request.Builder()
                .url(getUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                try {

                    JSONObject json = new JSONObject(myResponse);
                    //   txtString.setText("First Name: "+json.getJSONObject("data").getString("first_name") + "\nLast Name: " + json.getJSONObject("data").getString("last_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            /*    SignupActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(myResponse);
                            //   txtString.setText("First Name: "+json.getJSONObject("data").getString("first_name") + "\nLast Name: " + json.getJSONObject("data").getString("last_name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });*/

            }
        });
    }


}