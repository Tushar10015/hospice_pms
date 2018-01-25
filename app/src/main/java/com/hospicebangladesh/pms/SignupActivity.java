package com.hospicebangladesh.pms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
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
import com.hospicebangladesh.pms.model.Profile;
import com.hospicebangladesh.pms.repo.ProfileRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    public String signupPostUrl = "http://2aitbd.com/pms/api/signup.php";

    String gender = null, age = null;


    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_username)
    EditText _usernameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_mobile)
    EditText _mobileText;
    @Bind(R.id.input_age)
    Spinner _ageSpinner;
    @Bind(R.id.input_gender)
    Spinner _genderSpinner;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);


        initializeSpinner();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (JSONException e) {

                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }


    public void initializeSpinner() {

        String[] genderList = new String[]{"Select Gender", "Male", "Female"};

        String[] ageList = new String[100];
        ageList[0] = "Select Age";
        for (int i = 1; i < 100; i++) {
            ageList[i] = i + "";
        }

        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, genderList);

        _genderSpinner.setAdapter(adapterGender);

        _genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                gender = (String) parent.getItemAtPosition(position);
                Log.v(TAG, " Gender " + gender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> adapterAge = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ageList);

        _ageSpinner.setAdapter(adapterAge);

        _ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                age = (String) parent.getItemAtPosition(position);
                Log.v(TAG, " Age " + age);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void signup() throws JSONException {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

          _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        Profile profile=new Profile();
        profile.setName(name);
        profile.setUsername(username);
        profile.setEmail(email);
        profile.setMobile(mobile);
        profile.setPassword(password);

      final List<Profile> list = new ArrayList<>();
        list.add(profile);

        JSONObject postBody = new JSONObject();
        postBody.put("name", name);
        postBody.put("username", username);
        postBody.put("email", email);
        postBody.put("mobile", mobile);
        postBody.put("password", password);
        postBody.put("gender", gender);
        postBody.put("age", age);


        try {
            HttpRequest.postRequest(signupPostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    SignupActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                if (success == 1) {
                                    ProfileRepository.insert(getApplicationContext(),list);
                                    onSignupSuccess();
                                    progressDialog.dismiss();
                                } else {
                                    onSignupFailed();
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
                    SignupActivity.this.runOnUiThread(new Runnable() {
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


 /*new android.os.Handler().postDelayed(
                new Runnable() {
        public void run() {
            // On complete call either onSignupSuccess or onSignupFailed
            // depending on success
            onSignupSuccess();
            // onSignupFailed();
            progressDialog.dismiss();
        }
    }, 3000);*/


    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        startActivity(new Intent(this, MainActivity.class));
          finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String username = _usernameText.getText().toString();
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

        if (username.isEmpty()) {
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
}
