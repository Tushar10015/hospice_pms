package com.hospicebangladesh.rpms;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hospicebangladesh.rpms.http.HttpRequest;
import com.hospicebangladesh.rpms.http.HttpRequestCallBack;
import com.hospicebangladesh.rpms.model.Profile;
import com.hospicebangladesh.rpms.utils.Session;
import com.hospicebangladesh.rpms.utils.SessionManager;
import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity implements LabelledSpinner.OnItemChosenListener {

    private static final String TAG = "SignupActivity";
    public String signupPostUrl = "signup.php";

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

    @Bind(R.id.chk_terms)
    CheckBox _chk_terms;
    @Bind(R.id.input_address)
    EditText _addressText;

    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;

    @Bind(R.id.input_nid)
    EditText _nidText;


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

     //   setSignup();


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




        _chk_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {

                    AlertDialog alertDialog = new AlertDialog.Builder(SignupActivity.this).create();
                    alertDialog.setTitle("Terms and Condition");
                    alertDialog.setMessage("1.Licence and assignment\n" +
                            "2.Third Party Applications\n" +
                            "3.User generated content\n" +
                            "4.Consideration\n" +
                            "5.User Guidelines\n" +
                            "6.Copyright infringement\n" +
                            "7.Technology limitations and modifications\n" +
                            "8.Export control");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                    _chk_terms.setError(null);
                }
            }
        });



    }






    public void initializeSpinner() {

        String[] genderList = new String[]{ "Male", "Female"};

        String[] ageList = new String[100];


        for (int i = 0; i < 100; i++) {
            ageList[i] = (i+1) + "";
        }

        _genderSpinner.setItemsArray(genderList);
        _genderSpinner.setOnItemChosenListener(this);

        _ageSpinner.setItemsArray(ageList);
        _ageSpinner.setOnItemChosenListener(this);

    }

    private void setSignup() {

        _nameText.setText("Md. Developer");
        _emailText.setText("saniultushar@gmail.com");
        _mobileText.setText("01748702672");
        _genderSpinner.setSelection(1);
        _ageSpinner.setSelection(1);
        _passwordText.setText("123456");
        _reEnterPasswordText.setText("123456");
        _addressText.setText("test");

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

        final String name = _nameText.getText().toString();

        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String address = _addressText.getText().toString();
        String nid = _nidText.getText().toString();


        Profile profile = new Profile();
        profile.setName(name);
        profile.setEmail(email);
        profile.setMobile(mobile);
        profile.setPassword(password);
        profile.setGender(gender);
        profile.setAge(age);
        profile.setAddress(address);
        profile.setNid(nid);

        final List<Profile> list = new ArrayList<>();
        list.add(profile);

        JSONObject postBody = new JSONObject();
        postBody.put("name", name);
        postBody.put("email", email);
        postBody.put("mobile", mobile);
        postBody.put("password", password);
        postBody.put("gender", gender);
        postBody.put("age", age);
        postBody.put("address", address);
        postBody.put("nid", nid);

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
                                //    ProfileRepository.insert(getApplicationContext(), list);
                                    int insertid = json.getInt("insertid");
                                    String status=json.getString("status");
                                    String message=json.getString("message");
                                    onSignupSuccess(insertid,status,message);
                                    Session.savePreference(getApplicationContext(),Session.name,name);
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


    public void onSignupSuccess(int insertid,String status,String message) {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        SessionManager sessionManager=new SessionManager(insertid,mobile,password);
        Session.savePreference(getApplicationContext(),sessionManager);
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

        if (!_chk_terms.isChecked()) {
            _chk_terms.setError("Please check terms and condition");
            valid = false;
        } else {
            _chk_terms.setError(null);
        }


        return valid;
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
