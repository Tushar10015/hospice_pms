package com.hospicebangladesh.pms;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hospicebangladesh.pms.http.HttpRequest;
import com.hospicebangladesh.pms.http.HttpRequestCallBack;
import com.hospicebangladesh.pms.utils.Session;
import com.hospicebangladesh.pms.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Tushar on 2/7/2018.
 */

public class CheckPayment {

    private static final String TAG = "CheckPayment";
    public static String checkPaymentPostUrl = "check_payment.php";



    public  void  checkPayment(final Context context,final HttpRequestCallBack httpRequestCallBack) throws JSONException {

        JSONObject postBody = new JSONObject();
        String user_id=   Session.getPreference(context,Session.user_id);
        postBody.put("user_id", user_id);

        try {
            HttpRequest.postRequest(checkPaymentPostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);

                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    String status = json.getString("status");

                                    Session.savePreference(context,"status",status);
                                    Session.savePreference(context,"message",message);

                                } else {
                                    Session.savePreference(context,"status","0");
                                    Session.savePreference(context,"message",message);

                                }

                            } catch (JSONException e) {
                                Session.savePreference(context,"status","0");
                                Session.savePreference(context,"message",e.toString());


                            }
                    httpRequestCallBack.onSuccess(response);

                }

                @Override
                public void onFail() {
                    Session.savePreference(context,"status","0");
                    Session.savePreference(context,"message","Server Response Failed");
                    httpRequestCallBack.onFail();
                            Log.d(TAG, " onFail");

                }
            });
        } catch (IOException e) {
            Session.savePreference(context,"status","0");
            Session.savePreference(context,"message",e.toString());

        }




    }


}
