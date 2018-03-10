package com.hospicebangladesh.pms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hospicebangladesh.pms.http.HttpRequest;
import com.hospicebangladesh.pms.http.HttpRequestCallBack;
import com.hospicebangladesh.pms.model.Medicine;
import com.hospicebangladesh.pms.model.Profile;
import com.hospicebangladesh.pms.repo.ProfileRepository;
import com.hospicebangladesh.pms.utils.Session;
import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity implements LabelledSpinner.OnItemChosenListener {

    private static final String TAG = "ProfileActivity";
    public String profileUpdatePostUrl = "profile_update.php";
    public String profileGetPostUrl = "get_profile.php";

    String gender = null, age = null;

    @Bind(R.id.input_name)
    EditText _nameText;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_mobile)
    EditText _mobileText;
    @Bind(R.id.input_age)
    LabelledSpinner _ageSpinner;
    @Bind(R.id.input_gender)
    LabelledSpinner _genderSpinner;

    @Bind(R.id.input_address)
    EditText _addressText;

    @Bind(R.id.input_nid)
    EditText _nidText;

    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _mobileText.setEnabled(false);

      /*  List<Profile> profileList = ProfileRepository.getAll(getApplicationContext());

        for (Profile profile : profileList) {

            _nameText.setText(profile.getName());

            _mobileText.setText(profile.getMobile());
            _emailText.setText(profile.getEmail());
            _passwordText.setText(profile.getPassword());
            _reEnterPasswordText.setText(profile.getPassword());

            profile.getGender();
            profile.getAge();
        }*/


        initializeSpinner();
        try {
            getProfile();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (JSONException e) {

                }
            }
        });


    }


    public void initializeSpinner() {

        String[] genderList = new String[]{"Male", "Female"};

        String[] ageList = new String[100];
        for (int i = 0; i < 100; i++) {
            ageList[i] = (i + 1) + "";
        }

        _genderSpinner.setItemsArray(genderList);
        _genderSpinner.setOnItemChosenListener(this);

        _ageSpinner.setItemsArray(ageList);
        _ageSpinner.setOnItemChosenListener(this);


    }

    public void getProfile() throws JSONException {

        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Account...");
        progressDialog.show();

        String user_id = Session.getPreference(getApplicationContext(), Session.user_id);

        JSONObject postBody = new JSONObject();
        postBody.put("user_id", user_id);

        try {
            HttpRequest.postRequest(profileGetPostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    JSONArray jsonArrayProfiles = json.getJSONArray("profiles");
                                    for (int i = 0; i < jsonArrayProfiles.length(); i++) {
                                        JSONObject objProfiles = jsonArrayProfiles.getJSONObject(i);

                                        String name = objProfiles.getString("name");
                                        String password = objProfiles.getString("password");
                                        String phone = objProfiles.getString("phone");
                                        String email = objProfiles.getString("email");
                                        String gender = objProfiles.getString("gender");
                                        String age = objProfiles.getString("age");
                                        String address = objProfiles.getString("address");
                                        String nid = objProfiles.getString("nid");

                                        Session.savePreference(getApplicationContext(), Session.name, name);

                                        _nameText.setText(name);

                                        _mobileText.setText(phone);
                                        _emailText.setText(email);
                                        _passwordText.setText(password);
                                        _reEnterPasswordText.setText(password);
                                        _addressText.setText(address);
                                        _nidText.setText(nid);

                                        if (gender.equals("Male")) {
                                            _genderSpinner.setSelection(0);
                                        } else {
                                            _genderSpinner.setSelection(1);
                                        }

                                        _ageSpinner.setSelection((Integer.parseInt(age) - 1));

                                    }
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
                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server is not responding properly", Toast.LENGTH_LONG).show();
                            _signupButton.setEnabled(true);
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

    public void signup() throws JSONException {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed("Validation Failed");
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();

        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String address = _addressText.getText().toString();
        String nid = _nidText.getText().toString();
        JSONObject postBody = new JSONObject();

        String user_id = Session.getPreference(getApplicationContext(), Session.user_id);
        postBody.put("user_id", user_id);
        postBody.put("name", name);
        postBody.put("email", email);
        postBody.put("mobile", mobile);
        postBody.put("password", password);
        postBody.put("gender", gender);
        postBody.put("age", age);
        postBody.put("address", address);
        postBody.put("nid", nid);

        try {
            HttpRequest.postRequest(profileUpdatePostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    onSignupSuccess(message);
                                    Session.savePreference(getApplicationContext(), Session.name, name);
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
                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server is not responding properly", Toast.LENGTH_LONG).show();
                            _signupButton.setEnabled(true);
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
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    public void onSignupFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();

        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("At Least 3 Characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid Email Address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 11) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 4 and 10 Alphanumeric Characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do Not Match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
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
        if (id == R.id.action_logout) {
            Session.clearPreference(getApplicationContext());
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        switch (labelledSpinner.getId()) {
            case R.id.input_gender:
                gender = (String) adapterView.getItemAtPosition(position);
                break;
            case R.id.input_age:
                age = (String) adapterView.getItemAtPosition(position);
                break;

        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

    }
}
