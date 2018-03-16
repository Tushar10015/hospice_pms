package com.hospicebangladesh.rpms.http;

import java.io.IOException;

import okhttp3.Response;

public interface HttpRequestCallBack {

    void onSuccess(Response response) throws IOException;
    void onFail();
}