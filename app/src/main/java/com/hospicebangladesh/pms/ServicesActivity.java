package com.hospicebangladesh.pms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hospicebangladesh.pms.adapter.MedicineAdapter;
import com.hospicebangladesh.pms.http.HttpRequest;
import com.hospicebangladesh.pms.http.HttpRequestCallBack;
import com.hospicebangladesh.pms.model.Medicine;
import com.hospicebangladesh.pms.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class ServicesActivity extends AppCompatActivity {

    private static final String TAG = "ServicesActivity";
    public String serviceUpdatePostUrl = "http://2aitbd.com/pms/api/service.php";
    public String showServiceGetUrl = "http://2aitbd.com/pms/api/get_services.php";
    HashMap<String,Integer> spinnerMap;

    @Bind(R.id.input_mobile)
    EditText _mobileText;

    @Bind(R.id.input_address)
    EditText _addressText;

    @Bind(R.id.input_note)
    EditText _noteText;

    @Bind(R.id.input_services)
    Spinner _servicesSpinner;
  
    @Bind(R.id.btn_service)
    Button _serviceButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        initializeSpinner();


        _serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    service();
                } catch (JSONException e) {

                }
            }
        });


    }


    private void initializeSpinner() {

        final ProgressDialog progressDialog = new ProgressDialog(ServicesActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        try {

            HttpRequest.getRequest(showServiceGetUrl, new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    ServicesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    JSONArray jsonArrayServices = json.getJSONArray("services");

                                    List<String> serviceList = new ArrayList<>();
                                     spinnerMap = new HashMap<String,Integer>();
                                    for (int i = 0; i < jsonArrayServices.length(); i++) {
                                        JSONObject objPrescriptions = jsonArrayServices.getJSONObject(i);
                                        int servid = objPrescriptions.getInt("servid");
                                        String service = objPrescriptions.getString("service");
                                        serviceList.add(service);
                                        spinnerMap.put(service,servid);
//                                        spinnerMap.get(service);
                                    }


                                    ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(getApplicationContext(),
                                            android.R.layout.simple_spinner_item, serviceList);
                                    _servicesSpinner.setAdapter(adapterGender);
                                    _servicesSpinner.setSelection(getIntent().getExtras().getInt("index"));
                                    _servicesSpinner.setEnabled(false);
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onFail() {
                    ServicesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server is not responding properly", Toast.LENGTH_LONG).show();
                            Log.d(TAG, " onFail");
                            progressDialog.dismiss();

                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }






    public void service() throws JSONException {
        Log.d(TAG, "service");

        if (!validate()) {
            onSignupFailed("Validation Failed");
            return;
        }

        _serviceButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ServicesActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Account...");
        progressDialog.show();


        String mobile = _mobileText.getText().toString();
        String service = _servicesSpinner.getSelectedItem().toString();
        int service_id= spinnerMap.get(service);
        String address = _addressText.getText().toString();
        String note = _noteText.getText().toString();


        JSONObject postBody = new JSONObject();

        String user_id=   Session.getPreference(getApplicationContext(),Session.user_id);

        postBody.put("user_id", user_id);
        postBody.put("mobile", mobile);
        postBody.put("service_id", service_id);
        postBody.put("address", address);
        postBody.put("note", note);


        try {
            HttpRequest.postRequest(serviceUpdatePostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    ServicesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    onSignupSuccess(message);
                                    progressDialog.dismiss();
                                } else {
                                    onSignupFailed(message);
                                    progressDialog.dismiss();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onFail() {
                    ServicesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server is not responding properly", Toast.LENGTH_LONG).show();
                            _serviceButton.setEnabled(true);
                            progressDialog.dismiss();
                            Log.d(TAG, " onFail");
                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void onSignupSuccess(String message) {
        _serviceButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    public void onSignupFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _serviceButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        String mobile = _mobileText.getText().toString();

       /* if (username.isEmpty()) {
            _usernameText.setError("Enter Valid Username");
            valid = false;
        } else {
            _usernameText.setError(null);
        }


        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid Email Address");
            valid = false;
        } else {
            _emailText.setError(null);
        }*/

        if (mobile.isEmpty() || mobile.length() != 11) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }



        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
